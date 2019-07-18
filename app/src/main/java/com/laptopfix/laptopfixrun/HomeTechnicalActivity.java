package com.laptopfix.laptopfixrun;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.laptopfix.laptopfixrun.Fragment.ChatFragment;
import com.laptopfix.laptopfixrun.Fragment.LaptopFix.AppointmentFragment;
import com.laptopfix.laptopfixrun.Fragment.LaptopFix.CommentFragment;

public class HomeTechnicalActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_technical);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void displaySelectedScreen(int itemId){
        Fragment fragment = null;
        switch (itemId){
            case R.id.nav_cita:
                fragment = new AppointmentFragment();
                break;
            case R.id.nav_chat:
                fragment = new ChatFragment();
                break;
            case R.id.nav_comentario:
                fragment = new CommentFragment();
                break;
        }
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).commit();
        }
    }
}
