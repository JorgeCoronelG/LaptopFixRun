package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.laptopfix.laptopfixrun.Adapter.LaptopFixAdapter;
import com.laptopfix.laptopfixrun.Adapter.MessageAdapter;
import com.laptopfix.laptopfixrun.Activities.MessageActivity;
import com.laptopfix.laptopfixrun.Model.Chat;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.Model.LaptopFix;
import com.laptopfix.laptopfixrun.R;
import com.laptopfix.laptopfixrun.Util.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatCusFragment extends Fragment {


    private TextView username;

    private FirebaseUser fuser;
    private DatabaseReference reference;

    private ImageButton btn_send;
    private EditText text_send;

    private MessageAdapter messageAdapter;
    private List<Chat> mChat;


    private RecyclerView recyclerView;

    private Intent intent;

    private LaptopFixAdapter laptopFixAdapter;
    private List<LaptopFix> mLaptop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_cus, container, false);

        recyclerView = view.findViewById(R.id.Crecycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_send = view.findViewById(R.id.Cbtn_send);
        text_send = view.findViewById(R.id.Ctext_send);

        reference = FirebaseDatabase.getInstance().getReference("W7dtV7oJa0NuwTljHfcYDQvHGpi2").child("userid");

        intent = getActivity().getIntent();
        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        readLaptop();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), userid ,msg);
                } else {
                    Toast.makeText(getContext(), "No puedes enviar mensajes vacios", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LaptopFix laptopFix = dataSnapshot.getValue(LaptopFix.class);


                readMessage(fuser.getUid(), "W7dtV7oJa0NuwTljHfcYDQvHGpi2");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.chatlf));
    }


    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(final String myid, final String userid){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(getContext(), mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readLaptop() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("W7dtV7oJa0NuwTljHfcYDQvHGpi2");

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
