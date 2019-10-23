package com.laptopfix.laptopfixrun.Fragment.Customer;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.laptopfix.laptopfixrun.Adapter.LaptopFixAdapter;
import com.laptopfix.laptopfixrun.Model.LaptopFix;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Constants;

import java.util.ArrayList;
import java.util.List;


public class ChatSoporteFragment extends Fragment {

    private RecyclerView recyclerView;
    private LaptopFixAdapter laptopFixAdapter;
    private List<LaptopFix> mLaptop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_soporte, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mLaptop = new ArrayList<>();

        readLaptop();
        return view;
    }

    private void readLaptop() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.LAPTOP_FIX_TABLE);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mLaptop.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    LaptopFix laptopFix = snapshot.getValue(LaptopFix.class);

                    assert laptopFix != null;
                    assert firebaseUser != null;
                    if(!laptopFix.getId().equals(firebaseUser.getUid())){
                        mLaptop.add(laptopFix);
                    }
                }
                laptopFixAdapter = new LaptopFixAdapter(getContext(), mLaptop);
                recyclerView.setAdapter(laptopFixAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
