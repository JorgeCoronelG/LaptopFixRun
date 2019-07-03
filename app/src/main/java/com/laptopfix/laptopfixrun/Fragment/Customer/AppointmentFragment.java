package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laptopfix.laptopfixrun.Adapter.AdapterDatesCustomer;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Controller.DateController;
import com.laptopfix.laptopfixrun.Model.Date;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class AppointmentFragment extends Fragment implements DateController.VolleyListenerGetDates {

    private View view;
    private DateController dateController;
    private RecyclerView dateRecycler;
    private AdapterDatesCustomer adapterDatesCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment_customer, container, false);

        dateController = new DateController(getContext());
        dateController.setVolleyListenerGetDates(this);

        dateRecycler = view.findViewById(R.id.recyclerDates);

        dateController.getDatesCustomer(new CustomerController(getContext()).getCustomer());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.appoinment));
    }

    @Override
    public void requestFinished(ArrayList<Date> dates, int code) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dateRecycler.setLayoutManager(linearLayoutManager);
        adapterDatesCustomer = new AdapterDatesCustomer(dates, R.layout.item_dates_customer, getActivity());
        dateRecycler.setAdapter(adapterDatesCustomer);
    }
}
