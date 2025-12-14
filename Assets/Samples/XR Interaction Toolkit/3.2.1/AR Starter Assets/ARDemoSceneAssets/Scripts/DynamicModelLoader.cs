using System;
using System.Threading.Tasks;
using UnityEngine;
using GLTFast;
using UnityEngine.XR.Interaction.Toolkit;
using UnityEngine.XR.Interaction.Toolkit.Interactables;

namespace UnityEngine.XR.Interaction.Toolkit.Samples.ARStarterAssets
{
    public class DynamicModelLoader : MonoBehaviour
    {
        public static DynamicModelLoader Instance { get; private set; }

        void Awake()
        {
            if (Instance == null)
                Instance = this;
            else
                Destroy(gameObject);
        }

        public string PendingPath { get; set; }

        public async void LoadModelInto(string path, GameObject container)
        {
            try
            {
                var gltf = new GltfImport();
                var success = await gltf.Load(path);

                if (success)
                {
                    await gltf.InstantiateMainSceneAsync(container.transform);

                    // Normalize Scale (Fit to 0.5m)
                    NormalizeModelScale(container);

                    // Recalculate Collider
                    RecalculateBounds(container);
                }
                else
                {
                    Debug.LogError($"Failed to load GLTF from {path}");
                }
            }
            catch (Exception e)
            {
                Debug.LogError($"Exception loading GLTF: {e}");
            }
        }

        void NormalizeModelScale(GameObject container)
        {
            var renderers = container.GetComponentsInChildren<Renderer>();
            if (renderers.Length == 0) return;

            var bounds = renderers[0].bounds;
            foreach (var r in renderers)
                bounds.Encapsulate(r.bounds);

            float maxDim = Mathf.Max(bounds.size.x, bounds.size.y, bounds.size.z);
            float targetSize = 0.5f;

            if (maxDim > targetSize)
            {
                float scale = targetSize / maxDim;
                
                foreach (Transform child in container.transform)
                {
                    child.localScale *= scale;
                }
                
                Debug.Log($"[DynamicModelLoader] Normalized scale by factor {scale} (Original: {maxDim}m)");
            }
            
            // Re-calculate bounds after scaling to align pivot
            AlignToBottom(container);
        }

        void AlignToBottom(GameObject container)
        {
            var renderers = container.GetComponentsInChildren<Renderer>();
            if (renderers.Length == 0) return;

            var bounds = renderers[0].bounds;
            foreach (var r in renderers)
                bounds.Encapsulate(r.bounds);
            
            // We want bounds.min.y to be at container.position.y
            // Calculate the offset in World Space
            float worldOffset = container.transform.position.y - bounds.min.y;
            
            // Apply to all children
            foreach (Transform child in container.transform)
            {
                child.position += new Vector3(0, worldOffset, 0);
            }
             Debug.Log($"[DynamicModelLoader] Aligned pivot to bottom (Offset: {worldOffset})");
        }

        void RecalculateBounds(GameObject container)
        {
            var collider = container.GetComponent<BoxCollider>();
            if (collider == null) 
                 collider = container.AddComponent<BoxCollider>();

            var renderers = container.GetComponentsInChildren<Renderer>();
            if (renderers.Length == 0) return;

            var bounds = renderers[0].bounds;
            foreach (var r in renderers)
                bounds.Encapsulate(r.bounds);

            collider.center = container.transform.InverseTransformPoint(bounds.center);
            collider.size = container.transform.InverseTransformVector(bounds.size);
        }
    }
}
