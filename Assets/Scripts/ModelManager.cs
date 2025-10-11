using UnityEngine;
using UnityEngine.Networking;
using TMPro; // For the Dropdown UI
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Threading.Tasks; // Required for async Tasks
using GLTFast; // For loading the GLB models

// Helper class to parse JSON from the server
[System.Serializable]
public class ModelList
{
    public List<string> models;
}


public class ModelManager : MonoBehaviour
{
    // --- SERVER SETTINGS ---
    // !!! IMPORTANT: Add your computer's local IP addresses here !!!
    private List<string> serverIPs = new List<string> { "192.168.1.2", "192.168.1.15" };
    private string currentServerIP;
    private string serverUrl;
    
    void Awake()
    {
        Debug.Log("*** MODELMANAGER SCRIPT IS RUNNING! ***");
        Debug.LogError("*** THIS IS A TEST ERROR MESSAGE ***");
    }

    // --- SCENE REFERENCES ---
    [Header("Scene References")]
    [Tooltip("The Dropdown UI element for model selection.")]
    public TMP_Dropdown modelDropdown;

    [Tooltip("Reference to the PlaceOnIndicator script on your XR Origin.")]
    public PlaceOnIndicator placementScript;

    // --- INTERNAL STATE ---
    private List<string> modelNames = new List<string>();
    private GameObject currentLoadedModel;
    private Shader urpLitShader;

    void Start()
    {
        // Load the URP Lit shader to prevent it from being stripped
        Material urpLitMaterial = Resources.Load<Material>("URPLitReference");
        if (urpLitMaterial != null)
        {
            urpLitShader = urpLitMaterial.shader;
            Debug.Log("Successfully loaded URP Lit shader from Resources.");
        }
        else
        {
            Debug.LogError("Failed to load URPLitReference material from Resources. Make sure the material exists in a Resources folder.");
        }

        // Clear dropdown and add a default "loading" option
        modelDropdown.ClearOptions();
        modelDropdown.options.Add(new TMP_Dropdown.OptionData("Connecting to server..."));
        modelDropdown.interactable = false;

        // Add a listener that calls OnDropdownValueChanged when the selection changes
        modelDropdown.onValueChanged.AddListener(OnDropdownValueChanged);

        // Start the process of fetching the models
        StartCoroutine(FetchAndPrepareModels());
    }

    private IEnumerator FetchAndPrepareModels()
    {
        // Try to connect to any of the available server IPs
        bool connectionSuccessful = false;
        
        foreach (string ip in serverIPs)
        {
            Debug.Log($"[ModelManager] Trying to connect to IP: {ip}");
            currentServerIP = ip;
            serverUrl = $"https://{currentServerIP}:5000";
            
            // Update dropdown to show current attempt
            modelDropdown.ClearOptions();
            modelDropdown.options.Add(new TMP_Dropdown.OptionData($"Trying {ip}..."));
            modelDropdown.RefreshShownValue();
            
            yield return StartCoroutine(TestServerConnectivity());
            
            // Check if this IP was successful by seeing if currentServerIP is still set
            if (currentServerIP != null)
            {
                connectionSuccessful = true;
                Debug.Log($"[ModelManager] Successfully connected to {currentServerIP}");
                break;
            }
            else
            {
                Debug.Log($"[ModelManager] Failed to connect to {ip}, trying next IP...");
            }
        }
        
        if (connectionSuccessful)
        {
            yield return StartCoroutine(FetchModelListFromServer());
            PopulateDropdown();
        }
        else
        {
            // All IPs failed
            modelDropdown.ClearOptions();
            modelDropdown.options.Add(new TMP_Dropdown.OptionData("All servers unreachable!"));
            modelDropdown.RefreshShownValue();
            Debug.LogError("[ModelManager] Failed to connect to any server IP");
        }
    }
    
    private IEnumerator TestServerConnectivity()
    {
        // Test connectivity using the /list-models endpoint instead of root
        string testUrl = serverUrl + "/list-models";
        Debug.Log($"[ModelManager] Testing connectivity to {testUrl}");
        Debug.Log($"[ModelManager] Server IP: {currentServerIP}");
        Debug.Log($"[ModelManager] Full URL: {testUrl}");
        
        UnityWebRequest www = UnityWebRequest.Get(testUrl);
        www.certificateHandler = new AcceptAllCertificatesSigned();
        www.timeout = 10; // Increased timeout for better debugging
        
        Debug.Log($"[ModelManager] Sending web request to: {testUrl}");
        yield return www.SendWebRequest();
        Debug.Log($"[ModelManager] Web request completed. Result: {www.result}");

        if (www.result == UnityWebRequest.Result.Success)
        {
            Debug.Log($"[ModelManager] SUCCESS! Server is reachable at {currentServerIP}!");
            Debug.Log($"[ModelManager] Server response: {www.downloadHandler.text}");
            Debug.Log($"[ModelManager] Response code: {www.responseCode}");
            modelDropdown.ClearOptions();
            modelDropdown.options.Add(new TMP_Dropdown.OptionData($"Connected to {currentServerIP}! Loading models..."));
            modelDropdown.RefreshShownValue();
            // Keep currentServerIP as is (don't set to null)
        }
        else
        {
            Debug.LogError($"[ModelManager] FAILED! Server connectivity test failed for {currentServerIP}");
            Debug.LogError($"[ModelManager] Error: {www.error}");
            Debug.LogError($"[ModelManager] Response code: {www.responseCode}");
            Debug.LogError($"[ModelManager] Result: {www.result}");
            Debug.LogError($"[ModelManager] URL attempted: {testUrl}");
            
            // Set currentServerIP to null to indicate this IP failed
            currentServerIP = null;
        }
    }

    private IEnumerator FetchModelListFromServer()
    {
        string listUrl = serverUrl + "/list-models";
        Debug.Log($"Attempting to connect to: {listUrl}");
        
        UnityWebRequest www = UnityWebRequest.Get(listUrl);
        www.certificateHandler = new AcceptAllCertificatesSigned();
        www.timeout = 10; // 10 second timeout
        yield return www.SendWebRequest();

        if (www.result == UnityWebRequest.Result.Success)
        {
            Debug.Log($"Server response: {www.downloadHandler.text}");
            ModelList list = JsonUtility.FromJson<ModelList>(www.downloadHandler.text);
            modelNames = list.models;
            Debug.Log($"Successfully fetched model list. Found {modelNames.Count} models.");
        }
        else
        {
            Debug.LogError($"Failed to fetch model list: {www.error}");
            Debug.LogError($"Response code: {www.responseCode}");
            Debug.LogError($"URL attempted: {listUrl}");
        }
    }

    private void PopulateDropdown()
    {
        Debug.Log($"[ModelManager] PopulateDropdown called with {modelNames.Count} models");
        modelDropdown.ClearOptions();

        if (modelNames.Count == 0)
        {
            Debug.LogWarning("[ModelManager] No models found! Check server connection.");
            modelDropdown.options.Add(new TMP_Dropdown.OptionData("Connection Failed!"));
            modelDropdown.interactable = false;
            return;
        }

        modelDropdown.options.Add(new TMP_Dropdown.OptionData("Select a Model"));
        foreach (string modelName in modelNames)
        {
            Debug.Log($"[ModelManager] Adding model to dropdown: {modelName}");
            modelDropdown.options.Add(new TMP_Dropdown.OptionData(modelName));
        }

        modelDropdown.interactable = true;
        modelDropdown.value = 0;
        modelDropdown.RefreshShownValue();
        Debug.Log("[ModelManager] Dropdown populated successfully");
    }

    public void OnDropdownValueChanged(int index)
    {
        if (index == 0)
        {
            placementScript.SetPrefabToPlace(null);
            placementScript.SetCurrentModelName("");
            return;
        }

        string selectedModelName = modelNames[index - 1];
        
        // Set the current model name for planet detection
        placementScript.SetCurrentModelName(selectedModelName);

        // MODIFICATION: Call the async method directly instead of as a coroutine
        LoadModel(selectedModelName);
    }

    // Public method to manually test connection (can be called from UI button)
    public void TestConnection()
    {
        Debug.Log("[ModelManager] Manual connection test triggered");
        StartCoroutine(FetchAndPrepareModels());
    }

    // MODIFICATION: Changed from 'IEnumerator' to 'async void' to allow 'await'
    private async void LoadModel(string modelName)
    {
        string filePath = Path.Combine(Application.persistentDataPath, modelName);

        if (!File.Exists(filePath))
        {
            // MODIFICATION: Await the async download task
            await DownloadModel(modelName, filePath);
        }

        if (File.Exists(filePath))
        {
            if (currentLoadedModel != null)
            {
                Destroy(currentLoadedModel);
            }

            var gltf = new GltfImport();
            
            // This is now valid because the method is async
            bool success = await gltf.Load(filePath);

            if (success)
            {
                GameObject loadedObj = new GameObject("LoadedModelPrefab");
                // This is now valid because the method is async
                success = await gltf.InstantiateMainSceneAsync(loadedObj.transform);

                if (success)
                {
                    // Configure the loaded model for AR placement
                    loadedObj.SetActive(false);
                    
                    // Note: Scale will be set by AutoRotate component based on planet data
                    
                    // Add a collider if it doesn't have one (optional, for interaction)
                    if (loadedObj.GetComponent<Collider>() == null)
                    {
                        loadedObj.AddComponent<BoxCollider>();
                    }
                    
                    currentLoadedModel = loadedObj;

                    // Add some debug information
                    Debug.Log($"Created GLB GameObject: {loadedObj.name}, Children: {loadedObj.transform.childCount}");
                    Debug.Log($"Model scale: {loadedObj.transform.localScale}");
                    
                    // Debug materials and textures
                    DebugModelMaterials(loadedObj);
                    
                    // Try to fix materials for URP
                    UnityEngine.Texture baseMap = null;
                    UnityEngine.Texture normalMap = null;
                    UnityEngine.Texture metallicRoughnessMap = null;

                    if (gltf.TextureCount > 0)
                    {
                        for (int i = 0; i < gltf.TextureCount; i++)
                        {
                            var texture = gltf.GetTexture(i);
                            string lowerCaseName = texture.name.ToLower();

                            if ((lowerCaseName.Contains("base") || lowerCaseName.Contains("albedo") || lowerCaseName.Contains("diffuse")) && !lowerCaseName.Contains("normal"))
                            {
                                baseMap = texture;
                            }
                            else if (lowerCaseName.Contains("normal"))
                            {
                                normalMap = texture;
                            }
                            else if (lowerCaseName.Contains("metallic") || lowerCaseName.Contains("roughness"))
                            {
                                metallicRoughnessMap = texture;
                            }
                        }

                        // Fallback for base map if not found with specific keywords
                        if (baseMap == null)
                        {
                            for (int i = 0; i < gltf.TextureCount; i++)
                            {
                                var texture = gltf.GetTexture(i);
                                string lowerCaseName = texture.name.ToLower();
                                if (!lowerCaseName.Contains("normal") && !lowerCaseName.Contains("metallic") && !lowerCaseName.Contains("roughness"))
                                {
                                    baseMap = texture;
                                    break;
                                }
                            }
                        }
                    }

                    FixMaterialsForURP(loadedObj, baseMap, normalMap, metallicRoughnessMap);
                    
                    placementScript.SetPrefabToPlace(currentLoadedModel);
                    Debug.Log($"Successfully loaded {modelName} and set it as the placement prefab.");
                    Debug.Log($"Prefab reference passed to PlaceOnIndicator: {currentLoadedModel != null}");
                }
                else
                {
                    Debug.LogError($"Failed to instantiate GLB model: {modelName}");
                    Destroy(loadedObj);
                }
            }
            else
            {
                Debug.LogError($"Failed to load GLB file: {modelName}");
            }
        }
    }

    // MODIFICATION: Changed from 'IEnumerator' to 'async Task' to be awaitable
    private async Task DownloadModel(string modelName, string savePath)
    {
        string modelUrl = $"{serverUrl}/models/{modelName}";
        UnityWebRequest www = UnityWebRequest.Get(modelUrl);
        www.certificateHandler = new AcceptAllCertificatesSigned();
        www.timeout = 30; // 30 second timeout for model downloads

        Debug.Log($"Downloading {modelName} from {modelUrl}...");
        // MODIFICATION: Use await instead of yield return for the web request
        var asyncOp = www.SendWebRequest();
        while (!asyncOp.isDone)
        {
            await Task.Yield();
        }

        if (www.result == UnityWebRequest.Result.Success)
        {
            try
            {
                // Using WriteAllBytesAsync for better performance
                await File.WriteAllBytesAsync(savePath, www.downloadHandler.data);
                Debug.Log($"Successfully downloaded and saved {modelName}. File size: {www.downloadHandler.data.Length} bytes");
            }
            catch (System.Exception e)
            {
                Debug.LogError($"Failed to save {modelName}: {e.Message}");
            }
        }
        else
        {
            Debug.LogError($"Failed to download {modelName}: {www.error}");
            Debug.LogError($"Response code: {www.responseCode}");
            Debug.LogError($"URL attempted: {modelUrl}");
        }
    }
    
    private void DebugModelMaterials(GameObject model)
    {
        Renderer[] renderers = model.GetComponentsInChildren<Renderer>();
        Debug.Log($"Found {renderers.Length} renderers in the model");
        
        for (int i = 0; i < renderers.Length; i++)
        {
            Renderer renderer = renderers[i];
            Debug.Log($"Renderer {i}: {renderer.name}, Materials: {renderer.materials.Length}");
            
            for (int j = 0; j < renderer.materials.Length; j++)
            {
                UnityEngine.Material mat = renderer.materials[j];
                if (mat != null)
                {
                    Debug.Log($"  Material {j}: {mat.name}, Shader: {mat.shader.name}");
                    
                    // Check for main texture
                    if (mat.HasProperty("_MainTex"))
                    {
                        UnityEngine.Texture mainTex = mat.GetTexture("_MainTex");
                        Debug.Log($"    Main Texture: {(mainTex != null ? mainTex.name : "null")}");
                    }
                    
                    // Check for base map (URP)
                    if (mat.HasProperty("_BaseMap"))
                    {
                        UnityEngine.Texture baseMap = mat.GetTexture("_BaseMap");
                        Debug.Log($"    Base Map: {(baseMap != null ? baseMap.name : "null")}");
                    }
                }
                else
                {
                    Debug.LogWarning($"  Material {j}: NULL MATERIAL!");
                }
            }
        }
    }
    
    private void FixMaterialsForURP(GameObject model, UnityEngine.Texture baseMap, UnityEngine.Texture normalMap, UnityEngine.Texture metallicRoughnessMap)
    {
        Renderer[] renderers = model.GetComponentsInChildren<Renderer>();
        
        foreach (Renderer renderer in renderers)
        {
            UnityEngine.Material[] materials = renderer.materials;
            for (int i = 0; i < materials.Length; i++)
            {
                UnityEngine.Material mat = materials[i];
                if (mat != null)
                {
                    Debug.Log($"Attempting to convert material {mat.name} from {mat.shader.name} to URP shader");
                    
                    if (urpLitShader != null)
                    {
                        mat.shader = urpLitShader;
                        Debug.Log("   Applied URP Lit shader.");

                        if (baseMap != null && mat.HasProperty("_BaseMap"))
                        {
                            mat.SetTexture("_BaseMap", baseMap);
                            Debug.Log($"   Applied base map: {baseMap.name}");
                        }

                        if (normalMap != null && mat.HasProperty("_BumpMap"))
                        {
                            mat.SetTexture("_BumpMap", normalMap);
                            mat.EnableKeyword("_NORMALMAP");
                            Debug.Log($"   Applied normal map: {normalMap.name}");
                        }

                        if (metallicRoughnessMap != null && mat.HasProperty("_MetallicGlossMap"))
                        {
                            mat.SetTexture("_MetallicGlossMap", metallicRoughnessMap);
                            Debug.Log($"   Applied metallic/roughness map: {metallicRoughnessMap.name}");
                        }
                    }
                    else
                    {
                        Debug.LogWarning("Could not find Universal Render Pipeline/Lit shader");
                    }
                }
            }
        }
    }
}