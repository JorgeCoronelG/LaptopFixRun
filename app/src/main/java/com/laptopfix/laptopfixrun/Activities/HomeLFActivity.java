package com.laptopfix.laptopfixrun.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Fragment.LaptopFix.AppointmentFragment;
import com.laptopfix.laptopfixrun.Fragment.ChatFragment;
import com.laptopfix.laptopfixrun.Fragment.LaptopFix.CommentFragment;
import com.laptopfix.laptopfixrun.Model.User;
import com.laptopfix.laptopfixrun.R;

public class HomeLFActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private Toolbar toolbar;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lf);

        userController = new UserController(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        mMainNav.setOnNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_cita);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            userController.setUser(new User());
            Intent intent = new Intent(HomeLFActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
