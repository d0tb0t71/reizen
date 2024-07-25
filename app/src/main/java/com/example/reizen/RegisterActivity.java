package com.example.reizen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.reizen.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {


    Button reg_btn;
    EditText email, phone, fullName, username;
    EditText pass, confirmPass;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ImageView PRBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.reg_name);
        phone = findViewById(R.id.reg_mobile);
        fullName = findViewById(R.id.reg_name);
        pass = findViewById(R.id.reg_pass);
        confirmPass = findViewById(R.id.reg_cpass);

        reg_btn = findViewById(R.id.reg_now);
//        PRBack = findViewById(R.id.PRBack);

        mAuth = FirebaseAuth.getInstance();


        PRBack.setOnClickListener(v -> {

            getOnBackPressedDispatcher().onBackPressed();

        });

        reg_btn.setOnClickListener(v->{

            String email_st = email.getText().toString();
            String phone_st = phone.getText().toString();
            String fullname_st = fullName.getText().toString();
            String pass_st = pass.getText().toString();
            String c_pass_st = confirmPass.getText().toString();


            if(email_st.length()>5 && pass.length()>5 && fullname_st.length() > 3 && pass_st.equals(c_pass_st) && phone_st.length() > 7 ){

                mAuth.createUserWithEmailAndPassword(email_st,pass_st).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();

                            UserModel userModel = new UserModel(email_st,fullname_st,phone_st,"");

                            db = FirebaseFirestore.getInstance();

                            db.collection("users")
                                    .document(user.getUid())
                                    .set(userModel);

                            user.sendEmailVerification();

                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(),EmailVerifyActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Registration Failed\n"+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Registration Failed !\n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else{
                Toast.makeText(getApplicationContext(), "Please Enter Correct Information", Toast.LENGTH_SHORT).show();
            }


        });
    }
}