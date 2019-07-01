package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.DateController;
import com.laptopfix.laptopfixrun.HomeCustomerActivity;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.LoginActivity;
import com.laptopfix.laptopfixrun.Model.Date;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import java.util.Calendar;


public class GoPlaceFragment extends Fragment implements  View.OnFocusChangeListener, View.OnClickListener, DateController.VolleyListener {

    private View view;
    private EditText etDate;
    private EditText etHour;
    private EditText etProblem;
    private Button btnSchedule;
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";
    private String hour;
    private DateController dateController;
    private int dayOfWeek;

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
        view = inflater.inflate(R.layout.fragment_go_place, container, false);

        dateController = new DateController(getContext());
        dateController.setVolleyListener(this);

        etDate = view.findViewById(R.id.etDate);
        etHour = view.findViewById(R.id.etHour);
        etProblem = view.findViewById(R.id.etProblem);
        btnSchedule = view.findViewById(R.id.btnSchedule);
        //Lineas para ocultar el teclado al dar click en el edit text
        etDate.setInputType(InputType.TYPE_NULL);
        etHour.setInputType(InputType.TYPE_NULL);

        etDate.setOnFocusChangeListener(this);
        etHour.setOnFocusChangeListener(this);
        etDate.setOnClickListener(this);
        etHour.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.menu_iEstablecimiento));
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

                if(checkDate(dayOfWeek)){
                    etDate.setText(year + BARRA + (month + 1) + BARRA +  dayOfMonth);
                }
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
                if(checkHour(dayOfWeek, Integer.parseInt(horaFormateada))){
                    //Muestro la hora con el formato deseado
                    etHour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " hrs.");
                    hour = horaFormateada + DOS_PUNTOS + minutoFormateado;
                }
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto,false);

        recogerHora.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            switch (v.getId()){
                case R.id.etDate:
                    obtenerFecha();
                    break;
                case R.id.etHour:
                    if(!etDate.getText().toString().isEmpty()){
                        obtenerHora();
                    }else{
                        Toast.makeText(getContext(), getString(R.string.date_is_empty), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.etDate:
                obtenerFecha();
                break;
            case R.id.etHour:
                if(!etDate.getText().toString().isEmpty()){
                    obtenerHora();
                }else{
                    Toast.makeText(getContext(), getString(R.string.hour_is_selected_before_date), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSchedule:
                if(checkFields()){
                    dateController.insert(getDate());
                }
                break;
        }
    }

    private boolean checkFields(){
        if(etProblem.getText().toString().isEmpty()){
            etProblem.setError(getString(R.string.problem_is_empty));
            return false;
        }else if(etDate.getText().toString().isEmpty()){
            etDate.setError(getString(R.string.date_is_empty));
            return false;
        }else if(etHour.getText().toString().isEmpty()){
            etHour.setError(getString(R.string.hour_is_empty));
            return false;
        }
        return true;
    }

    private Date getDate(){
        Date date = new Date();
        date.setCustomer(new CustomerController(getContext()).getCustomer());
        date.setDate(etDate.getText().toString());
        date.setHour(hour);
        date.setResidenceCus("NA");
        date.setDesProblem(etProblem.getText().toString());
        return date;
    }

    private boolean checkDate(int date){
        if(date == Common.SUNDAY){
            Toast.makeText(getContext(), "No se labora los domingos", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkHour(int date, int hour){
        if(date == Common.SATURDAY){
            if(hour >= Common.WORKING_SATURDAY_START_LF && hour < Common.WORKING_SATURDAY_FINISH_LF){
                return true;
            }else{
                Toast.makeText(getContext(), "El horario del sábado es de 10 a 14 hrs.", Toast.LENGTH_LONG).show();
                return false;
            }
        }else if(hour >= Common.WORKING_ALL_WEEK_START_LF && hour < Common.WORKING_ALL_WEEK_FINISH_LF){
            return true;
        }else{
            Toast.makeText(getContext(), "El horario entre semana es de 9 a 19 hrs.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void requestFinished(int code) {
        if(code == CommunicationCode.CODE_DATE_INSERT){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getString(R.string.schedule_appointment));
            builder.setMessage(getString(R.string.contact_with_technical_support));
            builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), HomeCustomerActivity.class);
                    intent.putExtra("section", R.id.nav_establecimiento);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }
}