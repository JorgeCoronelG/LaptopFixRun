package com.laptopfix.laptopfixrun.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Communication.Communication;
import com.laptopfix.laptopfixrun.Communication.CommunicationPath;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class UserController {

    private StringRequest request;
    private Context context;
    private AlertDialog dialog;
    //Para verificar si los métodos son exitosos
    private boolean result;

    public UserController(Context context) {
        this.context = context;
    }

    public boolean login(final User user){
        createDialog(String.valueOf(R.string.waitAMoment));
        result = false;

        String url = Common.URL + CommunicationPath.LOGIN;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        JSONObject dataCustomer = jsonObject.getJSONObject("customer");

                        User user = new User();
                        user.setEmail(dataCustomer.getString("email"));

                        Customer customer = new Customer();
                        customer.setIdCus(dataCustomer.getInt("id"));
                        customer.setName(dataCustomer.getString("name"));
                        customer.setNumber(dataCustomer.getString("number"));
                        Common.currentCustomer = customer;

                        dialog.dismiss();
                        result = false;
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
                map.put("email", user.getEmail());
                map.put("password", user.getPassword());
                return map;
            }
        };

        Communication.getmInstance(context).addToRequestQueue(request);
        return result;
    }

    public void createDialog(String meesage){
        dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(meesage)
                .build();
        dialog.show();
    }
}