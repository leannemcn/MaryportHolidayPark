package com.example.b00682737.maryportholidaypark.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b00682737.maryportholidaypark.Activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private TextView tvRemainAttempts;
    private EditText edtLoginEmail;
    private EditText edtLoginPwd;
    private FirebaseAuth _mAuth;
    private ProgressBar pbLogin;
    private Button btnLogin;
    private Integer _remain = 3;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        pbLogin = view.findViewById(R.id.pb_loginLoader);

        edtLoginEmail = view.findViewById(R.id.edt_login_email);
        edtLoginPwd = view.findViewById(R.id.edt_login_pwd);

        TextView tvRegister = view.findViewById(R.id.tv_login_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.login_content, new RegisterFragment());
                ft.commit();
            }
        });

        tvRemainAttempts = view.findViewById(R.id.tv_remain_attempts);

        btnLogin = view.findViewById(R.id.btn_login_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pbLogin.getVisibility() == View.GONE) {
                    pbLogin.setVisibility(View.VISIBLE);
                }
                loginUser(edtLoginEmail.getText().toString(), edtLoginPwd.getText().toString());
            }
        });

        return view;
    }

    private void loginUser(String email, String pwd) {
        if(email.equals("")) {
            if(pbLogin.getVisibility() == View.VISIBLE) {
                pbLogin.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), "Please Input Email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pwd.equals("")) {
            if(pbLogin.getVisibility() == View.VISIBLE) {
                pbLogin.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), "Please Input password!", Toast.LENGTH_SHORT).show();
            return;
        }
        _mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            if(pbLogin.getVisibility() == View.VISIBLE) {
                                pbLogin.setVisibility(View.GONE);
                            }
                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(mainIntent);
                            getActivity().finish();
                        } else {
                            if(pbLogin.getVisibility() == View.VISIBLE) {
                                pbLogin.setVisibility(View.GONE);
                            }
                            Toast.makeText(getActivity(), "Error, failed Login", Toast.LENGTH_SHORT).show();
                            _remain --; //will decrease login attempts

                            tvRemainAttempts.setText("Attempts Remaining: " + _remain);
                            if(_remain == 0) {
                                Toast.makeText(getActivity(), "Please try again later!", Toast.LENGTH_SHORT).show();
                                btnLogin.setEnabled(false);
                            }
                        }
                    }
                });
    }
}

