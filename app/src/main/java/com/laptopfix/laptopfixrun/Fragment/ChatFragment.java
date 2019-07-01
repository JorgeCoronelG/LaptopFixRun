package com.laptopfix.laptopfixrun.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.laptopfix.laptopfixrun.Adapter.CustomerAdapter;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> mCustomer;
    private AlertDialog dialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_chat, container, false);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCustomer = new ArrayList<>();

        readCustomer();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.chat));
    }

    private void readCustomer() {
        createDialog("Cargando chats");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Common.CUSTOMER_TABLE);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCustomer.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Customer customer = snapshot.getValue(Customer.class);

                    assert customer != null;
                    assert firebaseUser != null;
                    mCustomer.add(customer);
                    /*if (!customer.getIdCus().equals(firebaseUser.getUid())){
                        mCustomer.add(customer);
                    }*/



                }

                customerAdapter = new CustomerAdapter(getContext(), mCustomer);
                recyclerView.setAdapter(customerAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void createDialog(String message){
        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(message)
                .build();
        dialog.show();
    }


}
