package com.laptopfix.laptopfixrun.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.laptopfix.laptopfixrun.R;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.internal.Util;


public class GoPlaceFragment extends Fragment implements  View.OnFocusChangeListener, View.OnClickListener {


    View view;
    EditText date;
    EditText hour;
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

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
    //final int diaSemana = c.get(Calendar.DAY_OF_WEEK);

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_go_place, container, false);


        date = (EditText) view.findViewById(R.id.etDate);
        hour = (EditText) view.findViewById(R.id.etHour);
        date.setOnFocusChangeListener(this);
        hour.setOnFocusChangeListener(this);
        //Lineas para ocultar el teclado al dar click en el edit text
        date.setInputType(InputType.TYPE_NULL);
        hour.setInputType(InputType.TYPE_NULL);

        date.setOnClickListener(this);
        hour.setOnClickListener(this);


        return  view;
    }


   private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

               int  day = c.get(Calendar.DAY_OF_WEEK);




                //Muestro la fecha con el formato deseado*/
                date.setText(year + BARRA + (month+1) + BARRA +  dayOfMonth + BARRA + day);

            }

        },anio, mes, dia );
        //Muestro el widget
        recogerFecha.show();

    }


    private void obtenerHora(){
        final TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);

                //Muestro la hora con el formato deseado
                hour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " hrs." );
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
                    obtenerHora();
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
                obtenerHora();
                break;
        }
    }

}
