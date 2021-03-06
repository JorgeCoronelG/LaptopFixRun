package com.laptopfix.laptopfixrun.Activities.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.laptopfix.laptopfixrun.Activities.LoginActivity;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.UserController;
import com.laptopfix.laptopfixrun.Fragment.Customer.AppointmentFragment;
import com.laptopfix.laptopfixrun.Fragment.Customer.FiscalDataFragment;
import com.laptopfix.laptopfixrun.Fragment.Customer.GoPlaceFragment;
import com.laptopfix.laptopfixrun.Fragment.Customer.HomeServiceFragment;
import com.laptopfix.laptopfixrun.Fragment.Customer.PlaceFragment;
import com.laptopfix.laptopfixrun.Fragment.Customer.ProfileFragment;
import com.laptopfix.laptopfixrun.Interface.VolleyListener;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.FiscalData;
import com.laptopfix.laptopfixrun.R;

public class HomeActivity extends AppCompatActivity
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
        txtPhone.setText(customer.getPhone());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

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
            /*case R.id.nav_iEstablecimiento:
                fragment = new GoPlaceFragment();
                break;*/
            case R.id.nav_rEquipo:
                fragment = new HomeServiceFragment();
                break;
            case R.id.nav_cPendiente:
                fragment = new AppointmentFragment();
                break;
            case R.id.nav_fiscal_data:
                fragment = new FiscalDataFragment();
                break;
            /*case R.id.nav_chat:
                //fragment = new ChatSoporteFragment();
                //fragment = new ChatCusFragment();
                break;*/
            case R.id.nav_closeSession:
                customerController.setCustomer(new Customer());
                customerController.setFiscalData(new FiscalData());
                new UserController(this).logout();
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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