package com.laptopfix.laptopfixrun.Activities.Customer;

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
import com.laptopfix.laptopfixrun.Model.MatchDate;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import dmax.dialog.SpotsDialog;

public class AppointmentDetailActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener {

    private TextView txtNameTechnical;
    private TextView txtPhoneTechnical;
    private TextView txtDate;
    private TextView txtHour;
    private TextView txtAddress;
    private TextView txtStatus;
    private Button btnCancelDate;
    private LinearLayout llTechnical;
    private LinearLayout llDateHour;
    private LinearLayout llUrgent;
    private MatchDate matchDate;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail_customer);
        showToolbar(getString(R.string.detailDate), true);

        txtNameTechnical = findViewById(R.id.txtNameTechnical);
        txtPhoneTechnical = findViewById(R.id.txtPhoneTechnical);
        txtDate = findViewById(R.id.txtDate);
        txtHour = findViewById(R.id.txtHour);
        txtAddress = findViewById(R.id.txtAddress);
        txtStatus = findViewById(R.id.txtStatus);
        btnCancelDate = findViewById(R.id.btnCancelDate);
        llTechnical = findViewById(R.id.llTechnical);
        llDateHour = findViewById(R.id.llDateHour);
        llUrgent = findViewById(R.id.llUrgent);
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if(intent != null){
            reference = database.getReference(Constants.MATCH_DATES_TABLE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(intent.getStringExtra("id"));
        }
        reference.addValueEventListener(this);
        btnCancelDate.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reference.addValueEventListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCancelDate:
                createDialog(getString(R.string.waitAMoment));
                deleteDateCustomer();
                break;
        }
    }

    private void deleteDateCustomer() {
        final MatchDate matchDate = this.matchDate;
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(matchDate.getDateHome().getStatus() != 0){
                    deleteDateTechnical(matchDate);
                }else{
                    dialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentDetailActivity.this);
                    builder.setMessage(getString(R.string.txt_delete_date));
                    builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(AppointmentDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDateTechnical(MatchDate matchDate) {
        reference = database.getReference(Constants.DATES_TECHNICAL_TABLE)
                .child(matchDate.getTechnical().getId())
                .child(matchDate.getDateHome().getId());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentDetailActivity.this);
                builder.setMessage(getString(R.string.txt_delete_date));
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
                Toast.makeText(AppointmentDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if( (matchDate.getDateHome().getStatus() == 0 ||
                matchDate.getDateHome().getStatus() == 1) &&
                matchDate.getDateHome().getService() == 0){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_edit, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_edit:
                Intent intent = new Intent(this, UpdateDateActivity.class);
                intent.putExtra("id", matchDate.getDateHome().getId());
                intent.putExtra("date", matchDate.getDateHome().getDate());
                intent.putExtra("hour", matchDate.getDateHome().getHour());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        matchDate = dataSnapshot.getValue(MatchDate.class);
        if(matchDate != null){
            setData();
        }
    }

    private void setData() {
        txtAddress.setText(matchDate.getDateHome().getAddress());
        switch(matchDate.getDateHome().getStatus()){
            case 0:
                btnCancelDate.setVisibility(View.VISIBLE);
                llTechnical.setVisibility(View.GONE);
                break;
            case 1:
                btnCancelDate.setVisibility(View.VISIBLE);
                txtNameTechnical.setText(matchDate.getTechnical().getName());
                txtPhoneTechnical.setText(matchDate.getTechnical().getPhone());
                txtStatus.setText("Aceptado");
                break;
            case 2:
                btnCancelDate.setVisibility(View.GONE);
                txtNameTechnical.setText(matchDate.getTechnical().getName());
                txtPhoneTechnical.setText(matchDate.getTechnical().getPhone());
                txtStatus.setText("En reparación");
                break;
            case 3:
                btnCancelDate.setVisibility(View.GONE);
                txtNameTechnical.setText(matchDate.getTechnical().getName());
                txtPhoneTechnical.setText(matchDate.getTechnical().getPhone());
                txtStatus.setText("Reparado");
                break;
        }
        if(matchDate.getDateHome().getService() == 0){
            llUrgent.setVisibility(View.GONE);
            txtDate.setText(matchDate.getDateHome().getDate());
            txtHour.setText(matchDate.getDateHome().getHour());
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
