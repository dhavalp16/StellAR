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

    public void SetPrefabToPlace(GameObject prefab)
    {
        placedPrefab = prefab;
        Debug.Log($"[PlaceOnIndicator] Prefab set to: {(prefab != null ? prefab.name : "null")}");
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
            GameObject placedObject = Instantiate(placedPrefab, placementIndicator.transform.position, placementIndicator.transform.rotation);
            placedObject.SetActive(true);
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
}
