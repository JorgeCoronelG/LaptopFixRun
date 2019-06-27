package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Adapter.AdapterComments;
import com.laptopfix.laptopfixrun.Model.Comment;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;


public class ComentFragment extends Fragment implements View.OnClickListener {

    ArrayList<Comment> listComments;
    RecyclerView recyclerView;

    View view;
    Button btnComentario;
    EditText txtWrite;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_coment, container, false);
        btnComentario = view.findViewById(R.id.btnAddComment);

        listComments = new ArrayList<>();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnComentario.setOnClickListener(this);

        obtenerCommentario();

        AdapterComments adapterComments = new AdapterComments(listComments);
        recyclerView.setAdapter(adapterComments);

        return view;
    }

    private void obtenerCommentario() {


        listComments.add(new Comment("Osvaldo Escamilla","Excelente servicio","21/06/19",R.drawable.ic_star));
        listComments.add(new Comment("Jorge Coronel","Excelente servicio","21/06/19",R.drawable.ic_star));
        listComments.add(new Comment("Osvaldo Escamilla","Excelente servicio","21/06/19",R.drawable.ic_star));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.comments));
    }

    RadioButton op1,op2,op3,op4,op5;

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnAddComment:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.add_comment,null);
                txtWrite = view.findViewById(R.id.etWriteComment);
                op1 = view.findViewById(R.id.r1);
                op2 = view.findViewById(R.id.r2);
                op3 = view.findViewById(R.id.r3);
                op4 = view.findViewById(R.id.r4);
                op5 = view.findViewById(R.id.r5);

                alert.setView(view).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Comentario enviado", Toast.LENGTH_SHORT).show();
                    }
                }).
                setNegativeButton("CANCELAR",null);

                alert.show();

                break;
        }
    }





}
