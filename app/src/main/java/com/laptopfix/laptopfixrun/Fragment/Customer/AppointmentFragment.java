package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Adapter.DatesCustomerAdapter;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.DateController;
import com.laptopfix.laptopfixrun.Interface.VolleyListenerGetDates;
import com.laptopfix.laptopfixrun.Model.DateLF;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class AppointmentFragment extends Fragment implements VolleyListenerGetDates {

    private View view;
    private DateController dateController;
    private RecyclerView dateRecycler;
    private DatesCustomerAdapter datesCustomerAdapter;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment_customer, container, false);

        dateController = new DateController(getContext());
        dateController.setmVolleyListenerGetDates(this);

        dateRecycler = view.findViewById(R.id.recyclerDates);

        //createDialog(getString(R.string.waitAMoment));

        //dateController.getDatesCustomer(new CustomerController(getContext()).getCustomer());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.appoinment));
    }

    @Override
    public void onSuccess(ArrayList<DateLF> dates, int code) {
        dialog.dismiss();
        if(code == CommunicationCode.CODE_GET_DATES_CUSTOMER){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            dateRecycler.setLayoutManager(linearLayoutManager);
            datesCustomerAdapter = new DatesCustomerAdapter(dates, R.layout.item_dates_customer, getActivity());
            dateRecycler.setAdapter(datesCustomerAdapter);
        }
    }

    @Override
    public void onFailure(String message) {
        dialog.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }
}
