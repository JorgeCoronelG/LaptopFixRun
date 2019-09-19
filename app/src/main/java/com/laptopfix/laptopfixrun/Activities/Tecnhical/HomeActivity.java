package com.laptopfix.laptopfixrun.Activities.Tecnhical;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.laptopfix.laptopfixrun.Activities.LoginActivity;
import com.laptopfix.laptopfixrun.Controller.TechnicalController;
import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Fragment.Technical.NowServiceFragment;
import com.laptopfix.laptopfixrun.Fragment.Technical.ProfileFragment;
import com.laptopfix.laptopfixrun.Fragment.Technical.ServiceFragment;
import com.laptopfix.laptopfixrun.Model.Technical;
import com.laptopfix.laptopfixrun.R;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_technical);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userController = new UserController(this);

        bottomNavigationView = findViewById(R.id.main_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_cita);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId){
        Fragment fragment = null;
        switch (itemId){
            case R.id.nav_cita:
                fragment = new ServiceFragment();
                break;
            case R.id.nav_dates_now:
                fragment = new NowServiceFragment();
                break;
            case R.id.nav_perfil:
                fragment = new ProfileFragment();
                break;
        }
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            new TechnicalController(this).setTechnical(new Technical());
            userController.logout();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
