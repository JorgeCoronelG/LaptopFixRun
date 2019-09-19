package com.laptopfix.laptopfixrun.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Model.Technical;
import com.laptopfix.laptopfixrun.Util.Common;

public class TechnicalController {

    private StringRequest request;
    private Context context;

    public TechnicalController(Context context) {
        this.context = context;
    }

    public void setTechnical(Technical technical){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("id", technical.getId());
        editor.putString("name", technical.getName());
        editor.putString("phone", technical.getPhone());
        editor.putString("email", technical.getEmail());
        editor.putInt("typeUser", Common.TYPE_USER_TECHNICAL);

        editor.commit();
    }

    public Technical getTechnical(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        Technical technical = new Technical();
        technical.setId(preferences.getString("id", null));
        technical.setName(preferences.getString("name", null));
        technical.setPhone(preferences.getString("phone", null));
        technical.setEmail(preferences.getString("email", null));

        return technical;
    }

}