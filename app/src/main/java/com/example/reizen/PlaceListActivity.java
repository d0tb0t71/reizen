package com.example.reizen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reizen.adapters.PlaceAdapter;
import com.example.reizen.interfaces.OnClickListeners;
import com.example.reizen.models.PlaceModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity implements OnClickListeners {


    DrawerLayout drawerLayout;
    ImageView backBtn, navBtn;
    NavigationView navView;
    PlaceScrapper placeScrapper = new PlaceScrapper();
    ArrayList<PlaceModel> placeList = new ArrayList<PlaceModel>();

    RecyclerView placeListRecyclerView;
    PlaceAdapter placeAdapter;

    int currentIndex = 1;
    TextView firstTV, prevTV, nextTV, lastTV;
    ProgressBar progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_list);


        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        navBtn = findViewById(R.id.navBtn);
        navView.bringToFront();

        placeListRecyclerView = findViewById(R.id.placeListRecyclerView);
        progressView = findViewById(R.id.progressView);
        firstTV = findViewById(R.id.firstTV);
        prevTV = findViewById(R.id.prevTV);
        nextTV = findViewById(R.id.nextTV);
        lastTV = findViewById(R.id.lastTV);

        getPlaceList(currentIndex);

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
                else if (id == R.id.HelpMenu){
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

        nextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentIndex += 1;
                getPlaceList(currentIndex);

            }
        });

        firstTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentIndex = 1;
                getPlaceList(currentIndex);

            }
        });

        prevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentIndex > 1){
                    currentIndex += 1;
                    getPlaceList(currentIndex);
                }

            }
        });


        nextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentIndex < 100){
                    currentIndex += 1;
                    getPlaceList(currentIndex);
                }

            }
        });

        lastTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentIndex = 100;
                getPlaceList(currentIndex);

            }
        });


    }

    private void getPlaceList(int currentIndex){

        progressView.setVisibility(View.VISIBLE);
        placeScrapper.getPlaceList(currentIndex, new PlaceScrapper.PlaceListCallback() {
            @Override
            public void onPlaceListRetrieved(ArrayList<PlaceModel> list) {
                runOnUiThread(() -> {

                    placeList = list;

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    placeListRecyclerView.setLayoutManager(layoutManager);
                    placeAdapter = new PlaceAdapter(getApplicationContext(),placeList, PlaceListActivity.this, false);
                    placeListRecyclerView.setAdapter(placeAdapter);

                    progressView.setVisibility(View.GONE);

                });
            }
        });

    }

    @Override
    public <T> void onClick(T model) {

        if (model instanceof PlaceModel) {
            PlaceModel placeModel = (PlaceModel) model;

            Intent intent = new Intent(getApplicationContext(), PlaceDetailsActivity.class);
            intent.putExtra("placeName", placeModel.getName());
            intent.putExtra("placeLocation", placeModel.getAddress());
            intent.putExtra("placeImgUrl", placeModel.getImg_url());

            startActivity(intent);
        }

    }

    @Override
    public <T> void onOptionMenuClicked(T model , View view) {

        if (model instanceof PlaceModel) {

            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.delete) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        db.collection("users")
                                .document(user.getUid())
                                .collection("wishlist")
                                .document(((PlaceModel) model).getName())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        placeList.remove(model);
                                        placeAdapter.notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), "Place deleted from wishlist", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to delete place from wishlist", Toast.LENGTH_SHORT).show();

                                    }
                                });



                        return true;

                    }else {
                        return false;
                    }
                }
            });

        }

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
