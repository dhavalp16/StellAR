using UnityEngine;

public class GlobalSettings : MonoBehaviour
{
    [Header("Global Rotation Settings")]
    [Tooltip("Global multiplier for all planet rotation speeds. Set this in the Inspector before building the app.")]
    [Range(0.1f, 100f)]
    public float globalRotationSpeedMultiplier = 1.0f;
    
    [Header("Global Scale Settings")]
    [Tooltip("Global multiplier for all planet scales. Set this in the Inspector before building the app.")]
    [Range(0.1f, 5f)]
    public float globalScaleMultiplier = 1.0f;
    
    // Static references to access from other scripts
    public static float GlobalRotationSpeedMultiplier { get; private set; } = 1.0f;
    public static float GlobalScaleMultiplier { get; private set; } = 1.0f;
    
    void Awake()
    {
        // Set the static values from the Inspector values
        GlobalRotationSpeedMultiplier = globalRotationSpeedMultiplier;
        GlobalScaleMultiplier = globalScaleMultiplier;
        
        Debug.Log($"[GlobalSettings] Global rotation speed multiplier set to: {GlobalRotationSpeedMultiplier:F2}x");
        Debug.Log($"[GlobalSettings] Global scale multiplier set to: {GlobalScaleMultiplier:F2}x");
    }
    
    void OnValidate()
    {
        // Update the static values when changed in Inspector (for testing in editor)
        GlobalRotationSpeedMultiplier = globalRotationSpeedMultiplier;
        GlobalScaleMultiplier = globalScaleMultiplier;
    }
    
    // You can add other global settings here in the future
}
