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
    // !!! IMPORTANT: Replace this with your computer's local IP address !!!
    private string serverIP = "192.168.1.2";
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
        // Construct the base URL
        serverUrl = $"https://{serverIP}:5000";
        Debug.Log($"[ModelManager] Starting with server URL: {serverUrl}");

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
        modelDropdown.options.Add(new TMP_Dropdown.OptionData($"Connecting to {serverIP}..."));
        modelDropdown.interactable = false;

        // Add a listener that calls OnDropdownValueChanged when the selection changes
        modelDropdown.onValueChanged.AddListener(OnDropdownValueChanged);

        // Start the process of fetching the models
        StartCoroutine(FetchAndPrepareModels());
    }

    private IEnumerator FetchAndPrepareModels()
    {
        // First test basic connectivity
        yield return StartCoroutine(TestServerConnectivity());
        
        yield return StartCoroutine(FetchModelListFromServer());
        PopulateDropdown();
    }
    
    private IEnumerator TestServerConnectivity()
    {
        Debug.Log($"[ModelManager] Testing connectivity to {serverUrl}");
        Debug.Log($"[ModelManager] Server IP: {serverIP}");
        Debug.Log($"[ModelManager] Full URL: {serverUrl}");
        
        // Update dropdown to show testing status
        modelDropdown.ClearOptions();
        modelDropdown.options.Add(new TMP_Dropdown.OptionData("Testing connection..."));
        modelDropdown.RefreshShownValue();
        
        UnityWebRequest www = UnityWebRequest.Get(serverUrl);
        www.certificateHandler = new AcceptAllCertificatesSigned();
        www.timeout = 10; // Increased timeout for better debugging
        
        yield return www.SendWebRequest();

        if (www.result == UnityWebRequest.Result.Success)
        {
            Debug.Log("[ModelManager] Server is reachable!");
            Debug.Log($"[ModelManager] Server response: {www.downloadHandler.text}");
            modelDropdown.ClearOptions();
            modelDropdown.options.Add(new TMP_Dropdown.OptionData("Server connected! Loading models..."));
            modelDropdown.RefreshShownValue();
        }
        else
        {
            Debug.LogError($"[ModelManager] Server connectivity test failed: {www.error}");
            Debug.LogError($"[ModelManager] Response code: {www.responseCode}");
            Debug.LogError($"[ModelManager] Result: {www.result}");
            Debug.LogError($"[ModelManager] URL attempted: {serverUrl}");
            
            // More specific error message based on the error type
            string errorMessage = "Connection failed";
            if (www.result == UnityWebRequest.Result.ConnectionError)
            {
                errorMessage = "Network error - check server IP/port";
            }
            else if (www.result == UnityWebRequest.Result.ProtocolError)
            {
                errorMessage = "Server error - check server status";
            }
            else if (www.result == UnityWebRequest.Result.DataProcessingError)
            {
                errorMessage = "Data processing error";
            }
            
            modelDropdown.ClearOptions();
            modelDropdown.options.Add(new TMP_Dropdown.OptionData(errorMessage));
            modelDropdown.RefreshShownValue();
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
            return;
        }

        string selectedModelName = modelNames[index - 1];

        // MODIFICATION: Call the async method directly instead of as a coroutine
        LoadModel(selectedModelName);
    }

    // Public method to manually test connection (can be called from UI button)
    public void TestConnection()
    {
        Debug.Log("[ModelManager] Manual connection test triggered");
        StartCoroutine(TestServerConnectivity());
        StartCoroutine(FetchModelListFromServer());
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
                    
                    // Ensure proper scale (GLB models might be very large or very small)
                    loadedObj.transform.localScale = Vector3.one * 0.1f; // Scale down to 10% for AR
                    
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