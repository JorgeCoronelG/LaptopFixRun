package com.laptopfix.laptopfixrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    private EditText etName, etNumber, etEmail, etPassword;
    private Button btnCreateAccount;
    private CustomerController customerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        showToolbar(getString(R.string.createAccount), true);

        customerController = new CustomerController(this);

        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

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
                if(!checkFields()){
                    customerController.insert(getCustomer());
                }
                break;
        }
    }

    @Override
    public void requestFinished(String title) {
        if(title.equals(getString(R.string.insertCustomer))){
            Intent intent = new Intent(RegisterActivity.this, CompleteActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private Customer getCustomer(){
        Customer customer = new Customer();
        customer.setName(etName.getText().toString());
        customer.setNumber(etNumber.getText().toString());

        User user = new User();
        user.setEmail(etEmail.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.setStatus(1);
        user.setIdTypeUser(2);//2 es el cliente

        customer.setUser(user);

        return customer;
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