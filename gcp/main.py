from google.cloud import storage
import tensorflow as tf
from PIL import Image
import numpy as np
from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import csv
import datetime
from io import StringIO

BUCKET_NAME = "rice-tf-model1"
class_names = ['Blast', 'Blight', 'Brownspot', 'Tungro']

model = None
model_health = None

app = Flask(__name__)
CORS(app)

def download_blob(bucket_name, source_blob_name, destination_file_name):
    storage_client = storage.Client()
    bucket = storage_client.get_bucket(bucket_name)
    blob = bucket.blob(source_blob_name)
    blob.download_to_filename(destination_file_name)

@app.route('/predict', methods=['POST'])
def predict(request):
    global model_health,model
    if model_health is None:
        download_blob(
            BUCKET_NAME,
            "models/healthy_unhealthy_50_Epochs.h5",
            "/tmp/healthy_unhealthy_50_Epochs.h5",
        )
        model_health = tf.keras.models.load_model("/tmp/healthy_unhealthy_50_Epochs.h5")

    if model is None:
        download_blob(
            BUCKET_NAME,
            "models/4_disease_classes_Mendeley.h5",
            "/tmp/4_disease_classes_Mendeley.h5",
        )
        model = tf.keras.models.load_model("/tmp/4_disease_classes_Mendeley.h5")
    image = request.files["file"]
    
    image_path = os.path.join('/tmp', image.filename)
    image.save(image_path)
    file_name = image.filename
    folder_name = 'Telemetry'
    
    # Retrieve the lat and lng parameters from the request
    lat = request.args.get('lat')
    lng = request.args.get('lng')
    print(f"Latitude & longitude: {lat}  {lng}")
    
    # Create a client object
    storage_client = storage.Client()

    # Get the bucket object
    bucket = storage_client.bucket(BUCKET_NAME)

    # Get the blob object and upload the file
    blob = bucket.blob('Telemetry/{}'.format(file_name))
    blob.upload_from_filename('/tmp/' + file_name)

    image = np.array(
        Image.open(image_path).convert("RGB").resize((224, 224)) # image resizing
    )

    # check if the image is of healthy or unhealthy 
    health_pred = model_health.predict(np.array([image]))
    health_class = "Healthy" if np.argmax(health_pred[0]) == 0 else "Unhealthy"
    print("Health Prediction:", health_pred, health_class)
    
    # if the plant is unhealthy, detect the disease
    if health_class == "Unhealthy":
        img_array = tf.expand_dims(image, 0)
        predictions = model.predict(img_array)

        print("Predictions:",predictions)

        predicted_class = class_names[np.argmax(predictions[0])]
        confidence = round(100 * (np.max(predictions[0])), 2)

    else:
        predicted_class = "Healthy"
        #confidence = 100.0
        confidence = round(100 * (np.max(health_pred[0])), 2)

    # Open the telemetry.csv file
    telemetry_blob = bucket.blob('telemetry.csv')
    telemetry_str = telemetry_blob.download_as_string()
    telemetry_file = StringIO(telemetry_str.decode('utf-8'))
    telemetry_reader = csv.reader(telemetry_file)

    # Append the new data to the telemetry.csv file
    telemetry_rows = list(telemetry_reader)
    telemetry_rows.append([datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'),file_name, predicted_class, confidence, lat, lng])
    telemetry_data = StringIO()
    telemetry_writer = csv.writer(telemetry_data)
    telemetry_writer.writerows(telemetry_rows)

    # Upload the updated telemetry.csv file to the cloud bucket
    telemetry_blob.upload_from_string(telemetry_data.getvalue())

    response = jsonify({
        "class": predicted_class,
        "confidence": confidence
    })
    response.headers.add('Access-Control-Allow-Origin', '*')
    response.headers.add('Access-Control-Allow-Methods', 'POST')
    response.headers.add('Access-Control-Allow-Headers', 'Content-Type')
    return response