package com.laptopfix.laptopfixrun.Fragment.Technical;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.TechnicalController;
import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Technical;
import com.laptopfix.laptopfixrun.R;

import dmax.dialog.SpotsDialog;


public class ProfileFragment extends Fragment implements View.OnClickListener, VolleyListener {

    private View view;
    private TextView txtName, txtNumber, txtEmail;
    private EditText etNewPassword, etNewEmail;
    private Button btnChangePass;
    private ImageView editEmail;
    private TechnicalController technicalController;
    private UserController userController;
    private AlertDialog dialog;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_tech, container, false);
        txtName = view.findViewById(R.id.txtName);
        txtNumber = view.findViewById(R.id.txtNumber);
        txtEmail = view.findViewById(R.id.txtEmail);
        btnChangePass = view.findViewById(R.id.btnChangePassword);
        editEmail = view.findViewById(R.id.editEmail);
        btnChangePass.setOnClickListener(this);
        editEmail.setOnClickListener(this);
        technicalController = new TechnicalController(getContext());
        userController = new UserController(getContext());
        userController.setmVolleyListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setDataTechnical();

        return view;
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
            case R.id.editEmail:
                viewChangeEmail();
                break;
        }
    }

    private void viewChangeEmail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.change_email, null);

        etNewEmail = view.findViewById(R.id.etChangeEmail);

        builder.setView(view)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(checkEmail()){
                            createDialog(getString(R.string.waitAMoment));
                            changeEmail();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel),null);
        builder.show();
    }

    private void changeEmail(){
        final String newEmail = etNewEmail.getText().toString();
        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Technical technical = technicalController.getTechnical();
                    userController.changeEmail(technical.getEmail(), newEmail);
                }else{
                    dialog.dismiss();
                    Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                            Technical technical = technicalController.getTechnical();
                            userController.changePassword(technical.getEmail(), newPassword);
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

    private boolean checkEmail() {
        if(etNewEmail.getText().toString().isEmpty()){
            Toast.makeText(getContext(), getString(R.string.required_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(int code) {
        dialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        switch (code){
            case CommunicationCode.CODE_CHANGE_PASSWORD:
                builder.setMessage(getString(R.string.txtPasswordSave));
                builder.setPositiveButton(getString(R.string.ok), null);
                builder.setCancelable(false);
                builder.show();
                break;

            case CommunicationCode.CODE_CHANGE_EMAIL:
                builder.setMessage(getString(R.string.txtEmailSave));
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDataTechnical();
                    }
                });
                builder.setCancelable(false);
                builder.show();
                break;
        }
    }

    @Override
    public void onFailure(String error) {
        dialog.dismiss();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void setDataTechnical(){
        Technical technical = technicalController.getTechnical();

        txtName.setText(technical.getName());
        txtNumber.setText(technical.getPhone());
        txtEmail.setText(technical.getEmail());
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }

}