package com.laptopfix.laptopfixrun.Activities.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    private Toolbar toolbar;
    private EditText etName, etNumber, etEmail;
    private Button btnSave;
    private CustomerController customerController;
    private android.app.AlertDialog dialog;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        showToolbar(getString(R.string.editProfile), true);

        //Init Firebase
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(Common.CUSTOMER_TABLE);

        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);

        customerController = new CustomerController(this);
        customerController.setmVolleyListener(this);

        btnSave.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null){
            setDataCustomer(intent);
        }
    }

    private void setDataCustomer(Intent intent){
        etName.setText(intent.getStringExtra("name"));
        etNumber.setText(intent.getStringExtra("number"));
        etEmail.setText(intent.getStringExtra("email"));
    }

    private void showToolbar(String title, boolean upButton) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                if(!checkFields()){
                    createDialog(getString(R.string.waitAMoment));
                    updateCustomer();
                }
                break;
        }
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
        }
        return false;
    }

    private Customer getCustomer(){
        Customer customer = customerController.getCustomer();
        customer.setName(etName.getText().toString());
        customer.setNumber(etNumber.getText().toString());
        return customer;
    }

    private void updateCustomer(){
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(getCustomer())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customerController.update(getCustomer());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProfileActivity.this, HomeCustomerActivity.class);
        intent.putExtra("section", R.id.nav_perfil);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccess(int code) {
        if(code == CommunicationCode.CODE_CUSTOMER_UPDATE){
            dialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            builder.setMessage(getString(R.string.txtSave));
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            builder.setCancelable(false);
            builder.show();
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
}
