package com.example.reizen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PlaceDetailsActivity extends AppCompatActivity {

    ImageView dePlaceImgIV;
    TextView dePlaceNameTV, dePlaceLocTV;

    Button goHotelList;

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

        dePlaceNameTV = findViewById(R.id.dePlaceNameTV);
        dePlaceLocTV = findViewById(R.id.dePlaceLocTV);
        dePlaceImgIV = findViewById(R.id.dePlaceImgIV);
        goHotelList = findViewById(R.id.goHotelList);

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


    }
}