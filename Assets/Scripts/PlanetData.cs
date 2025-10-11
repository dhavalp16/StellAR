using UnityEngine;

[System.Serializable]
public class PlanetData
{
    [Header("Planet Information")]
    public string planetName;
    public float diameterKm; // Diameter in kilometers
    public float rotationPeriodHours; // Rotation period in hours (positive = counter-clockwise, negative = clockwise)
    
    [Header("Visual Settings")]
    public float scaleMultiplier = 1f; // Relative to Earth
    public Color planetColor = Color.white;
    
    // Real planet data (diameters in km, rotation periods in hours)
    public static PlanetData GetPlanetData(string planetName)
    {
        switch (planetName.ToLower())
        {
            case "sun":
                return new PlanetData
                {
                    planetName = "Sun",
                    diameterKm = 1392700f, // 1,392,700 km
                    rotationPeriodHours = 609.12f, // ~25.38 days
                    scaleMultiplier = 0.8f // Scale down for AR
                };
                
            case "mercury":
                return new PlanetData
                {
                    planetName = "Mercury",
                    diameterKm = 4879f, // 4,879 km
                    rotationPeriodHours = 1407.6f, // 58.65 days
                    scaleMultiplier = 0.3f,
                    planetColor = new Color(0.8f, 0.7f, 0.6f)
                };
                
            case "venus":
                return new PlanetData
                {
                    planetName = "Venus",
                    diameterKm = 12104f, // 12,104 km
                    rotationPeriodHours = -5832.5f, // 243 days (retrograde rotation)
                    scaleMultiplier = 0.4f,
                    planetColor = new Color(1f, 0.9f, 0.7f)
                };
                
            case "earth":
                return new PlanetData
                {
                    planetName = "Earth",
                    diameterKm = 12756f, // 12,756 km
                    rotationPeriodHours = 24f, // 24 hours
                    scaleMultiplier = 0.5f, // Base scale
                    planetColor = new Color(0.3f, 0.6f, 1f)
                };
                
            case "moon":
                return new PlanetData
                {
                    planetName = "Moon",
                    diameterKm = 3475f, // 3,475 km
                    rotationPeriodHours = 655.7f, // 27.32 days
                    scaleMultiplier = 0.2f,
                    planetColor = new Color(0.7f, 0.7f, 0.7f)
                };
                
            case "mars":
                return new PlanetData
                {
                    planetName = "Mars",
                    diameterKm = 6792f, // 6,792 km
                    rotationPeriodHours = 24.6f, // 24.6 hours
                    scaleMultiplier = 0.35f,
                    planetColor = new Color(0.8f, 0.4f, 0.2f)
                };
                
            case "jupiter":
                return new PlanetData
                {
                    planetName = "Jupiter",
                    diameterKm = 142984f, // 142,984 km
                    rotationPeriodHours = 9.9f, // 9.9 hours
                    scaleMultiplier = 0.7f,
                    planetColor = new Color(0.9f, 0.7f, 0.5f)
                };
                
            case "saturn":
                return new PlanetData
                {
                    planetName = "Saturn",
                    diameterKm = 120536f, // 120,536 km
                    rotationPeriodHours = 10.7f, // 10.7 hours
                    scaleMultiplier = 0.6f,
                    planetColor = new Color(1f, 0.9f, 0.6f)
                };
                
            case "uranus":
                return new PlanetData
                {
                    planetName = "Uranus",
                    diameterKm = 51118f, // 51,118 km
                    rotationPeriodHours = -17.2f, // 17.2 hours (retrograde)
                    scaleMultiplier = 0.45f,
                    planetColor = new Color(0.6f, 0.8f, 1f)
                };
                
            case "neptune":
                return new PlanetData
                {
                    planetName = "Neptune",
                    diameterKm = 49528f, // 49,528 km
                    rotationPeriodHours = 16.1f, // 16.1 hours
                    scaleMultiplier = 0.45f,
                    planetColor = new Color(0.3f, 0.5f, 1f)
                };
                
            case "pluto":
                return new PlanetData
                {
                    planetName = "Pluto",
                    diameterKm = 2374f, // 2,374 km
                    rotationPeriodHours = -153.3f, // 6.39 days (retrograde)
                    scaleMultiplier = 0.15f,
                    planetColor = new Color(0.8f, 0.6f, 0.4f)
                };
                
            default:
                // Default to Earth if planet not found
                return GetPlanetData("earth");
        }
    }
    
    // Calculate rotation speed in degrees per second based on rotation period
    public float GetRotationSpeedDegreesPerSecond()
    {
        if (rotationPeriodHours == 0) return 0f;
        
        // Convert hours to seconds and calculate degrees per second
        float rotationPeriodSeconds = Mathf.Abs(rotationPeriodHours) * 3600f;
        float degreesPerSecond = 360f / rotationPeriodSeconds;
        
        // Normalize rotation speeds to reduce drastic differences
        // Use logarithmic scaling to compress the range
        float normalizedSpeed = Mathf.Log(degreesPerSecond * 1000f + 1f) / Mathf.Log(1000f);
        
        // Scale to a reasonable range (5-50 degrees per second)
        float finalSpeed = Mathf.Lerp(5f, 50f, normalizedSpeed);
        
        return finalSpeed;
    }
    
    // Check if planet rotates retrograde (clockwise)
    public bool IsRetrogradeRotation()
    {
        return rotationPeriodHours < 0;
    }
    
    // Get normalized scale relative to Earth
    public float GetNormalizedScale()
    {
        // Use Earth as the reference (diameter = 12,756 km)
        float earthDiameter = 12756f;
        float relativeSize = diameterKm / earthDiameter;
        
        // Use less aggressive normalization to preserve more realistic size differences
        // Apply square root scaling (less compression than cube root)
        float sqrtScale = Mathf.Sqrt(relativeSize);
        
        // Scale to a much wider range for AR (0.1 to 2.0) to show dramatic differences
        // This will make small objects much smaller and large objects much larger
        float normalizedScale = Mathf.Clamp(sqrtScale * 0.8f + 0.1f, 0.1f, 2.0f);
        
        Debug.Log($"[PlanetData] {planetName} scaling: Diameter={diameterKm}km, Relative={relativeSize:F2}x, Sqrt={sqrtScale:F2}, Final={normalizedScale:F2}");
        
        return normalizedScale;
    }
}
