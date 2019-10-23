package com.laptopfix.laptopfixrun.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.toolbox.StringRequest;
import com.laptopfix.laptopfixrun.Model.LaptopFix;
import com.laptopfix.laptopfixrun.Util.Constants;

public class LaptopFixController {

    private StringRequest request;
    private Context context;

    public LaptopFixController(Context context) {
        this.context = context;
    }

    public void setLaptopFix(LaptopFix laptopFix){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("id", laptopFix.getId());
        editor.putString("email", laptopFix.getEmail());
        editor.putInt("typeUser", Constants.TYPE_USER_LAPTOP_FIX);

        editor.commit();
    }

    public LaptopFix getLaptopFix(){
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        LaptopFix laptopFix = new LaptopFix();
        laptopFix.setId(preferences.getString("id", null));
        laptopFix.setEmail(preferences.getString("email", null));

        return laptopFix;
    }

}
