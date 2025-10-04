
using UnityEngine.Networking;

/// <summary>
/// A custom certificate handler that accepts any certificate.
/// WARNING: This is for development purposes only and should not be used in production
/// as it bypasses certificate validation, making the connection vulnerable.
/// </summary>
public class AcceptAllCertificatesSigned : CertificateHandler
{
    protected override bool ValidateCertificate(byte[] certificateData)
    {
        // Always return true to accept any certificate
        return true;
    }
}
