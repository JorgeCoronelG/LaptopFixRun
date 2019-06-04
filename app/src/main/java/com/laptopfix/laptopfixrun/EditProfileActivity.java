package com.laptopfix.laptopfixrun;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    private Toolbar toolbar;
    private EditText etName, etNumber, etEmail;
    private Button btnSave;
    private CustomerController customerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        showToolbar(getString(R.string.editProfile), true);

        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

        Intent intent = getIntent();

        if(intent != null){
            setDataCustomer(intent);
        }

        customerController = new CustomerController(this);
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
                    customerController.update(getCustomer());
                }
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
        }
        return false;
    }

    private Customer getCustomer(){
        Customer customer = customerController.getCustomer();
        customer.setName(etName.getText().toString());
        customer.setNumber(etNumber.getText().toString());
        return customer;
    }

    @Override
    public void requestFinished(String title) {
        if(title.equals(getString(R.string.updateCustomer))){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditProfileActivity.this, HomeCustomer.class);
        intent.putExtra("section", R.id.nav_perfil);
        startActivity(intent);
        finish();
    }

}
