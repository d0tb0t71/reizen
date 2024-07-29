package com.example.reizen;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.reizen.models.PlaceModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PlaceDetailsActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageView navBtn;
    NavigationView navView;
    ImageView dePlaceImgIV;
    TextView dePlaceNameTV, dePlaceLocTV;

    Button goHotelList,add_to_wishlist, showInMap,showRestaurants;

    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_details);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        navBtn = findViewById(R.id.navBtn);
        navView.bringToFront();

        dePlaceNameTV = findViewById(R.id.dePlaceNameTV);
        dePlaceLocTV = findViewById(R.id.dePlaceLocTV);
        dePlaceImgIV = findViewById(R.id.dePlaceImgIV);
        goHotelList = findViewById(R.id.goHotelList);
        showInMap = findViewById(R.id.showInMap);
        add_to_wishlist = findViewById(R.id.add_to_wishlist);
        showRestaurants = findViewById(R.id.showRestaurants);

        String placeName = getIntent().getStringExtra("placeName");
        String placeLocation = getIntent().getStringExtra("placeLocation");
        String placeImageUrl = getIntent().getStringExtra("placeImgUrl");

        navBtn.setOnClickListener(v-> {

            drawerLayout.open();

        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.HomeMenu){
                    Intent intent = new Intent(getApplicationContext(), PlaceListActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.ProfileMenu){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.WishlistMenu){
                    Intent intent = new Intent(getApplicationContext(), WishlistActivity.class);
                    startActivity(intent);
                }else if (id == R.id.HelpMenu){
                    Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.AboutMenu){
                    Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(intent);
                }
                else if (id == R.id.LogoutMenu){
                    logout();
                }

                return false;
            }
        });

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

            String mapQuery = "http://maps.google.com/maps/search/?api=1&query=hotels+near+" + Uri.encode(placeName.replaceFirst("^\\d+\\.\\s*", ""));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapQuery));
            startActivity(intent);


        });

        showRestaurants.setOnClickListener(v-> {

            String mapQuery = "http://maps.google.com/maps/search/?api=1&query=restaurants+near+" + Uri.encode(placeName.replaceFirst("^\\d+\\.\\s*", ""));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapQuery));
            startActivity(intent);


        });

        showInMap.setOnClickListener(v-> {

            String map = "http://maps.google.co.in/maps?q=" + placeName.replaceFirst("^\\d+\\.\\s*", "");
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

    private void logout(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to logout ?");
        builder.setTitle("Logout");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finishAffinity();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}