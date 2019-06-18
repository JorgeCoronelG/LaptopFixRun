package com.laptopfix.laptopfixrun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Fragment.ComentFragment;
import com.laptopfix.laptopfixrun.Fragment.GoPlaceFragment;
import com.laptopfix.laptopfixrun.Fragment.HomeServiceFragment;
import com.laptopfix.laptopfixrun.Fragment.PlaceFragment;
import com.laptopfix.laptopfixrun.Fragment.ProfileFragment;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;

public class HomeCustomerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TextView txtName, txtPhone;
    private CustomerController customerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_customer);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        customerController = new CustomerController(this);
        setDataCustomer(navigationView);

        Intent intent = getIntent();
        if(intent != null){
            navigationView.setCheckedItem(intent.getIntExtra("section", 0));
            displaySelectedScreen(intent.getIntExtra("section", 0));
        }
    }

    private void setDataCustomer(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);

        txtName = headerView.findViewById(R.id.txtUser);
        txtPhone = headerView.findViewById(R.id.txtPhone);

        Customer customer = customerController.getCustomer();

        txtName.setText(customer.getName());
        txtPhone.setText(customer.getNumber());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId){
        Fragment fragment = null;

        switch (itemId){
            case R.id.nav_establecimiento:
                fragment = new PlaceFragment();
                break;
            case R.id.nav_perfil:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_chat:
                break;
            case R.id.nav_iEstablecimiento:
                fragment = new GoPlaceFragment();
                break;
            case R.id.nav_rEquipo:
                fragment = new HomeServiceFragment();
                break;
            case R.id.nav_comentario:
                fragment = new ComentFragment();
                break;
            case R.id.nav_closeSession:
                customerController.setCustomer(new Customer(0, null, null, new User()));

                Intent intent = new Intent(HomeCustomerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}