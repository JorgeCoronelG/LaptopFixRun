
package com.laptopfix.laptopfixrun.Activities.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.FiscalDataController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.FiscalData;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import dmax.dialog.SpotsDialog;

public class EditFiscalDataActivity extends AppCompatActivity implements View.OnClickListener, VolleyListener {

    private EditText etName, etAddress, etPhone, etRFC, etEmail;
    private Spinner spnCFDI;
    private Button btnSave;
    private AlertDialog dialog;
    private FiscalData fiscalData;
    private FiscalDataController fiscalDataController;
    private CustomerController customerController;
    private ArrayAdapter<String> adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fiscal_data);

        showToolbar(getString(R.string.edit_fiscal_data), true);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etRFC = findViewById(R.id.etRFC);
        etEmail = findViewById(R.id.etEmail);
        spnCFDI = findViewById(R.id.spnCFDI);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        fiscalDataController = new FiscalDataController(this);
        fiscalDataController.setmVolleyListener(this);
        customerController = new CustomerController(this);

        Intent intent = getIntent();
        if(intent != null){
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.CFDI);
            setFiscalData(intent);
        }
    }

    private void showToolbar(String title, boolean upButton) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    private void setFiscalData(Intent intent) {
        etName.setText(intent.getStringExtra("name"));
        etAddress.setText(intent.getStringExtra("address"));
        etPhone.setText(intent.getStringExtra("phone"));
        etRFC.setText(intent.getStringExtra("rfc"));
        etEmail.setText(intent.getStringExtra("email"));
        spnCFDI.setAdapter(adapter);
        spnCFDI.setSelection(obtenerPosicionItem(spnCFDI, intent.getStringExtra("cfdi")));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                if(checkFields()){
                    createDialog(getString(R.string.waitAMoment));
                    update();
                }
                break;
        }
    }

    public void update(){
        fiscalData = new FiscalData();
        fiscalData.setCustomer(new CustomerController(this).getCustomer().getId());
        fiscalData.setName(etName.getText().toString());
        fiscalData.setAddress(etAddress.getText().toString());
        fiscalData.setPhone(etPhone.getText().toString());
        fiscalData.setRfc(etRFC.getText().toString());
        fiscalData.setCfdi(spnCFDI.getSelectedItem().toString());
        fiscalData.setEmail(etEmail.getText().toString());
        fiscalDataController.update(fiscalData);
    }

    @Override
    public void onSuccess(int code) {
        dialog.dismiss();
        switch (code){
            case CommunicationCode.CODE_UPDATE_FISCAL_DATA:
                customerController.setFiscalData(fiscalData);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.txtSave));
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditFiscalDataActivity.this, HomeActivity.class);
                        intent.putExtra("section", R.id.nav_fiscal_data);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
                break;
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

    public int obtenerPosicionItem(Spinner spinner, String item){
        int posicion = 0;
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) posicion = i;
        }
        return posicion;
    }

    public boolean checkFields(){
        if(etName.getText().toString().isEmpty()){
            etName.setError(getString(R.string.required_name));
            return false;
        }else if(etAddress.getText().toString().isEmpty()){
            etAddress.setError(getString(R.string.required_address));
            return false;
        }else if(etPhone.getText().toString().isEmpty()){
            etPhone.setText(getString(R.string.required_number));
            return false;
        }else if(etPhone.length() < 10){
            etPhone.setText(getString(R.string.required_number_size));
            return false;
        }else if(etRFC.getText().toString().isEmpty()){
            etRFC.setError(getString(R.string.required_rfc));
            return false;
        }else if(etEmail.getText().toString().isEmpty()){
            etEmail.setError(getString(R.string.required_email));
            return false;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("section", R.id.nav_fiscal_data);
        startActivity(intent);
        finish();
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(message)
                .build();
        dialog.show();
    }
}
