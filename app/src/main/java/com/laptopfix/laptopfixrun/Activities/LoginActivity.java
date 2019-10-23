package com.laptopfix.laptopfixrun.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.laptopfix.laptopfixrun.Activities.Customer.RegisterActivity;
import com.laptopfix.laptopfixrun.Activities.LaptopFix.HomeActivity;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.LaptopFixController;
import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.LaptopFix;
import com.laptopfix.laptopfixrun.R;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    //widgets
    private EditText etEmail, etPassword;
    private Button btnAccess, btnRegister;
    private UserController userController;
    private AlertDialog dialog;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userController = new UserController(this);
        userController.setmVolleyListener(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnAccess = findViewById(R.id.btnAccess);
        btnRegister = findViewById(R.id.btnRegister);

        btnAccess.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAccess:
                if(!checkFields()){
                    createDialog(getString(R.string.waitAMoment));
                    final String email = etEmail.getText().toString();
                    final String password = etPassword.getText().toString();
                    userController.login(email, password);
                }
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void loginFirebase(final String email, final String password, final int code) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        dialog.dismiss();

                        if(code == CommunicationCode.CODE_LOGIN_CUSTOMER){
                            Intent intent = new Intent(LoginActivity.this, com.laptopfix.laptopfixrun.Activities.Customer.HomeActivity.class);
                            intent.putExtra("section", R.id.nav_establecimiento);
                            startActivity(intent);
                            finish();
                        }else if(code == CommunicationCode.CODE_LOGIN_TECHNICAL) {
                            Intent intent = new Intent(LoginActivity.this, com.laptopfix.laptopfixrun.Activities.Technical.HomeActivity.class);
                            intent.putExtra("section", R.id.nav_citaL);
                            startActivity(intent);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onSuccess(int code) {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        /*if(code == CommunicationCode.CODE_LOGIN_LAPTOP_FIX){
            LaptopFix laptopFix = new LaptopFix();
            laptopFix.setId(auth.getUid());
            laptopFix.setEmail(etEmail.getText().toString());
            new LaptopFixController(this).setLaptopFix(laptopFix);

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else*/ if(code == CommunicationCode.CODE_LOGIN_CUSTOMER){
            loginFirebase(email, password, CommunicationCode.CODE_LOGIN_CUSTOMER);
        }else if(code == CommunicationCode.CODE_LOGIN_TECHNICAL){
            loginFirebase(email, password, CommunicationCode.CODE_LOGIN_TECHNICAL);
        }
    }

    @Override
    public void onFailure(String error) {
        dialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(message)
                .build();
        dialog.show();
    }

    private boolean checkFields() {
        if(etEmail.getText().toString().isEmpty()){
            etEmail.setError(getString(R.string.required_email));
            return true;
        }else if(etPassword.getText().toString().isEmpty()){
            etPassword.setError(getString(R.string.required_password));
            return true;
        }else if(etPassword.length() < 6){
            etPassword.setError(getString(R.string.required_password_size));
            return true;
        }
        return false;
    }
}