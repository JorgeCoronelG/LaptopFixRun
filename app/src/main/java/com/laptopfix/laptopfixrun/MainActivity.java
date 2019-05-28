package com.laptopfix.laptopfixrun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Fragment.Perfil;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;
import com.laptopfix.laptopfixrun.Util.Common;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Perfil.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private TextView txtName, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToolbar(getString(R.string.establishment), false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_establecimiento);

        setDataUser(navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setDataUser(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);

        txtName = headerView.findViewById(R.id.txtUser);
        txtPhone = headerView.findViewById(R.id.txtPhone);

        CustomerController customerController = new CustomerController(this);
        Customer customer = customerController.getCustomer();

        txtName.setText(customer.getName());
        txtPhone.setText(customer.getNumber());
    }

    private void showToolbar(String title, boolean upButton) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment miFragment =null;
        boolean fragmentSeleccionado = false;

        if (id == R.id.nav_establecimiento) {
            // Handle the camera action
        } else if (id == R.id.nav_perfil) {
            miFragment = new Perfil();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_rEquipo) {

        } else if (id == R.id.nav_iEstablecimiento) {

        } else if (id == R.id.nav_comentario) {

        } else if (id == R.id.nav_closeSession) {
            CustomerController customerController = new CustomerController(this);
            customerController.setCustomer(new Customer(0, null, null, new User()));

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if(fragmentSeleccionado == true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}