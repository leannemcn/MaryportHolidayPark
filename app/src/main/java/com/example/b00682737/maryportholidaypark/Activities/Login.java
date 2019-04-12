package com.example.b00682737.maryportholidaypark.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.b00682737.maryportholidaypark.Fragments.LoginFragment;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.login_content, new LoginFragment());
        ft.commit();
    }
}
