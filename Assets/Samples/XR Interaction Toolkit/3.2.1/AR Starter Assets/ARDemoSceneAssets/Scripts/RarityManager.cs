using UnityEngine;
using System.Collections.Generic;
using System.IO;

namespace UnityEngine.XR.Interaction.Toolkit.Samples.ARStarterAssets
{
    public class RarityManager : MonoBehaviour
    {
        public static RarityManager Instance { get; private set; }

        [Header("Rarity Colors")]
        public Color LegendaryColor = new Color(1f, 0.84f, 0f); // Gold
        public Color EpicColor = new Color(0.6f, 0.2f, 1f); // Purple
        public Color RareColor = new Color(0.2f, 0.6f, 1f); // Blue
        public Color CommonColor = Color.white;

        void Awake()
        {
            if (Instance == null) Instance = this;
            else Destroy(gameObject);
        }

        public Color GetColor(string rarity)
        {
            if (string.IsNullOrEmpty(rarity)) return CommonColor;

            switch (rarity.ToLower())
            {
                case "legendary": return LegendaryColor;
                case "epic": return EpicColor;
                case "rare": return RareColor;
                default: return CommonColor;
            }
        }

        public string GetRarityForFile(string filePath)
        {
            // Try to find sidecar .json
            // Expected format: model.glb -> model.json
            string metaPath = Path.ChangeExtension(filePath, ".json");
            
            if (File.Exists(metaPath))
            {
                try 
                {
                    string json = File.ReadAllText(metaPath);
                    // Use simple parsing or JsonUtility
                    var data = JsonUtility.FromJson<ModelMetadata>(json);
                    if (data != null && !string.IsNullOrEmpty(data.rarity)) 
                    {
                        return data.rarity;
                    }
                } 
                catch (System.Exception e) 
                {
                    Debug.LogWarning($"[RarityManager] Failed to read metadata for {Path.GetFileName(filePath)}: {e.Message}");
                }
            }
            
            // Fallback: If no metadata, maybe randomized for demo? 
            // Or just return Common.
            return "Common";
        }

        // Helper to Create Fake Metadata for Testing
        public void GenerateDebugMetadata(string filePath)
        {
            string metaPath = Path.ChangeExtension(filePath, ".json");
            if (!File.Exists(metaPath))
            {
                string[] rarities = { "Common", "Common", "Rare", "Epic", "Legendary" };
                string r = rarities[Random.Range(0, rarities.Length)];
                string json = $"{{\"rarity\": \"{r}\", \"xp_reward\": 100}}";
                File.WriteAllText(metaPath, json);
                Debug.Log($"[RarityManager] Created debug metadata for {Path.GetFileName(filePath)}: {r}");
            }
        }
    }

    [System.Serializable]
    public class ModelMetadata
    {
        public string rarity;
        public int xp_reward;
    }
}
