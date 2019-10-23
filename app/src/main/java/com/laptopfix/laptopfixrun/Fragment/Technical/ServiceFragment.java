package com.laptopfix.laptopfixrun.Fragment.Technical;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laptopfix.laptopfixrun.Activities.Technical.AppointmentDetailActivity;
import com.laptopfix.laptopfixrun.Adapter.DatesTechnicalAdapter;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


public class ServiceFragment extends Fragment implements ValueEventListener, DatesTechnicalAdapter.OnDateListener {

    private View view;
    private RecyclerView dateRecycler;
    private DatesTechnicalAdapter datesTechnicalAdapter;
    private AlertDialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<DateHome> dates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_service_tech, container, false);

        dateRecycler = view.findViewById(R.id.recyclerDates);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(Constants.DATES_TABLE);
        reference.addValueEventListener(this);

        createDialog(getString(R.string.waitAMoment));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.nav_serviciosTec));
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        dates = new ArrayList<DateHome>();
        for(DataSnapshot data : dataSnapshot.getChildren()){
            DateHome date = data.getValue(DateHome.class);
            dates.add(date);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dateRecycler.setLayoutManager(linearLayoutManager);
        datesTechnicalAdapter = new DatesTechnicalAdapter(dates, R.layout.item_dates, getActivity(), this);
        dateRecycler.setAdapter(datesTechnicalAdapter);
        dialog.dismiss();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        dialog.dismiss();
        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateClick(int position) {
        Intent intent = new Intent(getActivity(), AppointmentDetailActivity.class);
        intent.putExtra("id", String.valueOf(dates.get(position).getId()));
        startActivity(intent);
    }
}
