package com.laptopfix.laptopfixrun.Activities.Technical;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.laptopfix.laptopfixrun.Activities.Customer.EditProfileActivity;
import com.laptopfix.laptopfixrun.Controller.TechnicalController;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.Model.MatchDate;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import dmax.dialog.SpotsDialog;

public class AppointmentDetailActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    private TextView txtName, txtDate, txtHour, txtAddress, txtProblem;
    private Button btnAccept;
    private LinearLayout llUrgent, llDateHour;
    private DateHome dateHome;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        showToolbar(getString(R.string.detailDate), true);

        txtName = findViewById(R.id.txtNameCustomer);
        txtDate = findViewById(R.id.txtDate);
        txtHour = findViewById(R.id.txtHour);
        txtAddress = findViewById(R.id.txtAddress);
        txtProblem = findViewById(R.id.txtProblem);
        llDateHour = findViewById(R.id.llDateHour);
        llUrgent = findViewById(R.id.llUrgent);
        btnAccept = findViewById(R.id.btnAcceptDate);
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if(intent != null){
            reference = database.getReference(Common.DATES_TABLE).child(intent.getStringExtra("id"));
        }
        reference.addValueEventListener(this);
        btnAccept.setOnClickListener(this);
    }

    private void showToolbar(String title, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAcceptDate:
                dateHome.setStatus(1);//Aceptado
                createDialog(getString(R.string.waitAMoment));
                addMatchDate();
                break;
        }
    }

    private void addMatchDate() {
        MatchDate matchDate = new MatchDate(dateHome, new TechnicalController(this).getTechnical());
        reference = database.getReference(Common.MATCH_DATES_TABLE);
        reference.child(dateHome.getCustomer().getId()).child(dateHome.getId()).setValue(matchDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addDateTechnical();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(AppointmentDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDateTechnical() {
        reference = database.getReference(Common.DATES_TECHNICAL_TABLE);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dateHome.getId()).setValue(dateHome)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentDetailActivity.this);
                        builder.setTitle(getString(R.string.accept_service));
                        builder.setMessage(getString(R.string.txt_accept_service));
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Por programar
                                deleteDate();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(AppointmentDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteDate() {
        reference = database.getReference(Common.DATES_TABLE).child(dateHome.getId());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AppointmentDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        dateHome = dataSnapshot.getValue(DateHome.class);
        if(dateHome != null){
            setDateData();
        }
    }

    private void setDateData() {
        txtName.setText(dateHome.getCustomer().getName());
        txtAddress.setText(dateHome.getAddress());
        txtProblem.setText(dateHome.getProblem());
        if(dateHome.getService() == 0){//Significa que no es urgente
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

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(message)
                .build();
        dialog.show();
    }
}
