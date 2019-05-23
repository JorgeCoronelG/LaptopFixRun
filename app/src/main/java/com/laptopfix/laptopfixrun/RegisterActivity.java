package com.laptopfix.laptopfixrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etName, etNumber, etEmail, etPassword;
    private Button btnCreateAccount, btnRegisterComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        showToolbar("Crear cuenta", true);

        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnRegisterComplete = findViewById(R.id.btnRegisterComplete);


        btnCreateAccount.setOnClickListener(this);

    }

    private void showToolbar(String title, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
       
        switch (v.getId()){
            case R.id.btnCreateAccount:



                /*RComplete mFragment = new RComplete();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenerdorFrame, mFragment).commit();*/

               // FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.commit();

                    Intent intent = new Intent(RegisterActivity.this, CompleteActivity.class);
                    startActivity(intent);
                    finish();

                   /* FragmentManager fm = getSupportFragmentManager();
                    RComplete register = new RComplete();
                    fm.beginTransaction().replace(R.id.complete, register, null).commit();*/

                   /*RComplete rComplete = new RComplete();
                   getSupportFragmentManager().beginTransaction().add(R.id.frag, rComplete, null).commit();*/

              /*  //Paso 1: Obtener la instancia del administrador de fragmentos
                FragmentManager fragmentManager = getFragmentManager();

                //Paso 2: Crear una nueva transacción
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                //Paso 3: Crear un nuevo fragmento y añadirlo
                RComplete fragment = new RComplete();
                transaction.add(R.id.complete, fragment);

                //Paso 4: Confirmar el cambio
                transaction.commit();*/
                    //getSupportFragmentManager().beginTransaction().add(R.id.frag, register).commit();





                break;

        }
    }

    private boolean checkFields() {
        if(etName.getText().toString().isEmpty()){
            Toast.makeText(this, "Nombre completo obligatorio", Toast.LENGTH_SHORT).show();
            return true;
        }else if(etNumber.getText().toString().isEmpty()){
            Toast.makeText(this, "Número telefónico obligatorio", Toast.LENGTH_SHORT).show();
            return true;
        }else if(etNumber.length() != 10){
            Toast.makeText(this, "Número telefónico incorrecto", Toast.LENGTH_SHORT).show();
            return true;
        }else if(etEmail.getText().toString().isEmpty()){
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