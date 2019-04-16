package com.example.b00682737.maryportholidaypark.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends BaseActivity {
    //Creating variables for all widgets created on the xml page.
    private TextView SignInInfo;
    private Button SignIn;
    private EditText SignInName;
    private EditText SignInPassword;
    private int counter = 5;

    private TextView tvForgetPassword;
    private TextView Registration;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //Assigning variables a respective id.
        SignInInfo = (TextView) findViewById(R.id.tvSigninInfo);
        SignIn = (Button) findViewById(R.id.btnSignin);
        SignInName = (EditText) findViewById(R.id.SigninName);
        SignInPassword = (EditText) findViewById(R.id.etSigninPassword);
        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        Registration = (TextView) findViewById(R.id.tvRegister);

        SignInInfo.setText("Attempts remaining: 5");

        firebaseAuth = firebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Checking User Status
        if (firebaseUser != null) {

            UserInfo userInfo = appSettings.getUser();

            // Check User Role
            if (FirebaseInfo.USER_TYPE_USER.equals(userInfo.uRole)) {
                startActivity(new Intent(SignInActivity.this, UserHomeActivity.class));
                finish();
            } else if (FirebaseInfo.USER_TYPE_ADMIN.equals(userInfo.uRole)) {
                startActivity(new Intent(SignInActivity.this, AdminHomeActivity.class));
                finish();
            }
        }

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(SignInName.getText().toString(), SignInPassword.getText().toString());
            }
        });

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SignInActivity.this, SignupActivity.class), REQUEST_REGISTER);
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                View dialogView = getLayoutInflater().inflate(R.layout.dialog_input_email, null);
                final android.app.AlertDialog inputDlg = new android.app.AlertDialog.Builder(mContext)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                final EditText edtEmail = (EditText) dialogView.findViewById(R.id.edtEmail);
                dialogView.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String mEmail = edtEmail.getText().toString().trim();
                        if (TextUtils.isEmpty(mEmail)) {
                            showToastMessage("Please input your Email");
                            return;
                        }

                        showProgressDialog();
                        inputDlg.dismiss();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(mEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        hideProgressDialog();

                                        if (task.isSuccessful()) {
                                            showAlert(R.string.msg_success_sent_mail_for_password);
                                            Log.d(TAG, "Email sent.");
                                        } else {
                                            showAlert(R.string.msg_failed_reset_password);
                                        }
                                    }
                                });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        inputDlg.dismiss();
                    }
                });

                inputDlg.show();
                inputDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        });
    }

    private void validate(String userName, String userPassword) {

        final String name = SignInName.getText().toString().trim();
        final String password = SignInPassword.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Please enter user information.", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {

                    DatabaseReference mUserDetailDabaseReference = FirebaseDatabase.getInstance().getReference()
                            .child(FirebaseInfo.USERS)
                            .child(FirebaseAuth.getInstance().getUid());

                    ValueEventListener mUserDetailsValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            hideProgressDialog();

                            UserInfo user = null;
                            if (dataSnapshot.exists()) {
                                user = dataSnapshot.getValue(UserInfo.class);

                                // Save Current User Information
                                appSettings.saveUser(user);

                                // Check User Role
                                if (FirebaseInfo.USER_TYPE_USER.equals(user.uRole)) {
                                    Toast.makeText(SignInActivity.this, "You have logged in successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignInActivity.this, UserHomeActivity.class));
                                    finish();
                                } else if (FirebaseInfo.USER_TYPE_ADMIN.equals(user.uRole)) {
                                    Toast.makeText(SignInActivity.this, "You have logged in successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignInActivity.this, AdminHomeActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "You have logged in successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignInActivity.this, UserHomeActivity.class));
                                    finish();
                                }
                            } else {
                                firebaseAuth.signOut();
                                showAlert(R.string.msg_encountered_an_unexpected_error);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgressDialog();
                            showAlert(R.string.msg_encountered_an_unexpected_error);
                        }
                    };

                    showProgressDialog();
                    mUserDetailDabaseReference.addListenerForSingleValueEvent(mUserDetailsValueEventListener);

                } else {
                    Toast.makeText(SignInActivity.this, "You have failed to login.", Toast.LENGTH_LONG).show();
                    //User has 5 attempts to enter the right information. If they run out of attempts
                    // the sign in button will be inactive.
                    counter--;
                    SignInInfo.setText("Attempts Remaining: " + counter);
                    if (counter == 0) {
                        SignIn.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_REGISTER && resultCode == RESULT_OK) {
            SignInName.setText(data.getStringExtra("email"));
            SignInPassword.setText(data.getStringExtra("password"));
        }
    }
}