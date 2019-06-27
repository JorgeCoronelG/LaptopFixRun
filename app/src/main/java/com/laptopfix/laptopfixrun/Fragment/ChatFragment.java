package com.laptopfix.laptopfixrun.Fragment;

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

import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.User;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    //private CustomerAdapter customerAdapter;
    private List<Customer> mCustomer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.fragment_chat, container, false);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCustomer = new ArrayList<>();

        readCustomer();

        return view;
    }

    private void readCustomer() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Customers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCustomer.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Customer customer = snapshot.getValue(Customer.class);
                    mCustomer.add(customer);
                }

               /* customerAdapter = new CustomerAdapter(getContext(), mCustomer);
                recyclerView.setAdapter(customerAdapter);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
