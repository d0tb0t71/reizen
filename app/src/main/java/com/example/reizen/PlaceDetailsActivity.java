package com.example.reizen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.reizen.models.PlaceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlaceDetailsActivity extends AppCompatActivity {

    ImageView dePlaceImgIV;
    TextView dePlaceNameTV, dePlaceLocTV;

    Button goHotelList,add_to_wishlist, showInMap;

    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        dePlaceNameTV = findViewById(R.id.dePlaceNameTV);
        dePlaceLocTV = findViewById(R.id.dePlaceLocTV);
        dePlaceImgIV = findViewById(R.id.dePlaceImgIV);
        goHotelList = findViewById(R.id.goHotelList);
        showInMap = findViewById(R.id.showInMap);
        add_to_wishlist = findViewById(R.id.add_to_wishlist);

        String placeName = getIntent().getStringExtra("placeName");
        String placeLocation = getIntent().getStringExtra("placeLocation");
        String placeImageUrl = getIntent().getStringExtra("placeImgUrl");


        Glide.with(this)
                .load(placeImageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.baseline_home_24) // Placeholder image
                        .error(R.drawable.more) // Error image in case of loading failure
                )
                .into(dePlaceImgIV);

        dePlaceNameTV.setText(placeName);
        dePlaceLocTV.setText(placeLocation);

        goHotelList.setOnClickListener(v-> {

            startActivity(new Intent(getApplicationContext(), HotelListActivity.class));

        });

        showInMap.setOnClickListener(v-> {

            String map = "http://maps.google.co.in/maps?q=" + placeName;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
            startActivity(intent);

        });

        add_to_wishlist.setOnClickListener(v-> {


            PlaceModel placeModel = new PlaceModel(placeName, placeLocation, placeImageUrl);

            db.collection("users")
                    .document(user.getUid())
                    .collection("wishlist")
                    .document(placeName)
                    .set(placeModel);


            Toast.makeText(this, "Place added to wishlist.", Toast.LENGTH_SHORT).show();

        });


    }
}