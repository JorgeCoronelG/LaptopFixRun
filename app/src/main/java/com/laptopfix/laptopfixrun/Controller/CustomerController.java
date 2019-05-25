package com.laptopfix.laptopfixrun.Controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONException;
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
        createDialog(String.valueOf(R.string.waitAMoment));

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
                        Common.currentCustomer = customer;

                        dialog.dismiss();

                        volleyListener.requestFinished(String.valueOf(R.string.insertCustomer), true);
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

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(message)
                .build();
        dialog.show();
    }
}