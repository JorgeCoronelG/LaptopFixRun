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
import com.laptopfix.laptopfixrun.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerController implements Response.Listener<String>, Response.ErrorListener {

    private StringRequest request;
    private Context context;
    private VolleyListener mVolleyListener;
    private Customer customer;

    public CustomerController(Context context) {
        this.context = context;
    }

    public void insert(final Customer customer, final String password){
        this.customer = customer;
        String url = Constants.URL + CommunicationPath.CUSTOMER_INSERT;
        request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("id", customer.getId());
                map.put("email", customer.getEmail());
                map.put("password", password);
                map.put("name", customer.getName());
                map.put("phone", customer.getPhone());
                return map;
            }
        };
        Communication.getmInstance(context).addToRequestQueue(request);
    }

    public void update(final Customer customer){
        this.customer = customer;
        String url = Constants.URL + CommunicationPath.CUSTOMER_UPDATE;
        request = new StringRequest(Request.Method.POST, url, this, this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("id", customer.getId());
                map.put("name", customer.getName());
                map.put("phone", customer.getPhone());
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
        editor.putString("number", customer.getPhone());
        editor.putString("email", customer.getEmail());
        editor.putInt("typeUser", Constants.TYPE_USER_CUSTOMER);

        editor.commit();
    }

    public Customer getCustomer(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        Customer customer = new Customer();
        customer.setId(preferences.getString("id", null));
        customer.setName(preferences.getString("name", null));
        customer.setPhone(preferences.getString("number", null));
        customer.setEmail(preferences.getString("email", null));

        return customer;
    }

    public void setmVolleyListener(VolleyListener mVolleyListener) {
        this.mVolleyListener = mVolleyListener;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (jsonObject.getInt("code")){
                case CommunicationCode.CODE_CUSTOMER_INSERT:
                    setCustomer(customer);
                    mVolleyListener.onSuccess(CommunicationCode.CODE_CUSTOMER_INSERT);
                    break;

                case CommunicationCode.CODE_CUSTOMER_UPDATE:
                    setCustomer(customer);
                    mVolleyListener.onSuccess(CommunicationCode.CODE_CUSTOMER_UPDATE);
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