package com.example.b00682737.maryportholidaypark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        signOut = (Button) findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut(); //sign user out
                finish();
                startActivity(new Intent(ProfilePage.this, LoginPage.class));
                //this will log the user out and return to the login page
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.makeABooking) {
            Intent myintent = new Intent(ProfilePage.this, BookingPage.class);
            ProfilePage.this.startActivity(myintent); //to display the booking page once selected

            return false;


        } if (id == R.id.cancelBooking) {
               Intent cancelintent = new Intent(ProfilePage.this, CancelBooking.class);
                ProfilePage.this.startActivity(cancelintent); //to display the cancel booking page

               return false; }

        return super.onOptionsItemSelected(item);
            }

        }


