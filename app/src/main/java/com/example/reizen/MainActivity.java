package com.example.reizen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button login_now_btn;
    EditText login_email,login_pass;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView goToResisterBtn , forgotPassTV;

    ProgressBar progressView;

    FirebaseFirestore db;
    TextView goPlaceListTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login_email = findViewById(R.id.log_email);
        login_pass = findViewById(R.id.log_pass);
        login_now_btn = findViewById(R.id.log_now);
        forgotPassTV = findViewById(R.id.log_forgot);

        goPlaceListTV = findViewById(R.id.go_placelist);

        mAuth = FirebaseAuth.getInstance();


        goToResisterBtn = findViewById(R.id.go_register);

        //progressView.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        forgotPassTV.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ForgotPassActivity.class));
        });

        login_now_btn.setOnClickListener(v->{

            String email = login_email.getText().toString();
            String pass = login_pass.getText().toString();



            if(email.length()>5 && pass.length()>5){
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        user = mAuth.getCurrentUser();

                        if(task.isSuccessful() ){
                            if(user.isEmailVerified()){


                            }
                            else {
                                startActivity(new Intent(getApplicationContext(),EmailVerifyActivity.class));
                            }


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Login Failed \n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(), "Enter Valid Information", Toast.LENGTH_SHORT).show();
            }




        });


        goToResisterBtn.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

        });


        goPlaceListTV.setOnClickListener(v-> {

            startActivity(new Intent(getApplicationContext(), PlaceListActivity.class));

        });



    }
}