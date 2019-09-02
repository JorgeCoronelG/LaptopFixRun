package com.laptopfix.laptopfixrun.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

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
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerController {

    private StringRequest request;
    private Context context;
    private VolleyListener mVolleyListener;

    public CustomerController(Context context) {
        this.context = context;
    }

    public void insert(final Customer customer, final String password){

        String url = Common.URL + CommunicationPath.CUSTOMER_INSERT;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){

                        setCustomer(customer);

                        mVolleyListener.onSuccess(CommunicationCode.CODE_CUSTOMER_INSERT);

                    }else if(jsonObject.getInt("code") == 404){
                        mVolleyListener.onFailure(jsonObject.getString("message"));
                    }
                }catch (Exception e){
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
                map.put("id", customer.getId());
                map.put("email", customer.getEmail());
                map.put("password", password);
                map.put("name", customer.getName());
                map.put("number", customer.getNumber());
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void update(final Customer customer){

        String url = Common.URL + CommunicationPath.CUSTOMER_UPDATE;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        JSONObject dataCustomer = jsonObject.getJSONObject("customer");
                        customer.setName(dataCustomer.getString("name"));
                        customer.setNumber(dataCustomer.getString("number"));

                        setCustomer(customer);

                        mVolleyListener.onSuccess(CommunicationCode.CODE_CUSTOMER_UPDATE);
                    }else if(jsonObject.getInt("code") == 404){
                        mVolleyListener.onFailure(jsonObject.getString("message"));
                    }
                }catch (Exception e){
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
                map.put("id", customer.getId());
                map.put("name", customer.getName());
                map.put("number", customer.getNumber());
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void setCustomer(Customer customer) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("id", customer.getId());
        editor.putString("name", customer.getName());
        editor.putString("number", customer.getNumber());
        editor.putString("email", customer.getEmail());
        editor.putInt("typeUser", Common.TYPE_USER_CUSTOMER);

        editor.commit();
    }

    public Customer getCustomer(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        Customer customer = new Customer();
        customer.setId(preferences.getString("id", null));
        customer.setName(preferences.getString("name", null));
        customer.setNumber(preferences.getString("number", null));
        customer.setEmail(preferences.getString("email", null));

        return customer;
    }

    public void setmVolleyListener(VolleyListener mVolleyListener) {
        this.mVolleyListener = mVolleyListener;
    }
}