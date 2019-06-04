package com.laptopfix.laptopfixrun;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;

import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Util.Common;

public class SplashActivity extends Activity {

    private static final int DURATION_SPLASH = 2000; //2 seg

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserController userController = new UserController(SplashActivity.this);
                if(userController.checkUser() == 0){
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else if(userController.checkUser() == Common.TYPE_USER_LAPTOP_FIX){
                    //Por programar
                }else if(userController.checkUser() == Common.TYPE_USER_CUSTOMER){
                    Intent intent = new Intent(SplashActivity.this, HomeCustomer.class);
                    intent.putExtra("section", R.id.nav_establecimiento);
                    startActivity(intent);
                    finish();
                }
            }
        }, DURATION_SPLASH);
    }
}
