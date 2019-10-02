package com.laptopfix.laptopfixrun.Activities.Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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
import com.laptopfix.laptopfixrun.Util.Common;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class UpdateDateActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ValueEventListener {

    private EditText etDate;
    private EditText etHour;
    private Button btnUpdate;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private AlertDialog dialog;
    private MatchDate matchDate;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";
    private String hour;
    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private int hora = c.get(Calendar.HOUR_OF_DAY);
    private final int minuto = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_date);
        showToolbar(getString(R.string.update), true);

        etDate = findViewById(R.id.etDate);
        etHour = findViewById(R.id.etHour);
        btnUpdate = findViewById(R.id.btnUpdate);
        database = FirebaseDatabase.getInstance();

        etDate.setInputType(InputType.TYPE_NULL);
        etHour.setInputType(InputType.TYPE_NULL);

        etDate.setOnFocusChangeListener(this);
        etHour.setOnFocusChangeListener(this);
        etDate.setOnClickListener(this);
        etHour.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent != null){
            etDate.setText(intent.getStringExtra("date"));
            etHour.setText(intent.getStringExtra("hour")+" hrs.");
            hour = intent.getStringExtra("hour");
            reference = database.getReference(Common.MATCH_DATES_TABLE)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(intent.getStringExtra("id"));
        }

        reference.addValueEventListener(this);
        btnUpdate.setOnClickListener(this);
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
            case R.id.etDate:
                obtenerFecha();
                break;
            case R.id.etHour:
                obtenerHora();
                break;
            case R.id.btnUpdate:
                if(matchDate != null){
                    createDialog(getString(R.string.waitAMoment));
                    matchDate.getDateHome().setDate(etDate.getText().toString());
                    matchDate.getDateHome().setHour(hour);
                    updateDateCustomer();
                }
                break;
        }
    }

    private void updateDateCustomer() {
        reference.setValue(matchDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(matchDate.getDateHome().getStatus() != 0){
                    updateDateTechnical();
                }else{
                    dialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDateActivity.this);
                    builder.setMessage(getString(R.string.txtSave));
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
                Toast.makeText(UpdateDateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDateTechnical() {
        reference = database.getReference(Common.DATES_TECHNICAL_TABLE)
                .child(matchDate.getTechnical().getId())
                .child(matchDate.getDateHome().getId());
        reference.setValue(matchDate.getDateHome())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDateActivity.this);
                        builder.setMessage(getString(R.string.txtSave));
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
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
                        Toast.makeText(UpdateDateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        matchDate = dataSnapshot.getValue(MatchDate.class);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.etDate:
                    obtenerFecha();
                    break;
                case R.id.etHour:
                    obtenerHora();
                    break;
            }
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDate.setText("");
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                etDate.setText(year + BARRA + (month + 1) + BARRA +  dayOfMonth);

            }

        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    private void obtenerHora(){
        final TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etHour.setText("");
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);

                etHour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " hrs.");
                hour = horaFormateada + DOS_PUNTOS + minutoFormateado;

            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto,false);

        recogerHora.show();
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(message)
                .build();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
