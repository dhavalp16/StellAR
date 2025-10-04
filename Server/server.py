import os
from flask import Flask, jsonify, send_from_directory

app = Flask(__name__)
MODEL_DIRECTORY = "models"

# Endpoint 1: List all available .glb models
@app.route('/list-models', methods=['GET'])
def list_models():
    """Scans the models directory and returns a JSON list of .glb filenames."""
    model_files = []
    for filename in os.listdir(MODEL_DIRECTORY):
        if filename.endswith('.glb'):
            model_files.append(filename)
    
    # Return the list as a JSON object
    return jsonify({"models": model_files})

# Endpoint 2: Serve a specific model file
@app.route('/models/<path:filename>')
def get_model(filename):
    """Serves a requested model file from the models directory."""
    try:
        return send_from_directory(MODEL_DIRECTORY, filename, as_attachment=True)
    except FileNotFoundError:
        return "Model not found", 404

if __name__ == '__main__':
      # Run the server with SSL context, making it accessible via HTTPS
      # Make sure 'cert.pem' and 'key.pem' are in the same directory as this script
      app.run(debug=True, host='0.0.0.0', port=5000, ssl_context=('cert.pem', 'key.pem'))