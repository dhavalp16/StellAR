using System.Collections;
using System.IO;
using System.Threading.Tasks;
using GLTFast;
using UnityEngine;
using UnityEngine.UI;

namespace UnityEngine.XR.Interaction.Toolkit.Samples.ARStarterAssets
{
    public class ThumbnailGenerator : MonoBehaviour
    {
        public static ThumbnailGenerator Instance { get; private set; }

        [SerializeField] LayerMask m_RenderLayer; 
        Camera m_StudioCamera;
        Light m_StudioLight;
        GameObject m_StudioRoot;

        void Awake()
        {
            if (Instance == null) Instance = this;
            else Destroy(gameObject);

            SetupStudio();
        }

        void SetupStudio()
        {
            // Create a hidden studio far away
            m_StudioRoot = new GameObject("ThumbnailStudio");
            m_StudioRoot.transform.position = new Vector3(0, -9000, 0); // Far far below
            
            // Camera
            var camGo = new GameObject("StudioCamera");
            camGo.transform.SetParent(m_StudioRoot.transform, false);
            m_StudioCamera = camGo.AddComponent<Camera>();
            m_StudioCamera.clearFlags = CameraClearFlags.SolidColor;
            m_StudioCamera.backgroundColor = new Color(0, 0, 0, 0); // Transparent
            m_StudioCamera.cullingMask = 1 << 0; // Layer 0 (Default) - Safer for URP
            m_StudioCamera.nearClipPlane = 0.1f;
            m_StudioCamera.farClipPlane = 100f; // Limit range to avoid seeing the world above
            m_StudioCamera.enabled = false; 

            // Light
            var lightGo = new GameObject("StudioLight");
            lightGo.transform.SetParent(m_StudioRoot.transform, false);
            m_StudioLight = lightGo.AddComponent<Light>();
            m_StudioLight.type = LightType.Directional;
            m_StudioLight.intensity = 1.5f;
            lightGo.transform.rotation = Quaternion.Euler(50, -30, 0);
        }

        public async void GenerateThumbnailFor(string glbPath, Image targetImage)
        {
            string cachePath = Path.ChangeExtension(glbPath, ".png");
            
            // 1. Check Disk Cache (DISABLED FOR DEBUGGING/REPAIR)
            // if (File.Exists(cachePath))
            // {
            //     byte[] bytes = File.ReadAllBytes(cachePath);
            //     Texture2D tex = new Texture2D(2, 2);
            //     tex.LoadImage(bytes);
            //     targetImage.sprite = Sprite.Create(tex, new Rect(0, 0, tex.width, tex.height), new Vector2(0.5f, 0.5f));
            //     return;
            // }

            // 2. Generate
            var container = new GameObject("TempModel");
            container.transform.SetParent(m_StudioRoot.transform, false);
            // Use Default Layer (0)
            SetLayerRecursively(container, 0);

            var gltf = new GltfImport();
            bool success = await gltf.Load(glbPath);
            if (success)
            {
                await gltf.InstantiateMainSceneAsync(container.transform);
                
                // Set Layer again for children
                SetLayerRecursively(container, 0);

                // --- NORMALIZE (Important for huge models) ---
                NormalizeModelScale(container);
                // ---------------------------------------------

                // Frame it
                Bounds bounds = CalculateBounds(container);
                Debug.Log($"[ThumbnailGenerator] Model loaded. Bounds: {bounds}, Center: {bounds.center}, Size: {bounds.size}");
                
                FocusCameraOnBounds(bounds);

                // Wait for textures upload
                await Task.Delay(200); // 200ms delay to be safe

                // Render
                Debug.Log($"[ThumbnailGenerator] Model loaded. Bounds: {bounds}, Center: {bounds.center}, Size: {bounds.size}");
                
                if (bounds.size == Vector3.zero)
                {
                     Debug.LogWarning("[ThumbnailGenerator] Bounds are zero! Model might be empty or waiting for meshes.");
                }

                FocusCameraOnBounds(bounds);

                // Give it a frame to settle (materials/meshes)
                await Task.Yield();

                // Render
                Texture2D snapshot = RenderSnapshot(512, 512);
                
                // Save to Disk
                Debug.Log($"[ThumbnailGenerator] Saving thumbnail to: {cachePath}");
                File.WriteAllBytes(cachePath, snapshot.EncodeToPNG());

                // Apply
                targetImage.sprite = Sprite.Create(snapshot, new Rect(0, 0, snapshot.width, snapshot.height), new Vector2(0.5f, 0.5f));
            }
            else
            {
                 Debug.LogError("[ThumbnailGenerator] Failed to load GLTF for thumbnail.");
            }

            Destroy(container);
        }

        void SetLayerRecursively(GameObject obj, int layer)
        {
            obj.layer = layer;
            foreach (Transform child in obj.transform) SetLayerRecursively(child.gameObject, layer);
        }

        Bounds CalculateBounds(GameObject obj)
        {
            var renderers = obj.GetComponentsInChildren<Renderer>();
            if (renderers.Length == 0) return new Bounds(obj.transform.position, Vector3.zero);
            var b = renderers[0].bounds;
            foreach (var r in renderers) b.Encapsulate(r.bounds);
            return b;
        }

        void FocusCameraOnBounds(Bounds bounds)
        {
            if (bounds.size == Vector3.zero) return;
            
            float maxDim = Mathf.Max(bounds.size.x, bounds.size.y, bounds.size.z);
            float dist = maxDim / (2.0f * Mathf.Tan(0.5f * m_StudioCamera.fieldOfView * Mathf.Deg2Rad));
            
            m_StudioCamera.transform.position = bounds.center - Vector3.forward * (dist * 2.0f); // Back up a bit more
            m_StudioCamera.transform.LookAt(bounds.center);
        }

        void NormalizeModelScale(GameObject container)
        {
            var renderers = container.GetComponentsInChildren<Renderer>();
            if (renderers.Length == 0) return;

            var bounds = renderers[0].bounds;
            foreach (var r in renderers)
                bounds.Encapsulate(r.bounds);

            float maxDim = Mathf.Max(bounds.size.x, bounds.size.y, bounds.size.z);
            float targetSize = 1.0f; // 1m for thumbnail is fine

            if (maxDim > targetSize)
            {
                float scale = targetSize / maxDim;
                foreach (Transform child in container.transform)
                    child.localScale *= scale;
            }
            
            // --- MATERIAL FIX (URP) ---
            // If glTFast loaded materials with "Standard" shader, they might be invisible in URP.
            // Let's force them to URP Lit if they seem broken, or just ensure keywords are set.
            // var renderers = container.GetComponentsInChildren<Renderer>(); // Already defined above
            var replacementShader = Shader.Find("Universal Render Pipeline/Lit");
            if (replacementShader == null) replacementShader = Shader.Find("Universal Render Pipeline/Unlit");
            
            if (replacementShader != null)
            {
               foreach (var r in renderers)
               {
                   foreach (var mat in r.sharedMaterials)
                   {
                       if (mat == null) continue;
                       // If shader is null or Legacy/Standard, swap it
                       if (mat.shader == null || mat.shader.name == "Standard")
                       {
                           Debug.Log($"[ThumbnailGenerator] Replacing incompatible shader on {r.name} with {replacementShader.name}");
                           mat.shader = replacementShader;
                       }
                   }
               }
            }
            // --------------------------

            // Align to center
            Bounds newBounds = CalculateBounds(container);
            container.transform.position = container.transform.position - newBounds.center; 
            // This centers `container` relative to studio root (0,-500,0) generally.
            // Actually, we are parenting to studio root? Yes.
            // FocusCameraOnBounds uses bounds.center so it handles offset, but centering is safer.
        }

        Texture2D RenderSnapshot(int width, int height)
        {
            RenderTexture rt = new RenderTexture(width, height, 24);
            m_StudioCamera.targetTexture = rt;
            m_StudioCamera.Render();
            
            RenderTexture.active = rt;
            Texture2D tex = new Texture2D(width, height, TextureFormat.RGBA32, false);
            tex.ReadPixels(new Rect(0, 0, width, height), 0, 0);
            tex.Apply();
            
            m_StudioCamera.targetTexture = null;
            RenderTexture.active = null;
            Destroy(rt);
            
            return tex;
        }
    }
}
