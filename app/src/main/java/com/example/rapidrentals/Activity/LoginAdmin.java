package com.example.rapidrentals.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.rapidrentals.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginAdmin extends AppCompatActivity {

    TextInputLayout password, phno;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        password = findViewById(R.id.key);
        phno = findViewById(R.id.admin_no);

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAdmin();
            }
        });

    }

    private void isAdmin() {
        final String userEnteredPassword = password.getEditText().getText().toString().trim();
        final String userEnteredPhno = phno.getEditText().getText().toString().trim();
        if(userEnteredPhno.equals("12345")){
            if(userEnteredPassword.equals("12345")){
                password.setError(null);
                Intent mainIntent = new Intent(getApplication(),MainActivity2.class);
                startActivity(mainIntent);
                finish();
            }
            else {
                password.setError("Wrong Password");
                password.requestFocus();
            }
        }
        else{
            phno.setError("No such Phone number exist! Please signup");
            phno.requestFocus();
        }
    }
}



