package com.laptopfix.laptopfixrun.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Fragment.Customer.GoPlaceFragment;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.Date;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DateController {

    private StringRequest request;
    private Context context;
    private AlertDialog dialog;
    private VolleyListener mVolleyListener;
    private VolleyListenerGetDates mVolleyListenerGetDates;

    public DateController(Context context) {
        this.context = context;
    }

    public void insert(final Date date){
        createDialog(context.getString(R.string.waitAMoment));

        String url = Common.URL + CommunicationPath.DATE_INSERT;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        dialog.dismiss();
                        if(mVolleyListener != null){
                            mVolleyListener.requestFinished(CommunicationCode.CODE_DATE_INSERT);
                        }
                    }else{
                        dialog.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("idCus", String.valueOf(date.getCustomer().getIdCus()));
                map.put("date", date.getDate());
                map.put("hour", date.getHour());
                map.put("residence", date.getResidenceCus());
                map.put("problem", date.getDesProblem());
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void getDatesLaptopFix(){
        createDialog(context.getString(R.string.loading_dates));

        String url = Common.URL + CommunicationPath.GET_DATES_LAPTOP_FIX;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        ArrayList<Date> dates = new ArrayList<>();
                        JSONArray array = jsonObject.getJSONArray("dates");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject data = array.getJSONObject(i);

                            Date date = new Date();
                            date.setIdDate(data.getInt("id"));
                            date.setDate(context.getString(R.string.date_appointment) +" "+ data.getString("date"));
                            date.setHour(context.getString(R.string.hour_appointment) +" "+ data.getString("hour"));
                            date.setResidenceCus(data.getString("residence"));

                            Customer customer = new Customer();
                            customer.setName(data.getString("customer"));
                            date.setCustomer(customer);

                            dates.add(date);
                        }

                        dialog.dismiss();
                        if(mVolleyListenerGetDates != null){
                            mVolleyListenerGetDates.requestFinished(dates, CommunicationCode.CODE_GET_DATES_LAPTOP_FIX);
                        }
                    }else{
                        dialog.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void getDatesCustomer(final Customer customer){
        createDialog(context.getString(R.string.loading_dates));

        String url = Common.URL + CommunicationPath.GET_DATES_CUSTOMER;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        ArrayList<Date> dates = new ArrayList<>();
                        JSONArray array = jsonObject.getJSONArray("dates");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject data = array.getJSONObject(i);

                            Date date = new Date();
                            date.setDate(context.getString(R.string.date_appointment) +" "+ data.getString("date"));
                            date.setHour(context.getString(R.string.hour_appointment) +" "+ data.getString("hour"));
                            date.setResidenceCus(data.getString("residence"));
                            date.setDesProblem(context.getString(R.string.problem) +" "+ data.getString("problem"));

                            dates.add(date);
                        }

                        dialog.dismiss();
                        if(mVolleyListenerGetDates != null){
                            mVolleyListenerGetDates.requestFinished(dates, CommunicationCode.CODE_GET_DATES_CUSTOMER);
                        }
                    }else{
                        dialog.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("id", String.valueOf(customer.getIdCus()));
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(message)
                .build();
        dialog.show();
    }

    public interface VolleyListener {
        void requestFinished(int code);
    }

    public void setVolleyListener(VolleyListener volleyListener){
        this.mVolleyListener = volleyListener;
    }

    public interface VolleyListenerGetDates{
        void requestFinished(ArrayList<Date> dates, int code);
    }

    public void setVolleyListenerGetDates(VolleyListenerGetDates volleyListener){
        this.mVolleyListenerGetDates = volleyListener;
    }
}