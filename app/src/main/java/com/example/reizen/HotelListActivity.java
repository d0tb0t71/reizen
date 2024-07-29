package com.example.reizen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reizen.adapters.HotelAdapter;
import com.example.reizen.adapters.PlaceAdapter;
import com.example.reizen.interfaces.OnClickListeners;
import com.example.reizen.models.HotelModel;
import com.example.reizen.models.PlaceModel;
import com.example.reizen.scrappers.HotelScrapper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HotelListActivity extends AppCompatActivity implements OnClickListeners {


    DrawerLayout drawerLayout;
    ImageView backBtn, navBtn;
    NavigationView navView;
    HotelScrapper hotelScrapper = new HotelScrapper();
    ArrayList<HotelModel> hotelList = new ArrayList<HotelModel>();

    RecyclerView hotelListRecyclerView;
    HotelAdapter hotelAdapter;

    int currentIndex = 1;
    TextView firstTV, prevTV, nextTV, lastTV;
    ProgressBar progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hotel_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.hotelListDrawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        drawerLayout = findViewById(R.id.hotelListDrawerLayout);
        navView = findViewById(R.id.hotelListNavView);
        navBtn = findViewById(R.id.navBtn);
        navView.bringToFront();

        hotelListRecyclerView = findViewById(R.id.hotelListRecyclerView);
        progressView = findViewById(R.id.hotelListProgressView);
//        firstTV = findViewById(R.id.firstTV);
//        prevTV = findViewById(R.id.prevTV);
//        nextTV = findViewById(R.id.nextTV);
//        lastTV = findViewById(R.id.lastTV);

        getHotelList(currentIndex);

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



    }

    private void getHotelList(int currentIndex){

        progressView.setVisibility(View.VISIBLE);
        hotelScrapper.getHotelList(currentIndex, new HotelScrapper.HotelListCallback() {
            @Override
            public void onHotelListRetrieved(ArrayList<HotelModel> list) {


                runOnUiThread(() -> {

                    hotelList = list;

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    hotelListRecyclerView.setLayoutManager(layoutManager);
                    hotelAdapter = new HotelAdapter(getApplicationContext(),hotelList, HotelListActivity.this);
                    hotelListRecyclerView.setAdapter(hotelAdapter);

                    progressView.setVisibility(View.GONE);

                });

            }
        });

    }

    @Override
    public <T> void onClick(T model) {

        if (model instanceof PlaceModel) {
//            PlaceModel placeModel = (PlaceModel) model;
//
//            Intent intent = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
//            intent.putExtra("placeName", placeModel.getName());
//            intent.putExtra("placeLocation", placeModel.getAddress());
//            intent.putExtra("placeImgUrl", placeModel.getImg_url());
//
//            startActivity(intent);
        }

    }

    @Override
    public <T> void onOptionMenuClicked(T model, View v) {

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}