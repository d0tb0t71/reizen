package com.example.reizen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmailVerifyActivity extends AppCompatActivity {

    Button verify_refresh_btn;
    FirebaseAuth firebaseAuth;
    TextView resendVmail;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_verify);



        verify_refresh_btn = findViewById(R.id.verify_refresh_btn);
        resendVmail = findViewById(R.id.resendVmail);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        verify_refresh_btn.setOnClickListener(v-> {

            user.reload();

            if(user.isEmailVerified()){

                    startActivity(new Intent(getApplicationContext(), PlaceListActivity.class));
                    finishAffinity();

            }
            else{

                Toast.makeText(this, "Verify your email and try again.", Toast.LENGTH_SHORT).show();

            }

            Log.d("EmailVF", "Clicked");



        });

        resendVmail.setOnClickListener(v-> {

            user.sendEmailVerification();

            Toast.makeText(this, "Verification mail has been sent", Toast.LENGTH_SHORT).show();
        });
    }
}