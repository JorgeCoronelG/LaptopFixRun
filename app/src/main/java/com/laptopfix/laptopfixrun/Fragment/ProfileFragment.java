package com.laptopfix.laptopfixrun.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.EditProfileActivity;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.R;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView txtName, txtNumber, txtEmail;
    Button btnEditar;
    CustomerController customerController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtNumber = view.findViewById(R.id.txtNumber);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnEditar = view.findViewById(R.id.btnEdit);
        btnEditar.setOnClickListener(this);
        customerController = new CustomerController(getActivity());

        setDataCustomer();

        return  view;
    }

    private void setDataCustomer(){
        Customer customer = customerController.getCustomer();

        txtName.setText(customer.getName());
        txtNumber.setText(customer.getNumber());
        txtEmail.setText(customer.getUser().getEmail());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.myProfile));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEdit:
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("name", txtName.getText().toString());
                intent.putExtra("number", txtNumber.getText().toString());
                intent.putExtra("email", txtEmail.getText().toString());

                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
