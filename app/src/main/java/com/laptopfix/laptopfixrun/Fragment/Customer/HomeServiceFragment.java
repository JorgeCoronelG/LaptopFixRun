package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import java.util.Calendar;

public class HomeServiceFragment extends Fragment implements  View.OnFocusChangeListener, View.OnClickListener {

    private View view;
    private EditText etDate;
    private EditText etHour;
    private EditText etProblem;
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";
    private int dayOfWeek;
    private String hour;

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
        etDate.setOnFocusChangeListener(this);
        etHour.setOnFocusChangeListener(this);
        //Lineas para ocultar el teclado al dar click en el edit text
        etDate.setInputType(InputType.TYPE_NULL);
        etHour.setInputType(InputType.TYPE_NULL);

        etDate.setOnClickListener(this);
        etHour.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.menu_rEquipo));
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
        }
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
}
