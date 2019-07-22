package com.laptopfix.laptopfixrun.Fragment.LaptopFix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Adapter.DatesLFAdapter;
import com.laptopfix.laptopfixrun.Controller.DateController;
import com.laptopfix.laptopfixrun.Model.Date;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class AppointmentFragment extends Fragment implements DateController.VolleyListenerGetDates, DatesLFAdapter.OnDateListener {

    private View view;
    private DateController dateController;
    private RecyclerView dateRecycler;
    private DatesLFAdapter datesLFAdapter;
    private ArrayList<Date> dates = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment_lf, container, false);

        dateController = new DateController(getContext());
        dateController.setVolleyListenerGetDates(this);

        dateRecycler = view.findViewById(R.id.recyclerDates);

        dateController.getDatesLaptopFix();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.pending_appointment));
    }

    @Override
    public void requestFinished(ArrayList<Date> dates, int code) {
        this.dates = dates;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dateRecycler.setLayoutManager(linearLayoutManager);
        datesLFAdapter = new DatesLFAdapter(this.dates, R.layout.item_dates_lf, getActivity(), this);
        dateRecycler.setAdapter(datesLFAdapter);
    }

    @Override
    public void onDateClick(int position) {
        Date date = dates.get(position);
        Log.d("JCG", ""+date.getIdDate());
    }
}
