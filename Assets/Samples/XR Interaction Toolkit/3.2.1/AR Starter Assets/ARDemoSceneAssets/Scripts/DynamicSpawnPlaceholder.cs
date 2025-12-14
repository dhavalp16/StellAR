using UnityEngine;


namespace UnityEngine.XR.Interaction.Toolkit.Samples.ARStarterAssets
{
    public class DynamicSpawnPlaceholder : MonoBehaviour
    {
        void Start()
        {
            // Fix Rotation Snap: Disable TrackRotation so it doesn't face camera on click
            var interactable = GetComponent<UnityEngine.XR.Interaction.Toolkit.Interactables.XRGrabInteractable>();
            if (interactable != null)
            {
                interactable.trackRotation = false;
            }

            var loader = DynamicModelLoader.Instance;
            if (loader != null && !string.IsNullOrEmpty(loader.PendingPath))
            {
                loader.LoadModelInto(loader.PendingPath, this.gameObject);
            }
            else
            {
                Debug.LogWarning("DynamicSpawnPlaceholder instantiated but no PendingPath set!");
            }
        }
    }
}
