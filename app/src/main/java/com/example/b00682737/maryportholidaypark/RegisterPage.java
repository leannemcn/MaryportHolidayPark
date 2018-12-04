package com.example.b00682737.maryportholidaypark;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {

    EditText registerUsername, registerPassword, registerNumber, registerAddress, registerEmail;
    Button registerButton;
    TextView displayLogin;
    FirebaseAuth firebaseAuth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        setupUIViews();


        firebaseAuth1 = FirebaseAuth.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valid()) {
                    String registerusername = registerUsername.getText().toString().trim();
                    String registerpassword = registerPassword.getText().toString().trim();
                    //username and password will be validated and uploaded and stored to the database

                    firebaseAuth1.createUserWithEmailAndPassword(registerusername, registerpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    // creates user with authentic username and password
                            {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                firebaseAuth1.signOut();
                                Toast.makeText(RegisterPage.this, "Registration complete", Toast.LENGTH_LONG) .show();
                                finish();
                                startActivity(new Intent(RegisterPage.this, LoginPage.class));
                                //will return user to login page providing valid details have been entered

                            }else{ Toast.makeText(RegisterPage.this, "Registration has failed", Toast.LENGTH_LONG) .show();
                            }
                        }
                    });
                }
            }
        });

        displayLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPage.this, LoginPage.class));
                //will bring user back to the Login page
            }
        });
    }

    private void setupUIViews() {
        registerUsername = (EditText) findViewById(R.id.registerUsername);
        registerAddress = (EditText) findViewById(R.id.registerAddress);
        registerNumber = (EditText) findViewById(R.id.registerNumber);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerEmail = (EditText) findViewById(R.id.registerEmail);
        registerButton = (Button) findViewById(R.id.registerButton);
        displayLogin = (TextView) findViewById(R.id.displayLogin);
    }


    private Boolean valid() {
        Boolean result = false;
        String name = registerUsername.getText().toString();
        String address = registerAddress.getText().toString();
        String number = registerNumber.getText().toString();
        String password = registerPassword.getText().toString();
        String email = registerEmail.getText().toString();

        if (name.isEmpty() || address.isEmpty() || number.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all required spaces", Toast.LENGTH_LONG).show();
            //to make sure all required fields are filled in
        } else {
            result = true;
        }
        return result;
    }
}

