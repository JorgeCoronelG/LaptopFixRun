package com.laptopfix.laptopfixrun.Activities.Technical;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

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
            reference = database.getReference(Common.DATES_TECHNICAL_TABLE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(intent.getStringExtra("id"));
        }
        reference.addValueEventListener(this);
        btnRepair.setOnClickListener(this);
        btnDeliver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRepair:
                break;
            case R.id.btnDeliver:
                break;
        }
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
                txtPhoneCustomer.setText(dateHome.getCustomer().getNumber());
                txtStatus.setText("Aceptado");
                btnDeliver.setVisibility(View.GONE);
                break;
            case 2:
                txtNameCustomer.setText(dateHome.getCustomer().getName());
                txtPhoneCustomer.setText(dateHome.getCustomer().getNumber());
                txtStatus.setText("En reparación");
                btnRepair.setVisibility(View.GONE);
                break;
            case 3:
                txtNameCustomer.setText(dateHome.getCustomer().getName());
                txtPhoneCustomer.setText(dateHome.getCustomer().getNumber());
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

}
