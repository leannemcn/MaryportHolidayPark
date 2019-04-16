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
import com.example.b00682737.maryportholidaypark.R;

import com.example.b00682737.maryportholidaypark.Activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    private EditText _edtEmail;
    private EditText _edtName;
    private EditText _edtPhone;
    private EditText _edtAddress;
    private EditText _edtPassword;
    private ProgressBar _pbRegister;
    private DatabaseReference _mDB;
    private FirebaseAuth _mAuth;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        _mDB = FirebaseDatabase.getInstance().getReference();
        _mAuth = FirebaseAuth.getInstance();
        _edtEmail = view.findViewById(R.id.edt_register_email);
        _edtName = view.findViewById(R.id.edt_register_name);
        _edtPhone = view.findViewById(R.id.edt_register_phone);
        _edtAddress = view.findViewById(R.id.edt_register_address);
        _edtPassword = view.findViewById(R.id.edt_register_pwd);
        _pbRegister = view.findViewById(R.id.pb_register);

        Button btnRegister = view.findViewById(R.id.btn_register_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_pbRegister.getVisibility() == View.GONE) {
                    _pbRegister.setVisibility(View.VISIBLE);
                }
                registerUser();
            }
        });

        TextView lblLogin = view.findViewById(R.id.lbl_register_login);
        lblLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.login_content, new LoginFragment());
                ft.commit();
            }
        });

        return view;
    }

    public void registerUser() {
        if(_edtEmail.getText().toString() == "") {
            if(_pbRegister.getVisibility() == View.VISIBLE) {
                _pbRegister.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), "Please input email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(_edtName.getText().toString() == "") {
            if(_pbRegister.getVisibility() == View.VISIBLE) {
                _pbRegister.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), "Please input user name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(_edtPhone.getText().toString() == "") {
            if(_pbRegister.getVisibility() == View.VISIBLE) {
                _pbRegister.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), "Please input phone number!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(_edtAddress.getText().toString() == "") {
            if(_pbRegister.getVisibility() == View.VISIBLE) {
                _pbRegister.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), "Please input address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(_edtPassword.getText().toString() == "") {
            if(_pbRegister.getVisibility() == View.VISIBLE) {
                _pbRegister.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), "Please input password!", Toast.LENGTH_SHORT).show();
            return;
        }

        _mAuth.createUserWithEmailAndPassword(_edtEmail.getText().toString(), _edtPassword.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            createUserInfoOnDB();
                        } else {
                            if(_pbRegister.getVisibility() == View.VISIBLE) {
                                _pbRegister.setVisibility(View.GONE);
                            }
                            Toast.makeText(getActivity(), "Register Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createUserInfoOnDB() {
        FirebaseUser currentUser = _mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        HashMap<String, Object> tmpData = new HashMap<>();

        tmpData.put("name", _edtName.getText().toString());
        tmpData.put("email", _edtEmail.getText().toString());
        tmpData.put("phone", _edtPhone.getText().toString());
        tmpData.put("address", _edtAddress.getText().toString());
        tmpData.put("id", uid);

        _mDB.child("users/" + uid).updateChildren(tmpData)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            if(_pbRegister.getVisibility() == View.VISIBLE) {
                                _pbRegister.setVisibility(View.GONE);
                            }
                            Toast.makeText(getActivity(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(mainIntent);
                            getActivity().finish();
                        } else {
                            if(_pbRegister.getVisibility() == View.VISIBLE) {
                                _pbRegister.setVisibility(View.GONE);
                            }
                            Toast.makeText(getActivity(), "Register Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
