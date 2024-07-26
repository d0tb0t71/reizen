package com.example.reizen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {


    ImageView backBtnFPA;
    TextView backBtnLogin;
    EditText for_login_email;
    Button resetPassword, f_go_login, f_go_reg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgor_pass);



//        backBtnFPA = findViewById(R.id.backBtnFPA);
//        backBtnLogin = findViewById(R.id.backBtnLogin);
//        backBtnLogin = findViewById(R.id.backBtnLogin);
        for_login_email = findViewById(R.id.f_email);
        resetPassword = findViewById(R.id.f_reset_now);

        f_go_login = findViewById(R.id.f_go_login);
        f_go_reg = findViewById(R.id.f_go_reg);

        f_go_login.setOnClickListener(v-> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        f_go_reg.setOnClickListener(v-> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });


        resetPassword.setOnClickListener(v -> {

            String email = for_login_email.getText().toString();

            if(email.length() > 5){

                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassActivity.this, "Password reset mail has been sent to your email", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(ForgotPassActivity.this, "Failed to sent password reset mail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ForgotPassActivity.this, "Operation failed.\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            } else{
                Toast.makeText(ForgotPassActivity.this, "Enter valid email address", Toast.LENGTH_SHORT).show();
            }


        });

    }
}