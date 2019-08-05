package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Activities.Customer.EditProfileActivity;
import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.R;

import dmax.dialog.SpotsDialog;

public class ProfileFragment extends Fragment implements View.OnClickListener, UserController.VolleyListener {

    private TextView txtName, txtNumber, txtEmail;
    private EditText etNewPassword;
    private Button btnChangePass;
    private CustomerController customerController;
    private UserController userController;
    private AlertDialog dialog;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtNumber = view.findViewById(R.id.txtNumber);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnChangePass = view.findViewById(R.id.btnChangePassword);
        btnChangePass.setOnClickListener(this);
        customerController = new CustomerController(getContext());
        userController = new UserController(getContext());
        userController.setVolleyListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setDataCustomer();

        setHasOptionsMenu(true);

        return  view;
    }

    private void setDataCustomer(){
        Customer customer = customerController.getCustomer();

        txtName.setText(customer.getName());
        txtNumber.setText(customer.getNumber());
        txtEmail.setText(customer.getEmail());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.myProfile));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChangePassword:
                viewChangePassword();
                break;
        }
    }

    private void viewChangePassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.change_password, null);

        etNewPassword = view.findViewById(R.id.etChangePassword);

        builder.setView(view)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(checkPassword()){
                            createDialog(getString(R.string.waitAMoment));
                            changePassword();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel),null);
        builder.show();
    }

    private void changePassword(){
        final String newPassword = etNewPassword.getText().toString();
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Customer customer = customerController.getCustomer();
                            userController.changePassword(customer.getEmail(), newPassword);
                        }else{
                            dialog.dismiss();
                            Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkPassword(){
        if(etNewPassword.getText().toString().isEmpty()){
            Toast.makeText(getContext(), getString(R.string.required_password), Toast.LENGTH_SHORT).show();
            return false;
        }else if (etNewPassword.length() < 6){
            Toast.makeText(getContext(), getString(R.string.required_password_size), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_edit:
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("name", txtName.getText().toString());
                intent.putExtra("number", txtNumber.getText().toString());
                intent.putExtra("email", txtEmail.getText().toString());

                startActivity(intent);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(int code) {
        dialog.dismiss();
        if(code == CommunicationCode.CODE_CHANGE_PASSWORD){
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.txtPasswordSave));
            builder.setPositiveButton(getString(R.string.ok), null);
            builder.setCancelable(false);
            builder.show();
        }
    }

    @Override
    public void onFailure(String error) {
        dialog.dismiss();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }
}
