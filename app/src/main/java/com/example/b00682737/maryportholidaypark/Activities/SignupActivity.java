package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.b00682737.maryportholidaypark.R;

import com.example.b00682737.maryportholidaypark.FirebaseInfo;
import com.example.b00682737.maryportholidaypark.Models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignupActivity extends BaseActivity {
    private View backButton;
    private EditText userName, userEmail, userPhone, userAddress, userPassword, userPasswordConfirm;
    private Button regButton;
    private TextView userLogIn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = userName.getText().toString().trim();
                final String email = userEmail.getText().toString().trim();
                final String phone = userPhone.getText().toString().trim();
                final String address = userAddress.getText().toString().trim();
                final String password = userPassword.getText().toString().trim();
                final String confirm = userPasswordConfirm.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isEmailValid(email)) {
                    Toast.makeText(SignupActivity.this, "Please enter correct email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!inputsCanBePassword(password) || password.length() < 8) {
                    showAlert("Password should have at least 1 capital with 8 or more characters.");
                    return;
                }

                if (!password.equals(confirm)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgressDialog();
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            UserInfo user = new UserInfo();
                            user.email = email;
                            user.name = name;
                            user.phone = phone;
                            user.address = address;
                            user.id = firebaseAuth.getCurrentUser().getUid();
                            user.uRole = "user";

                            showProgressDialog();

                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child(FirebaseInfo.USERS).child(firebaseAuth.getCurrentUser().getUid()).setValue(user, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    hideProgressDialog();

                                    if (databaseError == null) {
                                        //sendUserData();
                                        firebaseAuth.signOut();

                                        Intent dataIntent = getIntent();
                                        dataIntent.putExtra("email", email);
                                        dataIntent.putExtra("password", password);
                                        setResult(RESULT_OK, dataIntent);

                                        Toast.makeText(SignupActivity.this, "You have Registered Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        showAlert(databaseError.getMessage());
                                    }
                                }
                            });
                        } else {
                            hideProgressDialog();
                            Toast.makeText(SignupActivity.this, "Error! Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        userLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static boolean inputsCanBePassword(String inputs) {

        if(TextUtils.isEmpty(inputs) || inputs.length() < 8)
            return false;

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        if (!UpperCasePatten.matcher(inputs).find()) {
            return false;
        }

        return true;
    }

    private void setupUIViews() {
        backButton = findViewById(R.id.btnBack);
        userName = (EditText) findViewById(R.id.etUsername);
        userEmail = (EditText) findViewById(R.id.etUserEmail);
        userPhone = (EditText) findViewById(R.id.etUserPhone);
        userAddress = (EditText) findViewById(R.id.etUserAddr);
        userPassword = (EditText) findViewById(R.id.etUserPassword);
        userPasswordConfirm = (EditText) findViewById(R.id.edtConfirm);
        regButton = (Button) findViewById(R.id.btnRegister);
        userLogIn = (TextView) findViewById(R.id.tvUserLogin);
    }
}
