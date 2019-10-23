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
import com.laptopfix.laptopfixrun.Model.Technical;
import com.laptopfix.laptopfixrun.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserController implements Response.Listener<String>, Response.ErrorListener {

    private StringRequest request;
    private Context context;
    private VolleyListener mVolleyListener;

    public UserController(Context context) {
        this.context = context;
    }

    public void login(final String email, final String password){
        String url = Constants.URL + CommunicationPath.LOGIN;
        request = new StringRequest(Request.Method.POST, url, this, this){
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
        String url = Constants.URL + CommunicationPath.CHANGE_PASSWORD;
        request = new StringRequest(Request.Method.POST, url, this, this){
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

    public void setmVolleyListener(VolleyListener mVolleyListener) {
        this.mVolleyListener = mVolleyListener;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch(jsonObject.getInt("code")){
                case CommunicationCode.CODE_LOGIN_LAPTOP_FIX:
                    mVolleyListener.onSuccess(CommunicationCode.CODE_LOGIN_LAPTOP_FIX);
                    break;

                case CommunicationCode.CODE_LOGIN_CUSTOMER:
                    JSONObject dataCustomer = jsonObject.getJSONObject("user");
                    Customer customer = new Customer();
                    customer.setId(dataCustomer.getString("id"));
                    customer.setName(dataCustomer.getString("name"));
                    customer.setPhone(dataCustomer.getString("phone"));
                    customer.setEmail(dataCustomer.getString("email"));
                    new CustomerController(context).setCustomer(customer);
                    mVolleyListener.onSuccess(CommunicationCode.CODE_LOGIN_CUSTOMER);
                    break;

                case CommunicationCode.CODE_LOGIN_TECHNICAL:
                    JSONObject dataTechnical = jsonObject.getJSONObject("user");
                    Technical technical = new Technical();
                    technical.setId(dataTechnical.getString("id"));
                    technical.setName(dataTechnical.getString("name"));
                    technical.setPhone(dataTechnical.getString("number"));
                    technical.setEmail(dataTechnical.getString("email"));
                    new TechnicalController(context).setTechnical(technical);
                    mVolleyListener.onSuccess(CommunicationCode.CODE_LOGIN_TECHNICAL);
                    break;

                case CommunicationCode.CODE_CHANGE_PASSWORD:
                    mVolleyListener.onSuccess(CommunicationCode.CODE_CHANGE_PASSWORD);
                    break;

                case CommunicationCode.CODE_ERROR:
                    mVolleyListener.onFailure(jsonObject.getString("message"));
                    break;
            }
        } catch (JSONException e) {
            mVolleyListener.onFailure(e.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mVolleyListener.onFailure(error.toString());
    }
}