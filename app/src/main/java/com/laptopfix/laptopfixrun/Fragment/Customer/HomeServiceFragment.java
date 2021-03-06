package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laptopfix.laptopfixrun.Activities.Customer.ChangeAddressActivity;
import com.laptopfix.laptopfixrun.Activities.Customer.HomeActivity;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.DateController;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.Model.FiscalData;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class HomeServiceFragment extends Fragment implements  View.OnFocusChangeListener, View.OnClickListener,
        VolleyListener {

    private View view;
    private EditText etDate, etHour, etProblem;
    private TextView txtAddress, txtChangeAddress, txtBill, txtNoteBill;
    private Spinner spnPay;
    private ArrayAdapter<String> adapter;
    private TextInputLayout tilDate, tilHour;
    private Switch swUrgent, swBill;
    private Button btnSave;
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";
    private int dayOfWeek;
    private String hour;
    private Geocoder geocoder;
    private List<Address> addresses;
    private AlertDialog dialog;
    private DateController dateController;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Variable para obtener el dia de la semana
    /*Se obtiene a partir de Domingo y los conviente en int
     * Domingo = 1
     * Lunes = 2 y así sucesivamente */
    private int diaSemana = c.get(Calendar.DAY_OF_WEEK);

    //Variables para obtener la hora hora
    private int hora = c.get(Calendar.HOUR_OF_DAY);
    private final int minuto = c.get(Calendar.MINUTE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        etDate = view.findViewById(R.id.etDate);
        etHour = view.findViewById(R.id.etHour);
        etProblem = view.findViewById(R.id.etProblem);
        swBill = view.findViewById(R.id.swBill);
        swUrgent = view.findViewById(R.id.swUrgent);
        spnPay = view.findViewById(R.id.spnPay);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, Constants.METHOD_PAY);
        spnPay.setAdapter(adapter);

        tilDate = view.findViewById(R.id.tilDate);
        tilHour  = view.findViewById(R.id.tilHour);
        btnSave = view.findViewById(R.id.btnSchedule);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtChangeAddress = view.findViewById(R.id.txtChangeAddress);
        txtChangeAddress.setText(Html.fromHtml(getResources().getString(R.string.changeAddress)));
        txtBill = view.findViewById(R.id.txtBill);
        txtBill.setText(getString(R.string.bill)+": NO");
        txtNoteBill = view.findViewById(R.id.txtNoteBill);
        txtNoteBill.setVisibility(View.GONE);

        database = FirebaseDatabase.getInstance();

        etDate.setOnFocusChangeListener(this);
        etHour.setOnFocusChangeListener(this);
        //Lineas para ocultar el teclado al dar click en el edit text
        etDate.setInputType(InputType.TYPE_NULL);
        etHour.setInputType(InputType.TYPE_NULL);

        dateController = new DateController(getContext());
        dateController.setmVolleyListener(this);

        etDate.setOnClickListener(this);
        etHour.setOnClickListener(this);
        txtChangeAddress.setOnClickListener(this);
        swUrgent.setOnClickListener(this);
        swBill.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.menu_rEquipo));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Constants.newAddress == null){
            txtAddress.setText(getDirection(
                    Constants.mLastLocation.getLatitude(),
                    Constants.mLastLocation.getLongitude())
            );
        }else{
            txtAddress.setText(getDirection(
                    Constants.newAddress.latitude,
                    Constants.newAddress.longitude));
        }
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
            case R.id.txtChangeAddress:
                ChangeAddressActivity changeAddressActivity = new ChangeAddressActivity();
                Intent intent = new Intent(getActivity(), changeAddressActivity.getClass());
                startActivity(intent);
                break;
            case R.id.swUrgent:
                if(swUrgent.isChecked()){
                    tilDate.setVisibility(View.GONE);
                    tilHour.setVisibility(View.GONE);
                }else{
                    tilDate.setVisibility(View.VISIBLE);
                    tilHour.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.swBill:
                if(swBill.isChecked()){
                    FiscalData fiscalData = new CustomerController(getContext()).getFiscalData();
                    if(fiscalData.getRfc() != null && fiscalData.getAddress() != null){
                        txtBill.setText(getString(R.string.bill)+": SI");
                        txtNoteBill.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(getContext(), "Debes tener tus datos fiscales completos", Toast.LENGTH_SHORT).show();
                        swBill.setChecked(false);
                    }
                }else {
                    txtBill.setText(getString(R.string.bill)+": NO");
                    txtNoteBill.setVisibility(View.GONE);
                }
                break;
            case R.id.btnSchedule:
                if(checkFields()){
                    createDialog(getString(R.string.waitAMoment));
                    dateController.insert(getDateHome());
                }
                break;
        }
    }

    public DateHome getDateHome(){
        DateHome dateHome = new DateHome();
        if(swUrgent.isChecked()) {
            dateHome.setService(1);
            dateHome.setHour("NA");
            dateHome.setDate("NA");
        }else{
            dateHome.setHour(hour);
            dateHome.setDate(etDate.getText().toString());
            dateHome.setService(0);
        }
        dateHome.setAddress(txtAddress.getText().toString());
        dateHome.setProblem(etProblem.getText().toString());
        dateHome.setCustomer(new CustomerController(getContext()).getCustomer());
        dateHome.setStatus(0);
        if(spnPay.getSelectedItem().equals("Efectivo")) dateHome.setPayment(1); else dateHome.setPayment(2);
        if(swBill.isChecked()) dateHome.setBill(1); else dateHome.setBill(0);
        return dateHome;
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
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDate.setText("");
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                etDate.setText(year + BARRA + (month + 1) + BARRA +  dayOfMonth);

            }

        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    private void obtenerHora(){
        final TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                etHour.setText("");
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);

                //Muestro la hora con el formato deseado
                etHour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " hrs.");
                hour = horaFormateada + DOS_PUNTOS + minutoFormateado;

            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto,false);

        recogerHora.show();
    }

    private String getDirection(double latitude, double longitude){
        try {
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses.get(0).getAddressLine(0);
    }

    @Override
    public void onSuccess(int code) {

    }

    @Override
    public void onSuccess(int code, Object object) {
        if(code == CommunicationCode.CODE_DATE_HOME_INSERT){
            final DateHome dateHome = (DateHome) object;
            reference = database.getReference(Constants.DATES_TABLE);
            reference.child(dateHome.getId()).setValue(dateHome)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            addDateHomeAtCustomer(dateHome);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onFailure(String error) {
        dialog.dismiss();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    public void addDateHomeAtCustomer(DateHome dateHome){
        reference = database.getReference(Constants.MATCH_DATES_TABLE);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dateHome.getId()).setValue(dateHome)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(getString(R.string.schedule_appointment));
                        builder.setMessage(getString(R.string.contact_with_technical_support));
                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                intent.putExtra("section", R.id.nav_cPendiente);
                                startActivity(intent);
                                getActivity().finish();
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
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean checkFields(){
        if(etProblem.getText().toString().isEmpty()){
            etProblem.setError(getString(R.string.problem_is_empty));
            return false;
        }else if(etDate.getText().toString().isEmpty() && !swUrgent.isChecked()){
            etDate.setError(getString(R.string.date_is_empty));
            return false;
        }else if(etHour.getText().toString().isEmpty() && !swUrgent.isChecked()){
            etHour.setError(getString(R.string.hour_is_empty));
            return false;
        }else{
            return true;
        }
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }
}