package com.laptopfix.laptopfixrun.Controller;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.DateLF;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DateController {

    private StringRequest request;
    private Context context;
    private VolleyListener mVolleyListener;
    private VolleyListenerGetDates mVolleyListenerGetDates;

    public DateController(Context context) {
        this.context = context;
    }

    public void insert(final DateLF date){
        String url = Common.URL + CommunicationPath.DATE_INSERT;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        if(mVolleyListener != null){
                            mVolleyListener.onSuccess(CommunicationCode.CODE_DATE_INSERT);
                        }
                    }else{
                        mVolleyListener.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    mVolleyListener.onFailure(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mVolleyListener.onFailure(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("idCus", String.valueOf(date.getCustomer().getId()));
                map.put("date", date.getDate());
                map.put("hour", date.getHour());
                map.put("problem", date.getDesProblem());
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void getDatesLaptopFix(){
        String url = Common.URL + CommunicationPath.GET_DATES_LAPTOP_FIX;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        ArrayList<DateLF> dates = new ArrayList<>();
                        JSONArray array = jsonObject.getJSONArray("dates");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject data = array.getJSONObject(i);

                            DateLF date = new DateLF();
                            date.setId(data.getInt("id"));
                            date.setDate(context.getString(R.string.date_appointment) +" "+ data.getString("date"));
                            date.setHour(context.getString(R.string.hour_appointment) +" "+ data.getString("hour"));

                            Customer customer = new Customer();
                            customer.setName(data.getString("customer"));
                            date.setCustomer(customer);

                            dates.add(date);
                        }
                        if(mVolleyListenerGetDates != null){
                            mVolleyListenerGetDates.onSuccess(dates, CommunicationCode.CODE_GET_DATES_LAPTOP_FIX);
                        }
                    }else{
                        mVolleyListenerGetDates.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    mVolleyListenerGetDates.onFailure(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mVolleyListenerGetDates.onFailure(error.toString());
            }
        });

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void getDatesCustomer(final Customer customer){
        String url = Common.URL + CommunicationPath.GET_DATES_CUSTOMER;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        ArrayList<DateLF> dates = new ArrayList<>();
                        JSONArray array = jsonObject.getJSONArray("dates");
                        for(int i = 0; i < array.length(); i++){
                            JSONObject data = array.getJSONObject(i);

                            DateLF date = new DateLF();
                            date.setDate(context.getString(R.string.date_appointment) +" "+ data.getString("date"));
                            date.setHour(context.getString(R.string.hour_appointment) +" "+ data.getString("hour"));
                            date.setDesProblem(context.getString(R.string.problem) +" "+ data.getString("problem"));

                            dates.add(date);
                        }
                        if(mVolleyListenerGetDates != null){
                            mVolleyListenerGetDates.onSuccess(dates, CommunicationCode.CODE_GET_DATES_CUSTOMER);
                        }
                    }else{
                        mVolleyListenerGetDates.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    mVolleyListenerGetDates.onFailure(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mVolleyListenerGetDates.onFailure(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("id", String.valueOf(customer.getId()));
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public interface VolleyListener {
        void onSuccess(int code);
        void onFailure(String message);
    }

    public void setVolleyListener(VolleyListener volleyListener){
        this.mVolleyListener = volleyListener;
    }

    public interface VolleyListenerGetDates{
        void onSuccess(ArrayList<DateLF> dates, int code);
        void onFailure(String message);
    }

    public void setVolleyListenerGetDates(VolleyListenerGetDates volleyListener){
        this.mVolleyListenerGetDates = volleyListener;
    }
}