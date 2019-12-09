package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FiscalDataFragment extends Fragment implements View.OnClickListener, VolleyListener {

    private EditText etName, etAddress, etPhone, etRFC, etEmail;
    private Spinner spnCDFI;
    private Button btnSave;
    private AlertDialog dialog;
    private FiscalDataController fiscalDataController;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fiscal_data, container, false);

        etName = view.findViewById(R.id.etName);
        etAddress = view.findViewById(R.id.etAddress);
        etPhone = view.findViewById(R.id.etPhone);
        etRFC = view.findViewById(R.id.etRFC);
        etEmail = view.findViewById(R.id.etEmail);
        spnCDFI = view.findViewById(R.id.spnCDFI);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        fiscalDataController = new FiscalDataController(getContext());
        fiscalDataController.setmVolleyListener(this);
        fiscalDataController.get(new CustomerController(getContext()).getCustomer().getId());

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Constants.CFDI);

        createDialog(getString(R.string.waitAMoment));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.fiscal_data));
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
        FiscalData fiscalData = new FiscalData();
        fiscalData.setCustomer(new CustomerController(getContext()).getCustomer().getId());
        fiscalData.setName(etName.getText().toString());
        fiscalData.setAddress(etAddress.getText().toString());
        fiscalData.setPhone(etPhone.getText().toString());
        fiscalData.setRfc(etRFC.getText().toString());
        fiscalData.setCfdi(spnCDFI.getSelectedItem().toString());
        fiscalData.setEmail(etEmail.getText().toString());
        fiscalDataController.update(fiscalData);
    }

    @Override
    public void onSuccess(int code) {
        dialog.dismiss();
        switch (code){
            case CommunicationCode.CODE_UPDATE_FISCAL_DATA:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.txtSave));
                builder.setPositiveButton(R.string.ok, null);
                builder.show();
                break;
        }
    }

    @Override
    public void onSuccess(int code, Object object) {
        dialog.dismiss();
        switch (code){
            case CommunicationCode.CODE_GET_FISCAL_DATA:
                FiscalData fiscalData = (FiscalData)object;
                etName.setText(fiscalData.getName());
                etAddress.setText(fiscalData.getAddress());
                etPhone.setText(fiscalData.getPhone());
                etRFC.setText(fiscalData.getRfc());
                etEmail.setText(fiscalData.getEmail());
                spnCDFI.setAdapter(adapter);
                spnCDFI.setSelection(obtenerPosicionItem(spnCDFI, fiscalData.getCfdi()));
                break;
        }
    }

    @Override
    public void onFailure(String error) {
        dialog.dismiss();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }
}