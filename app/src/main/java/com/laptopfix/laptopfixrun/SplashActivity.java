package com.laptopfix.laptopfixrun;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.laptopfix.laptopfixrun.Activities.Customer.HomeCustomerActivity;
import com.laptopfix.laptopfixrun.Activities.Tecnhical.HomeTechnicalActivity;
import com.laptopfix.laptopfixrun.Activities.LaptopFix.HomeLFActivity;
import com.laptopfix.laptopfixrun.Activities.LoginActivity;
import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Util.Common;

public class SplashActivity extends Activity {

    private static final int DURATION_SPLASH = 2000; //2 seg
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        userController = new UserController(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int typeUser = userController.checkUser();
                if(typeUser == 0){
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else if (typeUser == Common.TYPE_USER_LAPTOP_FIX){
                    Intent intent = new Intent(SplashActivity.this, HomeLFActivity.class);
                    startActivity(intent);
                    finish();
                }else if (typeUser == Common.TYPE_USER_CUSTOMER){
                    Intent intent = new Intent(SplashActivity.this, HomeCustomerActivity.class);
                    intent.putExtra("section", R.id.nav_establecimiento);
                    startActivity(intent);
                    finish();
                }else if (typeUser == Common.TYPE_USER_TECHNICAL){
                    Intent intent = new Intent(SplashActivity.this, HomeTechnicalActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, DURATION_SPLASH);
    }
}
