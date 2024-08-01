package com.example.reizen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HelpActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ImageView navBtn;
    NavigationView navView;

    TextView change_pass_help, update_profile_help, how_to_book_help, source_of_data_help;

    LinearLayout source_data_info , book_hotel_info;
    String url = "https://www.trip.com/travel-guide/attraction/sylhet-21399/tourist-attractions/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        navBtn = findViewById(R.id.navBtn);
        navView.bringToFront();

        navBtn.setOnClickListener(v-> {

            drawerLayout.open();

        });

        change_pass_help = findViewById(R.id.change_pass_help);
        update_profile_help = findViewById(R.id.update_profile_help);
        how_to_book_help  = findViewById(R.id.how_to_book_help);
        source_of_data_help  = findViewById(R.id.source_of_data_help);

        source_data_info  = findViewById(R.id.source_data_info);
        book_hotel_info  = findViewById(R.id.book_hotel_info);

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

        change_pass_help.setOnClickListener(v-> {

            resetPass();

        });

        update_profile_help.setOnClickListener(v-> {

            startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));

        });

        how_to_book_help.setOnClickListener(v-> {

            if (book_hotel_info.getVisibility() == View.VISIBLE){
                book_hotel_info.setVisibility(View.GONE);
            }else {
                book_hotel_info.setVisibility(View.VISIBLE);
            }

        });

        source_of_data_help.setOnClickListener(v-> {

            if (source_data_info.getVisibility() == View.VISIBLE){
                source_data_info.setVisibility(View.GONE);
            }else {
                source_data_info.setVisibility(View.VISIBLE);
            }

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

    private void resetPass(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to change your password ?");
        builder.setTitle("Change Password");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

                if(email.length() > 5){

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Password reset mail has been sent to your email", Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(getApplicationContext(), "Failed to sent password reset mail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Operation failed.\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else{
                    Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_SHORT).show();
                }


        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}