package com.example.b00682737.maryportholidaypark;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    TextView registerHere, Attempts;
    Button loginButton;
    int remaining = 3;
    FirebaseAuth firebaseAuth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        registerHere = (TextView) findViewById(R.id.registerHere);
        Attempts = (TextView) findViewById(R.id.Attempts);
        Attempts.setText("Attempts remaining: 3");
        //this will display the remaining login attempts left under the login button

        firebaseAuth1 = firebaseAuth1.getInstance();
        FirebaseUser user = firebaseAuth1.getCurrentUser();
        if (user != null) { finish();
            startActivity(new Intent(LoginPage.this, ProfilePage.class));
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(loginUsername.getText().toString(), loginPassword.getText().toString());
            } //this will log the user in if username and password are correct/ stored in database
        });
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerOpen = new Intent(LoginPage.this, RegisterPage.class);
                LoginPage.this.startActivity(registerOpen);
            } //this will open register page once selected
        });

    }

    private void validate(String username, String password) { //carries out validation for username and password
        firebaseAuth1.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginPage.this, "Successful Login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginPage.this, ProfilePage.class));
                } else {
                    Toast.makeText(LoginPage.this, "Failed Login", Toast.LENGTH_SHORT).show();
                    remaining --;
                    Attempts.setText("Number of attempts left: " + remaining);
                    if(remaining ==0 ); {loginButton.setEnabled(false);}
                    //this will disable login button after 3 incorrect attempts
                }
            }
        });
    }
}
