package com.laptopfix.laptopfixrun.Activities.Customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.FiscalData;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    private EditText etName, etNumber, etEmail, etPassword;
    private Button btnCreateAccount;
    private CustomerController customerController;
    private AlertDialog dialog;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        showToolbar(getString(R.string.createAccount), true);

        //Init Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(Constants.CUSTOMER_TABLE);

        customerController = new CustomerController(this);
        customerController.setmVolleyListener(this);

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
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateAccount:
                if(!checkFields()){
                    createDialog(getString(R.string.waitAMoment));
                    registerCustomer();
                }
                break;
        }
    }

    private void registerCustomer() {
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Save customer to db
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        final Customer customer = getCustomer();
                        customer.setId(firebaseUser.getUid());
                        final FiscalData fiscalData = new FiscalData(customer.getId(), customer.getName(), null,
                                customer.getPhone(), null,"G03", customer.getEmail());

                        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(customer)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        customerController.insert(customer, password, fiscalData);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Customer getCustomer() {
        Customer customer = new Customer();
        customer.setName(etName.getText().toString());
        customer.setPhone(etNumber.getText().toString());
        customer.setEmail(etEmail.getText().toString());

        return customer;
    }

    private boolean checkFields() {
        if(etName.getText().toString().isEmpty()){
            etName.setError(getString(R.string.required_name));
            return true;
        }else if(etNumber.getText().toString().isEmpty()){
            etNumber.setError(getString(R.string.required_number));
            return true;
        }else if(etNumber.length() != 10){
            etNumber.setError(getString(R.string.required_number_size));
            return true;
        }else if(etEmail.getText().toString().isEmpty()){
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

    @Override
    public void onSuccess(int code) {
        if(code == CommunicationCode.CODE_CUSTOMER_INSERT){
            dialog.dismiss();

            Intent intent = new Intent(RegisterActivity.this, CompleteActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSuccess(int code, Object object) {

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
}