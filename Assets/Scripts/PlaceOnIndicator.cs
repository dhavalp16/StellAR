using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.ARFoundation;
using UnityEngine.XR.ARSubsystems;
using UnityEngine.InputSystem;
using UnityEngine.EventSystems;

[RequireComponent(typeof(ARRaycastManager))]
public class PlaceOnIndicator : MonoBehaviour
{
    [SerializeField] GameObject placementIndicator;
    [SerializeField] GameObject placedPrefab;
    private List<GameObject> placedObjects = new List<GameObject>();
    [SerializeField] InputAction touchInput;
    ARRaycastManager aRRaycastManager;
    List<ARRaycastHit> hits = new List<ARRaycastHit>();
    
    // Store the current model name for planet detection
    private string currentModelName = "";

    public void SetPrefabToPlace(GameObject prefab)
    {
        placedPrefab = prefab;
        Debug.Log($"[PlaceOnIndicator] Prefab set to: {(prefab != null ? prefab.name : "null")}");
    }
    
    public void SetCurrentModelName(string modelName)
    {
        currentModelName = modelName;
        Debug.Log($"[PlaceOnIndicator] Current model name set to: {modelName}");
    }

    private void Awake()
    {
        aRRaycastManager = GetComponent<ARRaycastManager>();
        touchInput.performed += PlaceObject;
        placementIndicator.SetActive(false);
    }

    private void OnEnable()
    {
        touchInput.Enable();
    }

    private void OnDisable()
    {
        touchInput.Disable();
    }

    private void Update()
    {
        if (aRRaycastManager.Raycast(new Vector2(Screen.width / 2, Screen.height / 2), hits, TrackableType.PlaneWithinPolygon))
        {
            var hitPose = hits[0].pose;
            placementIndicator.transform.SetPositionAndRotation(hitPose.position, hitPose.rotation);
            if (!placementIndicator.activeInHierarchy)
                placementIndicator.SetActive(true);
        }
        else
        {
            placementIndicator.SetActive(false);
        }
    }

    public void PlaceObject(InputAction.CallbackContext context)
    {
        // Get the current pointer position
        Vector2 pointerPosition = Vector2.zero;

        if (context.control.device is Touchscreen touchscreen && touchscreen.primaryTouch.isInProgress)
        {
            pointerPosition = touchscreen.primaryTouch.position.ReadValue();
        }
        else if (context.control.device is Mouse mouse)
        {
            pointerPosition = mouse.position.ReadValue();
        }

        // Check if we're over UI
        if (IsPointerOverUI(pointerPosition))
        {
            Debug.Log("[PlaceOnIndicator] UI interaction detected, ignoring placement.");
            return;
        }

        Debug.Log($"[PlaceOnIndicator] PlaceObject called. Prefab: {(placedPrefab != null ? placedPrefab.name : "null")}, Indicator active: {placementIndicator.activeInHierarchy}");

        if (placedPrefab != null && placementIndicator.activeInHierarchy)
        {
            var offsetPosition = new Vector3(placementIndicator.transform.position.x, placementIndicator.transform.position.y, placementIndicator.transform.position.z);
            GameObject placedObject = Instantiate(placedPrefab, offsetPosition, placementIndicator.transform.rotation);
            placedObject.SetActive(true);
            
            // Add AutoRotate component to the placed object
            AutoRotate autoRotate = placedObject.AddComponent<AutoRotate>();
            
            // Try to detect planet name from the current model name
            string planetName = DetectPlanetName(currentModelName);
            autoRotate.planetName = planetName;
            Debug.Log($"[PlaceOnIndicator] Added AutoRotate component to {placedObject.name} with planet: {planetName} (from model: {currentModelName})");
            
            placedObjects.Add(placedObject); // Add the new object to our list
            Debug.Log($"[PlaceOnIndicator] Successfully placed object: {placedObject.name}");
        }
        else
        {
            if (placedPrefab == null)
                Debug.LogWarning("[PlaceOnIndicator] Cannot place object - no prefab selected!");
            if (!placementIndicator.activeInHierarchy)
                Debug.LogWarning("[PlaceOnIndicator] Cannot place object - placement indicator not active!");
        }
    }

    private bool IsPointerOverUI(Vector2 position)
    {
        // Create a pointer event data with the current position
        PointerEventData eventData = new PointerEventData(EventSystem.current);
        eventData.position = position;

        // Create a list to receive raycast results
        List<RaycastResult> results = new List<RaycastResult>();

        // Raycast using the graphics raycaster
        EventSystem.current.RaycastAll(eventData, results);

        // Check if any of the results are UI elements
        return results.Count > 0;
    }

    // New method to clear all placed objects
    public void ClearAllPlacedObjects()
    {
        foreach (GameObject obj in placedObjects)
        {
            Destroy(obj);
        }
        placedObjects.Clear();
        Debug.Log("[PlaceOnIndicator] All placed objects have been cleared.");
    }
    
    // Method to get the list of placed objects (for ObjectRotator)
    public List<GameObject> GetPlacedObjects()
    {
        return new List<GameObject>(placedObjects);
    }
    
    // Method to detect planet name from prefab name
    private string DetectPlanetName(string prefabName)
    {
        string lowerName = prefabName.ToLower();
        
        // Check for planet names in the prefab name
        if (lowerName.Contains("sun")) return "Sun";
        if (lowerName.Contains("mercury")) return "Mercury";
        if (lowerName.Contains("venus")) return "Venus";
        if (lowerName.Contains("earth")) return "Earth";
        if (lowerName.Contains("moon")) return "Moon";
        if (lowerName.Contains("mars")) return "Mars";
        if (lowerName.Contains("jupiter")) return "Jupiter";
        if (lowerName.Contains("saturn")) return "Saturn";
        if (lowerName.Contains("uranus")) return "Uranus";
        if (lowerName.Contains("neptune")) return "Neptune";
        if (lowerName.Contains("pluto")) return "Pluto";
        
        // Default to Earth if no planet detected
        return "Earth";
    }
}
