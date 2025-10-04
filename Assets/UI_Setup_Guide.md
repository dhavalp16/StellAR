# StellAR UI Setup Guide

## Overview
This guide explains how to set up the new AR Lab UI based on your Figma design in Unity.

## Files Created
- `Assets/UI Toolkit/ARLabUI.uxml` - The main UI structure
- `Assets/UI Toolkit/ARLabUI.uss` - The styling for the UI
- Updated `ARLabUIManager.cs` - Enhanced UI management
- Updated `ARSceneSetup.cs` - UI setup configuration

## Setup Instructions

### 1. Configure ARSceneSetup Component
In your AR Scene, find the GameObject with the `ARSceneSetup` component and configure:

**UI Assets:**
- **AR Lab UI Template**: Assign `Assets/UI Toolkit/ARLabUI.uxml`
- **AR Lab UI Styles**: Assign `Assets/UI Toolkit/ARLabUI.uss`

**System References:**
- **Model Manager**: Assign your ModelManager component
- **Place On Indicator**: Assign your PlaceOnIndicator component
- **UI Manager**: Will be auto-created if not assigned

### 2. UI Features
The new UI includes:

**Navigation:**
- Home, Models, and AR Lab tabs
- Active tab highlighting

**AR Lab Features:**
- "Place Item" button to open model selection
- "Clear Canvas" button to remove all placed objects
- Model selection popup with scrollable list
- Connection status indicator
- Step-by-step instructions

**Visual Design:**
- Dark theme with blue accents
- Responsive design for mobile devices
- Smooth transitions and hover effects
- Professional AR Lab interface

### 3. Integration with Existing Systems
The UI automatically integrates with:
- **ModelManager**: Displays available models and handles selection
- **PlaceOnIndicator**: Manages AR placement functionality
- **Connection Status**: Shows server connectivity

### 4. Customization
You can customize the UI by:
- Modifying `ARLabUI.uss` for styling changes
- Updating `ARLabUI.uxml` for layout changes
- Extending `ARLabUIManager.cs` for additional functionality

## Testing
1. Open your AR Scene
2. Ensure all references are assigned in ARSceneSetup
3. Play the scene and test the UI interactions
4. Verify model loading and placement functionality

## Troubleshooting
- If UI doesn't appear, check that UIDocument component is present
- If models don't load, verify ModelManager connection
- If placement doesn't work, ensure PlaceOnIndicator is assigned
