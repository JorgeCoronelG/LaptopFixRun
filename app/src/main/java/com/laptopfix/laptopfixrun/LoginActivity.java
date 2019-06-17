package com.laptopfix.laptopfixrun;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;
import com.laptopfix.laptopfixrun.Util.Common;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    //widgets
    private EditText etEmail, etPassword;
    private Button btnAccess, btnRegister;
    private UserController userController;
    private CustomerController customerController;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userController = new UserController(this);
        customerController = new CustomerController(this);

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
                /*Intent intent1 = new Intent(LoginActivity.this, HomeCustomerActivity.class);
                startActivity(intent1);
                finish();*/
                if(!checkFields()){
                    userController.login(getUser());
                }
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public User getUser(){
        User user = new User();
        user.setEmail(etEmail.getText().toString());
        user.setPassword(etPassword.getText().toString());
        return user;
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

    @Override
    public void requestFinished(String title) {
        if(title.equals(getString(R.string.login_customer))){
            checkCustomer();
        }else if(title.equals(getString(R.string.login_laptopfix))){
            Toast.makeText(this, "Bienvenido Laptop Fix", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCustomer() {
        Customer customer = customerController.getCustomer();
        reference = database.getReference(Common.CUSTOMER_TABLE);
        auth.signInWithEmailAndPassword(customer.getUser().getEmail(), customer.getUser().getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        customerController.getDialog().dismiss();

                        Intent intent = new Intent(LoginActivity.this, HomeCustomerActivity.class);
                        intent.putExtra("section", R.id.nav_establecimiento);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customerController.getDialog().dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}