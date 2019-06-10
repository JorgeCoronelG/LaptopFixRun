package com.laptopfix.laptopfixrun.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.laptopfix.laptopfixrun.R;

import java.util.Calendar;
public class GoPlaceFragment extends Fragment implements View.OnClickListener {

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

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_go_place, container, false);

        date = view.findViewById(R.id.etDate);
        hour = view.findViewById(R.id.etHour);
        date.setOnClickListener(this);
        hour.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.menu_iEstablecimiento));
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

   private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                date.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            }
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
   }

   private void obtenerHora(){
       TimePickerDialog recogerHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
           @Override
           public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
               //Formateo el hora obtenido: antepone el 0 si son menores de 10
               String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
               //Formateo el minuto obtenido: antepone el 0 si son menores de 10
               String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
               //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
               String AM_PM;
               if(hourOfDay < 12) {
                   AM_PM = "a.m.";
               } else {
                   AM_PM = "p.m.";
               }
               //Muestro la hora con el formato deseado
               hour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
           }
           //Estos valores deben ir en ese orden
           //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
           //Pero el sistema devuelve la hora en formato 24 horas
       }, hora, minuto, false);

       recogerHora.show();
   }
}
