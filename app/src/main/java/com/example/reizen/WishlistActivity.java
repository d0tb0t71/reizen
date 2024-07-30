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
import androidx.annotation.Nullable;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WishlistActivity extends AppCompatActivity implements OnClickListeners {

    DrawerLayout drawerLayout;
    ImageView backBtn, navBtn;
    NavigationView navView;
    PlaceScrapper placeScrapper = new PlaceScrapper();
    ArrayList<PlaceModel> placeList = new ArrayList<PlaceModel>();

    RecyclerView wishlistListRecyclerView;
    PlaceAdapter placeAdapter;
    ProgressBar progressView;

    FirebaseFirestore db;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist);

        drawerLayout = findViewById(R.id.wishlist_drawerLayout);
        navView = findViewById(R.id.navView);
        navBtn = findViewById(R.id.navBtn);
        navView.bringToFront();

        wishlistListRecyclerView = findViewById(R.id.wishListRecyclerView);
        progressView = findViewById(R.id.progressView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        wishlistListRecyclerView.setLayoutManager(layoutManager);

        placeList = new ArrayList<>();

        placeAdapter = new PlaceAdapter(getApplicationContext(),placeList, WishlistActivity.this, true);
        wishlistListRecyclerView.setAdapter(placeAdapter);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        getWishlist();

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
    }

    void getWishlist(){

        db.collection("users")
                .document(user.getUid())
                .collection("wishlist")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Toast.makeText(getApplicationContext(), "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {

                                PlaceModel placeModel = dc.getDocument().toObject(PlaceModel.class);

                                placeList.add(placeModel);

                            }

                        }

                        placeAdapter.notifyDataSetChanged();

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
                                .delete();

                        Toast.makeText(getApplicationContext(), "Place deleted from wishlist", Toast.LENGTH_SHORT).show();

                        placeList.remove(model);
                        placeAdapter.notifyDataSetChanged();

                        return true;

                    }else {

                        return false;
                    }
                }
            });

            popupMenu.show();

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