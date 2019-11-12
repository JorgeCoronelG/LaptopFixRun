package com.laptopfix.laptopfixrun.Activities.Technical;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.DeliverController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.Model.Deliver;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class DeliverActivity extends AppCompatActivity implements VolleyListener, View.OnClickListener, ValueEventListener {

    private AlertDialog dialog;
    private String baseService;
    private EditText etDescribe;
    private EditText etCost;
    private TextView txtNote;
    private Button btnDeliver;
    private DeliverController deliverController;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DateHome dateHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);
        showToolbar(getString(R.string.deliverEquipment), true);

        etDescribe = findViewById(R.id.etDescribe);
        etCost = findViewById(R.id.etCost);
        txtNote = findViewById(R.id.txtNoteDeliver);
        btnDeliver = findViewById(R.id.btnDeliver);
        deliverController = new DeliverController(this);
        deliverController.setmVolleyListener(this);
        createDialog(getString(R.string.waitAMoment));
        deliverController.getBaseService();

        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        if(intent != null){
            reference = database.getReference(Constants.DATES_TECHNICAL_TABLE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(intent.getStringExtra("id"));
        }
        reference.addValueEventListener(this);
        btnDeliver.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CurrentServiceDetailActivity.class);
        intent.putExtra("id", dateHome.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccess(int code) {
        dialog.dismiss();
        switch (code){
            case CommunicationCode.CODE_DELIVER_INSERT:
                dialog.dismiss();
                double bs = Double.parseDouble(etCost.getText().toString());
                //Por programar
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Por último...");
                builder.setMessage("El cliente debe de pagarte $"+(Double.parseDouble(baseService)+bs));
                builder.setPositiveButton("Pagado", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDate();
                    }
                });
                builder.setCancelable(false);
                builder.show();
                break;
        }
    }

    private void deleteDate() {
        final DateHome dateHome = this.dateHome;
        //Borrar la cita por parte del técnico
        reference = database.getReference(Constants.DATES_TECHNICAL_TABLE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(dateHome.getId());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Borrar la cita por parte del cliente
                reference = database.getReference(Constants.MATCH_DATES_TABLE)
                        .child(dateHome.getCustomer().getId())
                        .child(dateHome.getId());
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(DeliverActivity.this, HomeActivity.class);
                        intent.putExtra("section", R.id.nav_citaL);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(DeliverActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(DeliverActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSuccess(int code, Object object) {
        dialog.dismiss();
        switch (code){
            case CommunicationCode.CODE_GET_BASE_SERVICE:
                baseService = (String)object;
                txtNote.setText(getString(R.string.noteDeliver)+" $"+baseService);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDeliver:
                if(checkFields()){
                    createDialog(getString(R.string.waitAMoment));
                    changeStatus(Constants.STATUS_REPAIRED);
                }
                break;
        }
    }

    private void changeStatus(final int status) {
        dateHome.setStatus(status);
        reference = database.getReference(Constants.DATES_TECHNICAL_TABLE)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(dateHome.getId());
        reference.setValue(dateHome).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                changeStatusCustomer();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(DeliverActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeStatusCustomer() {
        reference = database.getReference(Constants.MATCH_DATES_TABLE)
                .child(dateHome.getCustomer().getId())
                .child(dateHome.getId())
                .child("dateHome");
        reference.setValue(dateHome).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Deliver deliver = new Deliver();
                deliver.setDateHome(dateHome);
                deliver.setDescDel(etDescribe.getText().toString());
                deliver.setCostDel(etCost.getText().toString());
                String idTech = FirebaseAuth.getInstance().getCurrentUser().getUid();
                deliverController.insert(deliver, idTech, baseService);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(DeliverActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkFields(){
        if(etDescribe.getText().toString().isEmpty()){
            etDescribe.setError(getString(R.string.describe_required));
            return false;
        }else if(etCost.getText().toString().isEmpty()){
            etCost.setError(getString(R.string.cost_required));
            return false;
        }
        return true;
    }

    @Override
    public void onFailure(String error) {
        dialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        dateHome = dataSnapshot.getValue(DateHome.class);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showToolbar(String title, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(message)
                .build();
        dialog.show();
    }
}
