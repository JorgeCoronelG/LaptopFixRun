package com.laptopfix.laptopfixrun.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.LaptopFix;
import com.laptopfix.laptopfixrun.Model.Technical;
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserController {

    private StringRequest request;
    private Context context;
    private VolleyListener volleyListener;

    public UserController(Context context) {
        this.context = context;
    }

    public void login(final String email, final String password){
        final com.laptopfix.laptopfixrun.Interface.VolleyListener volleyListener = (com.laptopfix.laptopfixrun.Interface.VolleyListener) context;
        String url = Common.URL + CommunicationPath.LOGIN;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        JSONObject dataUser = jsonObject.getJSONObject("user");

                        if(dataUser.getInt("typeUser") == Common.TYPE_USER_LAPTOP_FIX){

                            volleyListener.onSuccess(CommunicationCode.CODE_LOGIN_LAPTOP_FIX);

                        }else if(dataUser.getInt("typeUser") == Common.TYPE_USER_CUSTOMER){

                            Customer customer = new Customer();
                            customer.setId(dataUser.getString("id"));
                            customer.setName(dataUser.getString("name"));
                            customer.setNumber(dataUser.getString("number"));
                            customer.setEmail(dataUser.getString("email"));
                            new CustomerController(context).setCustomer(customer);
                            volleyListener.onSuccess(CommunicationCode.CODE_LOGIN_CUSTOMER);

                        }else if(dataUser.getInt("typeUser") == Common.TYPE_USER_TECHNICAL){

                            Technical technical = new Technical();
                            technical.setId(dataUser.getString("id"));
                            technical.setName(dataUser.getString("name"));
                            technical.setPhone(dataUser.getString("number"));
                            technical.setEmail(dataUser.getString("email"));
                            new TechnicalController(context).setTechnical(technical);
                            volleyListener.onSuccess(CommunicationCode.CODE_LOGIN_TECHNICAL);

                        }
                    }else if(jsonObject.getInt("code") == 404){
                        volleyListener.onFailure(jsonObject.getString("message"));
                    }
                }catch (Exception e){
                    volleyListener.onFailure(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyListener.onFailure(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void changePassword(final String email, final String password){
        String url = Common.URL + CommunicationPath.CHANGE_PASSWORD;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        volleyListener.onSuccess(CommunicationCode.CODE_CHANGE_PASSWORD);
                    }else if(jsonObject.getInt("code") == 404){
                        volleyListener.onFailure(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    volleyListener.onFailure(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyListener.onFailure(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public interface VolleyListener{
        void onSuccess(int code);
        void onFailure(String error);
    }

    public void setVolleyListener(VolleyListener volleyListener){
        this.volleyListener = volleyListener;
    }

    public int checkUser(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        int typeUser = preferences.getInt("typeUser",0);
        return typeUser;
    }

    public void logout(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("typeUser", 0);
        editor.commit();
    }
}