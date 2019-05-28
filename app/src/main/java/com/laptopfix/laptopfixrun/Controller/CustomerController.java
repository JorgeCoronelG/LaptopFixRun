package com.laptopfix.laptopfixrun.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CustomerController {

    private StringRequest request;
    private Context context;
    private AlertDialog dialog;

    public CustomerController(Context context) {
        this.context = context;
    }

    public void insert(final Customer customer){
        createDialog(context.getString(R.string.waitAMoment));

        final VolleyListener volleyListener = (VolleyListener)context;

        String url = Common.URL + CommunicationPath.CUSTOMER_INSERT;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        JSONObject dataCustomer = jsonObject.getJSONObject("customer");
                        customer.setIdCus(dataCustomer.getInt("id"));

                        setCustomer(customer);

                        dialog.dismiss();

                        volleyListener.requestFinished(context.getString(R.string.insertCustomer), true);
                    }else if(jsonObject.getInt("code") == 404){
                        dialog.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
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
                map.put("email", customer.getUser().getEmail());
                map.put("password", customer.getUser().getPassword());
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

        editor.putInt("id", customer.getIdCus());
        editor.putString("name", customer.getName());
        editor.putString("number", customer.getNumber());
        editor.putString("email", customer.getUser().getEmail());

        editor.commit();
    }

    public Customer getCustomer(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Customer customer = new Customer();
        customer.setIdCus(preferences.getInt("id", 0));
        customer.setName(preferences.getString("name", null));
        customer.setNumber(preferences.getString("number", null));

        User user = new User();
        user.setEmail(preferences.getString("email", null));

        customer.setUser(user);

        return customer;
    }

    public boolean checkCustomer(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = preferences.getString("name",null);
        if(name == null){
            return false;
        }else{
            return true;
        }
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(message)
                .build();
        dialog.show();
    }
}