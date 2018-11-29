package com.example.b00682737.maryportholidaypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilePage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button createButton, exisitngButton, signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        firebaseAuth = FirebaseAuth.getInstance();

        createButton = (Button) findViewById(R.id.createButton);
        exisitngButton = (Button) findViewById(R.id.exisitngButton);
        signOut = (Button) findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut(); //sign user out
                finish();
                startActivity(new Intent (ProfilePage.this, LoginPage.class ));
                //this will log the user out and return to the login page
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createBooking = new Intent(ProfilePage.this, BookingPage.class);
                ProfilePage.this.startActivity(createBooking);
    }
});
    }
}