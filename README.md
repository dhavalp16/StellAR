# StellAR 🌟

**StellAR** is an Augmented Reality application that allows users to generate unique 3D models using AI (via the StellAR-Server) and place them in the real world.

## 📱 Features
*   **Dynamic AR**: Place 3D objects (`.glb`) onto recognized planes.
*   **AI Generation**: Integrated with **Majorserver** (ComfyUI) to generate models from text/images.
*   **Gamification**: Models have Rarity properties (Common, Rare, Epic, Legendary) visualized by **Colored Borders** in the UI.
*   **Runtime Loading**: Uses `glTFast` to download and instantiate models without app updates.

## 🛠️ Setup Guide

1.  **Unity Version**: Recommended **Unity 2022.3 LTS** or higher.
2.  **Packages Required**:
    *   AR Foundation
    *   XR Interaction Toolkit
    *   Universal Render Pipeline (URP)
    *   glTFast (for importing models)

3.  **Installation**:
    ```bash
    git clone https://github.com/dhavalp16/StellAR.git
    # Open folder in Unity Hub
    ```

## 📂 Key Components (`Assets/`)

*   **`ARTemplateMenuManager.cs`**: The core UI controller. Scans local storage for models and generates specific buttons with Rarity colors.
*   **`RarityManager.cs`**: Handles gamification logic (Gold for Legendary, Purple for Epic, etc.).
*   **`DynamicModelLoader.cs`**: Handles the logic of downloading a `.glb` from a URL or File Path and resizing it for AR.
*   **`CloudModelDebug.cs`**: A debug tool to verify connection to the Python Backend.

## 🔗 Backend Connection
This app requires the **StellAR-Server** to be running for Generation and Discovery features.
*   Clone the server: [StellAR-Server Repo](https://github.com/dhavalp16/StellAR-Server)
*   Ensure the server is running on `http://127.0.0.1:5000` (for Editor testing).

## 🧪 Testing in Editor
1.  Open `Scenes/ARDemoScene`.
2.  Hit **Play**.
3.  Use the **Object Menu** to select and place items.
4.  (Optional) Attach `CloudModelDebug` to an object to test server connectivity.

## 🤝 Contribution
Pull requests are welcome! Please ensure you do not commit the `Library/` folder (checked by `.gitignore`).
