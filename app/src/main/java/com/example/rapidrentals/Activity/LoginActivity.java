package com.example.rapidrentals.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rapidrentals.R;
import com.example.rapidrentals.Utility.LoadingDialog;
import com.example.rapidrentals.Utility.SessionManager;
import com.example.rapidrentals.Utility.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private TextView headerText;
    private TextInputLayout emailLayout, passwordLayout;
    private CheckBox rememberMe;
    private Button goToForgotBtn, logInBtn, goToRegisterBtn,admin;

    private LoadingDialog loadingDialog;
    private Animation RL;
    private ImageView bikeLogo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        RL = AnimationUtils.loadAnimation(this, R.anim.login_bike);
        bikeLogo = findViewById(R.id.bikelogo);
        bikeLogo.setAnimation(RL);

        initComponent();
        addEventListener();

        //Check Remember Me
        SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);
        if (sessionManager.isRememberMe()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeFromSession();
            emailLayout.getEditText().setText(rememberMeDetails.get(SessionManager.KEY_RM_EMAILID));
            passwordLayout.getEditText().setText(rememberMeDetails.get(SessionManager.KEY_RM_PASSWORD));
            rememberMe.setChecked(true);
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void addEventListener() {
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();

                String emailId = emailLayout.getEditText().getText().toString().trim();
                String password = passwordLayout.getEditText().getText().toString().trim();

                if (!Validation.validateEmail(emailLayout) | !Validation.validatePassword(passwordLayout)){
                    Toast.makeText(LoginActivity.this, "Validation Failed", Toast.LENGTH_LONG).show();
                    loadingDialog.stopLoadingDialog();
                    return;
                }
                mAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //Remember Me Session
                            if (rememberMe.isChecked()){
                                SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);
                                sessionManager.createRememberMeSession(emailId, password);
                            }

                            Toast.makeText(LoginActivity.this, "Sign In Success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            loadingDialog.stopLoadingDialog();
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_LONG).show();
                            loadingDialog.stopLoadingDialog();
                        }
                    }
                });
            }
        });

        goToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(headerText, "header_text");
                pairs[1] = new Pair<View, String>(emailLayout, "email_trans");
                pairs[2] = new Pair<View, String>(passwordLayout, "password_trans");
                pairs[3] = new Pair<View, String>(logInBtn, "button_trans");
                pairs[4] = new Pair<View, String>(goToRegisterBtn, "goto_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginAdmin.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String>(headerText, "header_text");
                pairs[1] = new Pair<View, String>(emailLayout, "email_trans");
                pairs[2] = new Pair<View, String>(passwordLayout, "password_trans");
                pairs[3] = new Pair<View, String>(logInBtn, "button_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });
        goToForgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                Pair[] pairs = new Pair[4];
                pairs[0] = new Pair<View, String>(headerText, "header_text");
                pairs[1] = new Pair<View, String>(emailLayout, "email_trans");
                pairs[2] = new Pair<View, String>(logInBtn, "button_trans");
                pairs[3] = new Pair<View, String>(goToRegisterBtn, "goto_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });
    }

    private void initComponent() {
        loadingDialog = new LoadingDialog(LoginActivity.this);
        headerText = findViewById(R.id.header);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        rememberMe = findViewById(R.id.remember_me);
        goToForgotBtn = findViewById(R.id.goto_forgot);
        logInBtn = findViewById(R.id.login_button);
        goToRegisterBtn = findViewById(R.id.goto_register);
        admin = findViewById(R.id.admin);    }
}