package com.example.paddydoctor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    // Define the pic id
    private static final int pic_id = 123;
    private static final int PICK_IMAGE_REQUEST = 1;
    // Define the button and imageview type variable
    Button camera_open_id;
    ImageView click_image_id;
    private Button mButtonChooseImage;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Called when the location has changed
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Do something with the location data
                TextView responseTextView = findViewById(R.id.text);
                responseTextView.setText("Longitude: " + longitude + "Latitude: " + latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Check for permission to access location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, locationListener);
        }



        // Ask permission to open image
        String[] permissions = { Manifest.permission.CAMERA };
        ActivityCompat.requestPermissions(this, permissions, pic_id);

        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);

        // Initialize ImageView and Button objects
        // mImageView = findViewById(R.id.click_image);
        mButtonChooseImage = findViewById(R.id.gallery_button);

        // Set OnClickListener on button to launch image picker
        mButtonChooseImage.setOnClickListener(view -> openFileChooser());

        // Camera_open button is for open the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Start the activity with camera_intent, and request pic id

                    startActivityForResult(intent, pic_id);
                }
        });

    }

    @Override
    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Match the request 'pic id with requestCode
        if (requestCode == pic_id ) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI and set it to ImageView
            Uri imageUri = data.getData();
            click_image_id.setImageURI(imageUri);
        }
    }

    // Method to launch image picker
    private void openFileChooser() {
        // Create an Intent object to pick an image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    // Handle permission request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Request location updates
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            0, 0, locationListener);
                }
            }
        }
    }

/*    private void sendImage(File file) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        Request request = new Request.Builder()
                .url("https://us-central1-paddy-disease-classification.cloudfunctions.net/predict")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the error
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String className = jsonObject.getString("class");
                    double confidence = jsonObject.getDouble("confidence");
                    TextView responseTextView = findViewById(R.id.text);
                    responseTextView.setText("Class: " + className);

                    // Use the class name and confidence value as required
                } catch (JSONException e) {
                    // Handle the JSON parsing error
                    String className = "Unknown";
                    TextView responseTextView = findViewById(R.id.text);
                    responseTextView.setText("Class: " + className);
                }
            }
        });
    }*/
}
