package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Activities.Customer.EditFiscalDataActivity;
import com.laptopfix.laptopfixrun.Activities.Customer.EditProfileActivity;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.FiscalDataController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.FiscalData;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import dmax.dialog.SpotsDialog;

public class FiscalDataFragment extends Fragment {

    private TextView txtName, txtAddress, txtPhone, txtRFC, txtCFDI, txtEmail;
    private AlertDialog dialog;
    private CustomerController customerController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fiscal_data, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtRFC = view.findViewById(R.id.txtRFC);
        txtCFDI = view.findViewById(R.id.txtCFDI);
        txtEmail = view.findViewById(R.id.txtEmail);

        customerController = new CustomerController(getContext());
        setFiscalData();
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.fiscal_data));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setFiscalData(){
        FiscalData fiscalData = customerController.getFiscalData();
        txtName.setText(fiscalData.getName());
        if(fiscalData.getAddress() == null) txtAddress.setText("***"); else txtAddress.setText(fiscalData.getAddress());
        txtPhone.setText(fiscalData.getPhone());
        if(fiscalData.getRfc() == null) txtRFC.setText("***"); else txtRFC.setText(fiscalData.getRfc());
        txtCFDI.setText(fiscalData.getCfdi());
        txtEmail.setText(fiscalData.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_edit:
                Intent intent = new Intent(getActivity(), EditFiscalDataActivity.class);
                intent.putExtra("name", txtName.getText().toString());

                if(txtAddress.getText().equals("***"))
                    intent.putExtra("address", "");
                else
                    intent.putExtra("address", txtAddress.getText().toString());

                intent.putExtra("phone", txtPhone.getText().toString());

                if(txtRFC.getText().equals("***"))
                    intent.putExtra("rfc", "");
                else
                    intent.putExtra("rfc", txtRFC.getText().toString());

                if(txtAddress.getText().equals("***"))
                    intent.putExtra("address", "");
                else
                    intent.putExtra("address", txtAddress.getText().toString());

                intent.putExtra("cfdi", txtCFDI.getText().toString());
                intent.putExtra("email", txtEmail.getText().toString());

                startActivity(intent);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }
}