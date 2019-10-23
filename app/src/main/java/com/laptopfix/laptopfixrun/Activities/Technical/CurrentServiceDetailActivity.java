package com.laptopfix.laptopfixrun.Activities.Technical;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import dmax.dialog.SpotsDialog;

public class CurrentServiceDetailActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

    private TextView txtNameCustomer;
    private TextView txtPhoneCustomer;
    private TextView txtDate;
    private TextView txtHour;
    private TextView txtAddress;
    private TextView txtStatus;
    private Button btnRepair;
    private Button btnDeliver;
    private LinearLayout llDateHour;
    private LinearLayout llUrgent;
    private DateHome dateHome;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_service_tech);
        showToolbar(getString(R.string.detailDate), true);

        txtNameCustomer = findViewById(R.id.txtNameCustomer);
        txtPhoneCustomer = findViewById(R.id.txtPhoneCustomer);
        txtDate = findViewById(R.id.txtDate);
        txtHour = findViewById(R.id.txtHour);
        txtAddress = findViewById(R.id.txtAddress);
        txtStatus = findViewById(R.id.txtStatus);
        btnRepair = findViewById(R.id.btnRepair);
        btnDeliver = findViewById(R.id.btnDeliver);
        llDateHour = findViewById(R.id.llDateHour);
        llUrgent = findViewById(R.id.llUrgent);

        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if(intent != null){
            reference = database.getReference(Constants.DATES_TECHNICAL_TABLE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(intent.getStringExtra("id"));
        }
        reference.addValueEventListener(this);
        btnRepair.setOnClickListener(this);
        btnDeliver.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(dateHome.getStatus() == Constants.STATUS_ACCEPT){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_cancel, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_cancel:
                createDialog(getString(R.string.waitAMoment));
                cancelDate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelDate() {
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
                        dateHome.setStatus(0);
                        addDate(dateHome);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(CurrentServiceDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(CurrentServiceDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDate(DateHome dateHome) {
        reference = database.getReference(Constants.DATES_TABLE).child(dateHome.getId());
        reference.setValue(dateHome).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentServiceDetailActivity.this);
                builder.setMessage(getString(R.string.txt_cancel_date));
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(CurrentServiceDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRepair:
                createDialog(getString(R.string.waitAMoment));
                changeStatus(Constants.STATUS_IN_REPAIR);
                break;
            case R.id.btnDeliver:
                createDialog(getString(R.string.waitAMoment));
                changeStatus(Constants.STATUS_REPAIRED);
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
                changeStatusCustomer(status);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(CurrentServiceDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeStatusCustomer(int status) {
        reference = database.getReference(Constants.MATCH_DATES_TABLE)
                .child(dateHome.getCustomer().getId())
                .child(dateHome.getId())
                .child("dateHome");
        reference.setValue(dateHome).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(CurrentServiceDetailActivity.this);
                builder.setMessage(getString(R.string.txt_success_process));
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(CurrentServiceDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        dateHome = dataSnapshot.getValue(DateHome.class);
        if(dateHome != null){
            setData();
        }
    }

    private void setData() {
        txtAddress.setText(dateHome.getAddress());
        switch(dateHome.getStatus()){
            case 1:
                txtNameCustomer.setText(dateHome.getCustomer().getName());
                txtPhoneCustomer.setText(dateHome.getCustomer().getPhone());
                txtStatus.setText("Aceptado");
                btnRepair.setVisibility(View.VISIBLE);
                btnDeliver.setVisibility(View.GONE);
                break;
            case 2:
                txtNameCustomer.setText(dateHome.getCustomer().getName());
                txtPhoneCustomer.setText(dateHome.getCustomer().getPhone());
                txtStatus.setText("En reparación");
                btnRepair.setVisibility(View.GONE);
                btnDeliver.setVisibility(View.VISIBLE);
                break;
            case 3:
                txtNameCustomer.setText(dateHome.getCustomer().getName());
                txtPhoneCustomer.setText(dateHome.getCustomer().getPhone());
                txtStatus.setText("Reparado");
                btnDeliver.setVisibility(View.GONE);
                btnRepair.setVisibility(View.GONE);
                break;
        }
        if(dateHome.getService() == 0){
            llUrgent.setVisibility(View.GONE);
            txtDate.setText(dateHome.getDate());
            txtHour.setText(dateHome.getHour());
        }else{
            llDateHour.setVisibility(View.GONE);
        }
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

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(message)
                .build();
        dialog.show();
    }

}
