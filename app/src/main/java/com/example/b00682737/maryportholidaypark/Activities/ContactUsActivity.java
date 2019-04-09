package com.example.b00682737.maryportholidaypark.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.b00682737.maryportholidaypark.FirebaseInfo;
import com.example.b00682737.maryportholidaypark.Models.MessageInfo;
import com.example.b00682737.maryportholidaypark.Models.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactUsActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {
    FirebaseAuth mAuth;
    UserInfo userInfo;

    EditText edtEmail;
    EditText edtPhone;
    EditText edtMessage;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // Init UI Elements
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmail.setKeyListener(null);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPhone.setKeyListener(null);

        edtMessage = (EditText) findViewById(R.id.edtMessage);

        edtEmail.setText(userInfo.email);
        edtPhone.setText(userInfo.phone);

        findViewById(R.id.btnSubmit).setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move the map location to the park
        LatLng MaryportHolidayPark = new LatLng(54.670930, -4.882914);
        mMap.addMarker(new MarkerOptions().position(MaryportHolidayPark).title("Maryport Drummore Stranraer DG9 9RD"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MaryportHolidayPark));
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btnSubmit) {
            sendFeedBack();
        }
    }

    private void sendFeedBack() {

        hideKeyboard(edtEmail);
        hideKeyboard(edtPhone);
        hideKeyboard(edtMessage);

        String strEmail = edtEmail.getText().toString().trim();
        String strPhone = edtPhone.getText().toString().trim();
        String strMessage = edtMessage.getText().toString().trim();

        if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPhone)) {
            showToastMessage(R.string.error_empty_user_fields);
            return;
        }

        // Check Email
        if (!isValidEmail(strEmail)) {
            showToastMessage(R.string.error_invalid_email);
            return;
        }

        // Check Message
        if (TextUtils.isEmpty(strMessage)) {
            showToastMessage(R.string.error_empty_message);
            return;
        }

        showProgressDialog();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.email = strEmail;
        messageInfo.phone = strPhone;
        messageInfo.message = strMessage;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(FirebaseInfo.MESSAGES).push().setValue(messageInfo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                hideProgressDialog();

                if (databaseError == null) {
                    showAlert(R.string.success_send_message, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else {
                    showToastMessage(databaseError.getMessage());
                }
            }
        });
    }
}

