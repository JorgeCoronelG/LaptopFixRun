package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laptopfix.laptopfixrun.Activities.Customer.AppointmentDetailActivity;
import com.laptopfix.laptopfixrun.Adapter.DatesCustomerAdapter;
import com.laptopfix.laptopfixrun.Controller.DateController;
import com.laptopfix.laptopfixrun.Interface.VolleyListenerGetDates;
import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.Model.DateLF;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class AppointmentFragment extends Fragment implements VolleyListenerGetDates,
        ValueEventListener, DatesCustomerAdapter.OnDateListener {

    private View view;
    private DateController dateController;
    private RecyclerView dateRecycler;
    private DatesCustomerAdapter datesCustomerAdapter;
    private AlertDialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<DateHome> dates;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment_customer, container, false);

        dateController = new DateController(getActivity());
        dateController.setmVolleyListenerGetDates(this);

        dateRecycler = view.findViewById(R.id.recyclerDates);

        createDialog(getString(R.string.waitAMoment));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(Constants.MATCH_DATES_TABLE).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(this);

        //dateController.getDatesCustomer(new CustomerController(getContext()).getCustomer());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.appoinment));
    }

    @Override
    public void onResume() {
        super.onResume();
        reference.addValueEventListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onSuccess(ArrayList<DateLF> dates, int code) {
        /*dialog.dismiss();
        if(code == CommunicationCode.CODE_GET_DATES_CUSTOMER){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            dateRecycler.setLayoutManager(linearLayoutManager);
            datesCustomerAdapter = new DatesCustomerAdapter(dates, R.layout.item_dates_customer, getActivity());
            dateRecycler.setAdapter(datesCustomerAdapter);
        }*/
    }

    @Override
    public void onFailure(String message) {
        dialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getActivity())
                .setMessage(message)
                .build();
        dialog.show();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        dates = new ArrayList<DateHome>();
        for(DataSnapshot data : dataSnapshot.getChildren()){
            DateHome dateHome = data.getValue(DateHome.class);
            dates.add(dateHome);
        }
        if(!dates.isEmpty()){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            dateRecycler.setLayoutManager(linearLayoutManager);
            datesCustomerAdapter = new DatesCustomerAdapter(dates, R.layout.item_dates_customer, getActivity(), this);
            dateRecycler.setAdapter(datesCustomerAdapter);
            dialog.dismiss();
        }else{
            dialog.dismiss();
            Toast.makeText(context, "No tienes citas pendientes", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        dialog.dismiss();
        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateClick(int position) {
        getActivity().finish();
        Intent intent = new Intent(getActivity(), AppointmentDetailActivity.class);
        intent.putExtra("id", dates.get(position).getId());
        startActivity(intent);
    }
}
