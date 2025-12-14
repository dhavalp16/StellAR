using System.Collections;
using UnityEngine;
using UnityEngine.Networking;
// using Newtonsoft.Json; // If available, otherwise simple generic parsing or JsonUtility wrapper

namespace UnityEngine.XR.Interaction.Toolkit.Samples.ARStarterAssets
{
    public class CloudModelDebug : MonoBehaviour
    {
        // Use 127.0.0.1 for editor, but for Android device you'd need the PC's actual IP
        private string serverUrl = "http://127.0.0.1:5000/api/models"; 

        void Start()
        {
            StartCoroutine(FetchModels());
        }

        IEnumerator FetchModels()
        {
            Debug.Log($"[CloudDebug] Connecting to {serverUrl}...");
            using (UnityWebRequest webRequest = UnityWebRequest.Get(serverUrl))
            {
                // Request and wait for the desired page.
                yield return webRequest.SendWebRequest();

                if (webRequest.result != UnityWebRequest.Result.Success)
                {
                    Debug.LogError($"[CloudDebug] Error: {webRequest.error}");
                }
                else
                {
                    string json = webRequest.downloadHandler.text;
                    Debug.Log($"[CloudDebug] Success! Raw JSON: {json}");

                    // Parsing demo (Simple wrapper since JsonUtility needs known schema)
                    CloudModelList data = JsonUtility.FromJson<CloudModelList>("{\"models\":" + json + "}");
                    // note: The array comes as raw [...], JsonUtility needs an object. 
                    // Let's just log the raw text for the proof of concept as it's easiest without external libs.
                    
                    Debug.Log("------------------------------------------------");
                    Debug.Log("CLOUD CONNECTION VERIFIED ✅");
                    Debug.Log("If you see the JSON above containing your model name, Unity can see Supabase!");
                    Debug.Log("------------------------------------------------");
                }
            }
        }
    }

    [System.Serializable]
    public class CloudModelList
    {
        public CloudModel[] models;
    }

    [System.Serializable]
    public class CloudModel
    {
        public int id;
        public string name;
        public string rarity;
        public string storage_url;
    }
}
