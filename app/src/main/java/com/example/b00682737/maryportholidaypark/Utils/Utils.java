package com.example.b00682737.maryportholidaypark.Utils;

import com.google.firebase.auth.FirebaseAuth;

public class Utils {

    private FirebaseAuth _mAuth;

    public Utils() {
        _mAuth = FirebaseAuth.getInstance();
    }

    public void logOut() {
        _mAuth.signOut();
    }
}
