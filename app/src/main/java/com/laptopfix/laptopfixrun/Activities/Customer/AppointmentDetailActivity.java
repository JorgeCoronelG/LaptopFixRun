package com.laptopfix.laptopfixrun.Activities.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.DeliverController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.Model.Deliver;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import dmax.dialog.SpotsDialog;

public class AppointmentDetailActivity extends AppCompatActivity implements ValueEventListener, View.OnClickListener,
        VolleyListener {

    private TextView txtNameTechnical, txtPhoneTechnical, txtDate, txtHour, txtAddress, txtStatus, txtPay, txtBill;
    private Button btnCancelDate;
    private LinearLayout llTechnical;
    private LinearLayout llDateHour;
    private LinearLayout llUrgent;
    private DateHome dateHome;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AlertDialog dialog;
    private DeliverController deliverController;
    private Deliver deliver;
    private float baseService = 0;
    private int control = 0;
    private boolean bill;

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
        txtPay = findViewById(R.id.txtPayment);
        txtBill = findViewById(R.id.txtBill);
        btnCancelDate = findViewById(R.id.btnCancelDate);
        llTechnical = findViewById(R.id.llTechnical);
        llDateHour = findViewById(R.id.llDateHour);
        llUrgent = findViewById(R.id.llUrgent);
        database = FirebaseDatabase.getInstance();

        deliverController = new DeliverController(this);
        deliverController.setmVolleyListener(this);

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
        final DateHome dateHome = this.dateHome;
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(dateHome.getStatus() != 0){
                    deleteDateTechnical(dateHome);
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

    private void deleteDateTechnical(DateHome dateHome) {
        reference = database.getReference(Constants.MATCH_DATES_TABLE)
                .child(dateHome.getTechnical().getId())
                .child(dateHome.getId());
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
        if( (dateHome.getStatus() == 0 ||
                dateHome.getStatus() == 1) &&
                dateHome.getService() == 0){
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
                intent.putExtra("id", dateHome.getId());
                intent.putExtra("date", dateHome.getDate());
                intent.putExtra("hour", dateHome.getHour());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        dateHome = dataSnapshot.getValue(DateHome.class);
        if(dateHome != null){
            setData();
        }else{
            control++;
            if(control == 2){
                control = 0;
                dialog.dismiss();
                if(bill){
                    createDialog(getString(R.string.waitAMoment));
                    deliverController.insertBill(deliver, new CustomerController(this).getCustomer().getId());
                }else{
                    onBackPressed();
                }
            }
        }
    }

    private void setData() {
        control++;
        if(control == 2){
            control = 0;
            txtAddress.setText(dateHome.getAddress());
            if(dateHome.getPayment() == 1) txtPay.setText("Efectivo"); else txtPay.setText("PayPal");
            if(dateHome.getBill() == 1) txtBill.setText("Si"); else txtBill.setText("No");
            switch(dateHome.getStatus()){
                case Constants.STATUS_WAIT:
                    btnCancelDate.setVisibility(View.VISIBLE);
                    llTechnical.setVisibility(View.GONE);
                    break;
                case Constants.STATUS_ACCEPT:
                    btnCancelDate.setVisibility(View.VISIBLE);
                    llTechnical.setVisibility(View.VISIBLE);
                    txtNameTechnical.setText(dateHome.getTechnical().getName());
                    txtPhoneTechnical.setText(dateHome.getTechnical().getPhone());
                    txtStatus.setText("Aceptado");
                    break;
                case Constants.STATUS_IN_REPAIR:
                    btnCancelDate.setVisibility(View.GONE);
                    llTechnical.setVisibility(View.VISIBLE);
                    txtNameTechnical.setText(dateHome.getTechnical().getName());
                    txtPhoneTechnical.setText(dateHome.getTechnical().getPhone());
                    txtStatus.setText("En reparación");
                    break;
                case Constants.STATUS_REPAIRED:
                    btnCancelDate.setVisibility(View.GONE);
                    llTechnical.setVisibility(View.VISIBLE);
                    txtNameTechnical.setText(dateHome.getTechnical().getName());
                    txtPhoneTechnical.setText(dateHome.getTechnical().getPhone());
                    txtStatus.setText("Reparado y esperando total del servicio");
                    break;
                case Constants.STATUS_PAYMENT:
                    if(dateHome.getBill() == 1) bill = true; else bill = false;
                    if(dateHome.getPayment() == 1){
                        //Efectivo
                        createDialog(getString(R.string.waitAMoment));
                        deliverController.getBaseService();
                    }else{
                        //PayPal
                    }
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
        finish();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("section", R.id.nav_cPendiente);
        startActivity(intent);
    }

    @Override
    public void onSuccess(int code) {
        dialog.dismiss();
        switch(code){
            case CommunicationCode.CODE_INSERT_BILL:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onSuccess(int code, Object object) {
        switch (code){
            case CommunicationCode.CODE_DELIVER_GET:
                dialog.dismiss();
                this.deliver = (Deliver)object;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Pago");
                if(dateHome.getBill() == 1){
                    float total = Float.parseFloat(deliver.getCostDel());
                    total += baseService;
                    total *= 1.16;
                    builder.setMessage("Debe de pagar al técnico: $"+total);
                    deliver.setCostDel(String.valueOf(total));
                }else{
                    double total = Double.parseDouble(deliver.getCostDel());
                    total += baseService;
                    builder.setMessage("Debe de pagar al técnico: $"+total);
                    deliver.setCostDel(String.valueOf(total));
                }
                builder.setCancelable(false);
                dialog = builder.create();
                dialog.show();
                break;
            case CommunicationCode.CODE_GET_BASE_SERVICE:
                this.baseService += Float.valueOf((String)object);
                deliverController.get(dateHome);
                break;
        }
    }

    @Override
    public void onFailure(String error) {
        dialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
