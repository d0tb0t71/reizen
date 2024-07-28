package com.example.reizen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.reizen.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageView backBtn, navBtn;
    NavigationView navView;
    TextView p_name, p_email, p_mobile;

    ImageView profileImgView;
    Button updateProfileBtn;
    FirebaseFirestore db;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        drawerLayout = findViewById(R.id.drawerLayout);
//        backBtn = findViewById(R.id.backBtn);
        navBtn = findViewById(R.id.navBtn);
        navView = findViewById(R.id.navView);
        navView.bringToFront();

        profileImgView = findViewById(R.id.profileImg);
        p_name = findViewById(R.id.u_name);
        p_email = findViewById(R.id.u_email);
        p_mobile = findViewById(R.id.u_mobile);
        updateProfileBtn = findViewById(R.id.updateProfileBtn);


        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel userModel = document.toObject(UserModel.class);

                        p_name.setText(userModel.getName());
                        p_mobile.setText(userModel.getMobile());
                        p_email.setText(userModel.getEmail());

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

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
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finishAffinity();
                }

                return false;
            }
        });

        updateProfileBtn.setOnClickListener(v-> {

            //startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));

        });

    }
}