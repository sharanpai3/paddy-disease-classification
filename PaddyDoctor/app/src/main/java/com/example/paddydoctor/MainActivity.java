package com.example.paddydoctor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements LocationListener {
    // Define the pic id
    private static final int pic_id = 123, PICK_IMAGE_REQUEST = 1, REQUEST_PERMISSIONS = 100;
    private static final String ROOT_URL = "https://us-central1-paddy-disease-classification.cloudfunctions.net/predict";
    public double longitude;
    public double latitude;
    // Define the button and imageview type variable
    Button camera_open_id, mButtonChooseImage;
    ImageView imageView;
    TextView text,help;
    private String provider;
    public static double lat;
    public static double lng;
    public static String PredictedClass;
    private LocationManager locationManager;
    private LocationListener locationListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
/*            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");*/
        }
        // Check if location services are enabled
        boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isLocationEnabled) {
            // Prompt the user to turn on location services
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(
                    MainActivity.this,"Please turn on the location",
                    Toast.LENGTH_LONG).show();
        }

        // Initialize ImageView and Button objects
        camera_open_id = findViewById(R.id.camera_button);
        imageView = findViewById(R.id.imageView);
        mButtonChooseImage = findViewById(R.id.gallery_button);
        text =  findViewById(R.id.text);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // Camera_open button is for opening the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(
                v -> {
                    // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // Start the activity with camera_intent, and request pic id
                    startActivityForResult(intent, pic_id);
                });

        // Upload file through the upload button
        mButtonChooseImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    showFileChooser();
                }
            } else {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    showFileChooser();
                }
            }
        });
        TextView text = findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object to navigate to the next activity
                Intent intent = new Intent(MainActivity.this, HelpPage.class);

                // Start the activity using the intent
                startActivity(intent);
            }
        });
        TextView help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object to navigate to the next activity
                Intent intent = new Intent(MainActivity.this, HelpPage.class);

                // Start the activity using the intent
                startActivity(intent);
            }
        });

    }

    @Override
    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Match the request 'pic id with requestCode45
        // Displaying image taken on Camera
        if (requestCode == pic_id && resultCode == RESULT_OK && data != null) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            if (photo != null) {
                uploadBitmap(photo);
                imageView.setImageBitmap(photo);
            } else {
                Log.e("MainActivity", "photo is null");
            }

/*            uploadBitmap(photo);
            // Set the image in imageview for display
            imageView.setImageBitmap(photo);*/
        }

        // When the Gallery button is clicked
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI and set it to ImageView
            Uri imageUri = data.getData();
            String filePath = getPath(imageUri);
            imageView.setImageURI(imageUri);

            if (filePath != null) {
                try {
                    Log.d("filePath", String.valueOf(filePath));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    uploadBitmap(bitmap);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(
                        MainActivity.this,"no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @SuppressLint("Range")
    public String getPath(Uri uri) {
        String path = "";
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String document_id = cursor.getString(0);
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();

                cursor = getContentResolver().query(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
                }
            }
        } else if ("file".equals(uri.getScheme())) {
            path = uri.getPath();
        }
        return path;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        int timeout = 30000; // 30 seconds
        // Show the progress bar until result is received
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if (progressBar != null) {
            // Show the progress bar
            progressBar.setVisibility(View.VISIBLE);
        }
        // Clear text everytime a new request is made
        TextView responseTextView = findViewById(R.id.text);
        responseTextView.setText("");

        TextView responseHelpView = findViewById(R.id.help);
        responseHelpView.setText("");

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {

                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String result1 = obj.getString("class") + " (" + obj.getString("confidence") + "%)";
                            PredictedClass = obj.getString("class");
                            String help = "Seeking help? Click here.";
                            responseTextView.setText(result1);
                            responseHelpView.setText(help);
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
/*        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

}
