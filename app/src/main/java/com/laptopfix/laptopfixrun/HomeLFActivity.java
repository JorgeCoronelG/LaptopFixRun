package com.laptopfix.laptopfixrun;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.laptopfix.laptopfixrun.Fragment.LaptopFix.AppointmentFragment;
import com.laptopfix.laptopfixrun.Fragment.LaptopFix.ChatFragment;
import com.laptopfix.laptopfixrun.Fragment.Customer.ComentFragment;

public class HomeLFActivity extends AppCompatActivity{

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private AppointmentFragment appointmentFragment;
    private ChatFragment chatFragment;
    private ComentFragment comentFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lf);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        appointmentFragment = new AppointmentFragment();
        chatFragment = new ChatFragment();
        comentFragment = new ComentFragment();

        setFragment(appointmentFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_cita:
                        //mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(appointmentFragment);
                        return true;

                    case R.id.nav_chat:
                        //mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(chatFragment);
                        return true;

                    case R.id.nav_comentario:
                        //mMainNav.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(comentFragment);
                        return true;

                        default:
                            return false;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }




}
