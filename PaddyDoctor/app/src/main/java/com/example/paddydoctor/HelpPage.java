package com.example.paddydoctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.paddydoctor.MainActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class HelpPage extends AppCompatActivity {
    KvkFinder kv = new KvkFinder();
    double latitude = KvkFinder.kvklat;
    double longitude = KvkFinder.kvklong;
    private TextView textView;
    private String Tungro = "• Yellow or orange-yellow discoloration is noticeable in tungro-infected plants.\n" +
            "• Discoloration begins from the leaf tip and extends down to the blade or the lower leaf portion.\n" +
            "• Infected leaves may also show mottled or striped appearance, rust-colored spots, and inter-veinal necrosis.";
    private String Brownspot = "• Infected seedlings have small, circular, yellow brown or brown lesions that may girdle the coleoptile and distort primary and secondary leaves.\n" +
            "• Starting at tillering stage, lesions can be observed on the leaves. They are initially small, circular, and dark brown to purple-brown.\n" +
            "• Fully developed lesions are circular to oval with a light brown to gray center, surrounded by a reddish brown margin caused by the toxin produced by the fungi.";
    private String Blight = "• Check for wilting and yellowing of leaves, or wilting of seedlings (also called kresek).\n" +
            "\n" +
            "• On seedlings, infected leaves turn grayish green and roll up. As the disease progresses, the leaves turn yellow to straw-colored and wilt, leading whole seedlings to dry up and die.\n" +
            "\n" +
            "• Kresek on seedlings may sometimes be confused with early rice stem borer damage.";
    private String Blast = "• Initial symptoms appear as white to gray-green lesions or spots, with dark green borders.\n" +
            "• Older lesions on the leaves are elliptical or spindle-shaped and whitish to gray centers with red to brownish or necrotic border.\n" +
            "• Some resemble diamond shape, wide in the center and pointed toward either ends.\n" +
            "• Lesions can enlarge and coalesce, growing together, to kill the entire leaves.\n";
    private String Healthy = "• Your plant has been detected has healthy. Please do not hesitate to contact KVK for any queries.";
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        // Create an instance of Open Street maps
        osmdroid();

        // Typing effect
        TextView textView = findViewById(R.id.textview_symptoms_list);
        final Handler handler = new Handler();
        long delay = 50; // in milliseconds

        // Create a Runnable to update the TextView with the next character
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                textView.setText(PredictedDisease().substring(0, index++));
                if (index <= PredictedDisease().length()) {
                    handler.postDelayed(this, delay);
                }
            }
        };

        // Start the animation
        handler.postDelayed(runnable, delay);

        TextView HelpPageKvk = findViewById(R.id.textView);
        String res = kv.KvkFinders(latitude,longitude);
        HelpPageKvk.setText(res);
        TextView HelpPageLocation = findViewById(R.id.textview_location);
        HelpPageLocation.setText(KvkFinder.kvkaddress);
        TextView HelpPageContact = findViewById(R.id.textViewContact);
        HelpPageContact.setText(KvkFinder.kvkcontact);

        // Clicking on the card will enable the user to dial the phone number
        CardView cardView = findViewById(R.id.card_text_location);

        // Set an OnClickListener to the card view
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to launch the phone dialer with the specified phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+KvkFinder.kvkcontact));
                startActivity(intent);
            }
        });
    }


    public void osmdroid(){
        //osmdroid
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));

        MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint point = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);

        map.getController().setZoom(15.0);
        map.getController().setCenter(point);
    }

    public String PredictedDisease(){
        String disease = null;
        switch (MainActivity.PredictedClass){
            case "Blast":
                disease = Blast;
                break;
            case "Blight":
                disease = Blight;
                break;
            case "Brownspot":
                disease = Brownspot;
                break;
            case "Tungro":
                disease = Tungro;
                break;
            default:
                disease = Healthy;
                break;
        }
        return disease;
    }
}
