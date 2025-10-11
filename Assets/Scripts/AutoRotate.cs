using UnityEngine;

public class AutoRotate : MonoBehaviour
{
    [Header("Rotation Settings")]
    [Tooltip("Speed of rotation in degrees per second")]
    public float rotationSpeed = 90f;
    
    [Tooltip("Axis to rotate around (X, Y, or Z)")]
    public RotationAxis rotationAxis = RotationAxis.Y;
    
    [Tooltip("Enable/disable rotation")]
    public bool isRotating = true;
    
    [Tooltip("Reverse rotation direction")]
    public bool reverseDirection = false;
    
    [Header("Planet Settings")]
    [Tooltip("Name of the planet (for automatic settings)")]
    public string planetName = "Earth";
    
    [Tooltip("Use planet-specific rotation settings")]
    public bool usePlanetData = true;
    
    [Header("Advanced Settings")]
    [Tooltip("Smooth rotation (uses Slerp) or instant rotation")]
    public bool smoothRotation = true;
    
    [Tooltip("Smooth rotation speed multiplier (higher = faster smoothing)")]
    public float smoothSpeed = 5f;
    
    private Vector3 rotationVector;
    private PlanetData planetData;
    
    public enum RotationAxis
    {
        X,
        Y,
        Z
    }
    
    void Start()
    {
        Debug.Log($"[AutoRotate] AutoRotate component added to {gameObject.name}");
        
        // Get planet data if enabled
        if (usePlanetData)
        {
            planetData = PlanetData.GetPlanetData(planetName);
            ApplyPlanetSettings();
        }
        
        UpdateRotationVector();
    }
    
    private void ApplyPlanetSettings()
    {
        if (planetData != null)
        {
            // Set rotation speed based on planet's actual rotation period, then apply global multiplier
            float baseRotationSpeed = planetData.GetRotationSpeedDegreesPerSecond();
            rotationSpeed = baseRotationSpeed * GlobalSettings.GlobalRotationSpeedMultiplier;
            
            // Set rotation direction based on planet (retrograde planets rotate clockwise)
            reverseDirection = planetData.IsRetrogradeRotation();
            
            // Apply planet-specific scale with global multiplier
            float normalizedScale = planetData.GetNormalizedScale();
            float finalScale = normalizedScale * GlobalSettings.GlobalScaleMultiplier;
            transform.localScale = Vector3.one * finalScale;
            
            Debug.Log($"[AutoRotate] Applied {planetData.planetName} settings:");
            Debug.Log($"  - Base rotation speed: {baseRotationSpeed:F2}°/sec");
            Debug.Log($"  - Global rotation multiplier: {GlobalSettings.GlobalRotationSpeedMultiplier:F2}x");
            Debug.Log($"  - Final rotation speed: {rotationSpeed:F2}°/sec");
            Debug.Log($"  - Rotation direction: {(reverseDirection ? "Clockwise (Retrograde)" : "Counter-clockwise")}");
            Debug.Log($"  - Base scale: {normalizedScale:F2}");
            Debug.Log($"  - Global scale multiplier: {GlobalSettings.GlobalScaleMultiplier:F2}x");
            Debug.Log($"  - Final scale: {finalScale:F2}");
            Debug.Log($"  - Actual rotation period: {planetData.rotationPeriodHours:F1} hours");
        }
    }
    
    void Update()
    {
        if (!isRotating) return;
        
        if (smoothRotation)
        {
            // Smooth rotation using Slerp
            Quaternion targetRotation = transform.rotation * Quaternion.Euler(rotationVector * Time.deltaTime);
            transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, smoothSpeed * Time.deltaTime);
        }
        else
        {
            // Instant rotation
            transform.Rotate(rotationVector * Time.deltaTime);
        }
        
        // Debug log rotation every 2 seconds
        if (Time.time % 2f < Time.deltaTime)
        {
            Debug.Log($"[AutoRotate] {gameObject.name} rotating - Y: {transform.rotation.eulerAngles.y:F1}°");
        }
    }
    
    private void UpdateRotationVector()
    {
        float speed = reverseDirection ? -rotationSpeed : rotationSpeed;
        
        switch (rotationAxis)
        {
            case RotationAxis.X:
                rotationVector = new Vector3(speed, 0, 0);
                break;
            case RotationAxis.Y:
                rotationVector = new Vector3(0, speed, 0);
                break;
            case RotationAxis.Z:
                rotationVector = new Vector3(0, 0, speed);
                break;
        }
    }
    
    // Public methods to control rotation
    public void SetRotationSpeed(float speed)
    {
        rotationSpeed = speed;
        UpdateRotationVector();
        Debug.Log($"[AutoRotate] {gameObject.name} rotation speed set to {speed}°/sec");
    }
    
    public void SetRotationAxis(RotationAxis axis)
    {
        rotationAxis = axis;
        UpdateRotationVector();
        Debug.Log($"[AutoRotate] {gameObject.name} rotation axis set to {axis}");
    }
    
    public void ToggleRotation()
    {
        isRotating = !isRotating;
        Debug.Log($"[AutoRotate] {gameObject.name} rotation {(isRotating ? "enabled" : "disabled")}");
    }
    
    public void ToggleDirection()
    {
        reverseDirection = !reverseDirection;
        UpdateRotationVector();
        Debug.Log($"[AutoRotate] {gameObject.name} rotation direction {(reverseDirection ? "reversed" : "normal")}");
    }
    
    public void SetSmoothRotation(bool smooth)
    {
        smoothRotation = smooth;
        Debug.Log($"[AutoRotate] {gameObject.name} smooth rotation {(smooth ? "enabled" : "disabled")}");
    }
}
