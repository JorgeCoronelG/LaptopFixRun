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

public class UserController {

    private StringRequest request;
    private Context context;
    private AlertDialog dialog;

    public UserController(Context context) {
        this.context = context;
        dialog = new SpotsDialog.Builder()
                .setContext(context)
                .build();
    }

    public void login(final User user){
        createDialog(context.getString(R.string.waitAMoment));

        final VolleyListener volleyListener = (VolleyListener)context;

        String url = Common.URL + CommunicationPath.LOGIN;

        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("code") == 200){
                        JSONObject dataUser = jsonObject.getJSONObject("user");

                        if(dataUser.getInt("typeUser") == Common.TYPE_USER_LAPTOP_FIX){

                            user.setEmail(dataUser.getString("email"));
                            user.setPassword(dataUser.getString("password"));
                            user.setIdTypeUser(dataUser.getInt("typeUser"));
                            user.setStatus(dataUser.getInt("status"));

                            setUser(user);

                            volleyListener.requestFinished(context.getString(R.string.login_laptopfix));
                        }else if(dataUser.getInt("typeUser") == Common.TYPE_USER_CUSTOMER){
                            Customer customer = new Customer();
                            customer.setIdCus(dataUser.getInt("id"));
                            customer.setName(dataUser.getString("name"));
                            customer.setNumber(dataUser.getString("number"));
                            customer.setUser(new User());
                            customer.getUser().setEmail(dataUser.getString("email"));
                            customer.getUser().setPassword(dataUser.getString("password"));
                            customer.getUser().setIdTypeUser(dataUser.getInt("typeUser"));
                            customer.getUser().setStatus(dataUser.getInt("status"));

                            CustomerController customerController = new CustomerController(context);
                            customerController.setCustomer(customer);

                            volleyListener.requestFinished(context.getString(R.string.login_customer));
                        }
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
    }

    public void setUser(User user){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.putInt("status", user.getStatus());
        editor.putInt("typeUser", user.getIdTypeUser());

        editor.commit();
    }

    public User getUser(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        User user = new User();
        user.setEmail(preferences.getString("email", null));
        user.setPassword(preferences.getString("password", null));
        user.setStatus(preferences.getInt("status", 0));
        user.setIdTypeUser(preferences.getInt("typeUser", 0));

        return user;
    }

    public int checkUser(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        int typeUser = preferences.getInt("typeUser",0);
        return typeUser;
    }

    public void createDialog(String message){
        dialog.setMessage(message);
        dialog.show();
    }

    public AlertDialog getDialog() {
        return dialog;
    }
}