from google.cloud import storage
import tensorflow as tf
from PIL import Image
import numpy as np
from flask import Flask, request, jsonify
from flask_cors import CORS

BUCKET_NAME = "rice-tf-model"
class_names = ['Blast', 'Blight', 'Brownspot', 'Tungro']

model = None

app = Flask(__name__)
CORS(app)

def download_blob(bucket_name, source_blob_name, destination_file_name):
    storage_client = storage.Client()
    bucket = storage_client.get_bucket(bucket_name)
    blob = bucket.blob(source_blob_name)
    blob.download_to_filename(destination_file_name)

@app.route('/predict', methods=['POST'])
def predict(arg):
    global model
    if model is None: # If model is already downloaded, dont download again
        download_blob(
            BUCKET_NAME,
            "models/5.h5",
            "/tmp/5.h5",
        )
        model = tf.keras.models.load_model("/tmp/5.h5")

    image = request.files["file"]
    image = np.array(
        Image.open(image).convert("RGB").resize((224, 224)) # image resizing
    )

    #image = image/255 # normalize the image in 0 to 1 range

    img_array = tf.expand_dims(image, 0)
    predictions = model.predict(img_array)

    print("Predictions:",predictions)

    predicted_class = class_names[np.argmax(predictions[0])]
    confidence = round(100 * (np.max(predictions[0])), 2)

    response = jsonify({
        "class": predicted_class,
        "confidence": confidence
    })
    response.headers.add('Access-Control-Allow-Origin', '*')
    response.headers.add('Access-Control-Allow-Methods', 'POST')
    response.headers.add('Access-Control-Allow-Headers', 'Content-Type')
    return response
