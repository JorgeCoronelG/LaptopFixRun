package com.laptopfix.laptopfixrun;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private EditText etEmail, etPassword;
    private Button btnAccess, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    //Código a programar
                }
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean checkFields() {
        if(etEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Correo electrónico obligatorio", Toast.LENGTH_SHORT).show();
            return true;
        }else if(etPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Contraseña obligatoria", Toast.LENGTH_SHORT).show();
            return true;
        }else if(etPassword.length() < 6){
            Toast.makeText(this, "Contraseña debe tener 6 dígitos o más", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}