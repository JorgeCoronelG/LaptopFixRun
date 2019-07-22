package com.laptopfix.laptopfixrun.Fragment.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.laptopfix.laptopfixrun.Adapter.CommentAdapter;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CommentController;
import com.laptopfix.laptopfixrun.Controller.CustomerController;
import com.laptopfix.laptopfixrun.Model.Comment;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;


public class CommentFragment extends Fragment implements View.OnClickListener, CommentController.VolleyListener {

    private View view;
    private Button btnComentario;
    private EditText etWrite;
    private RadioButton op1,op2,op3,op4,op5;
    private CommentController commentController;
    private RecyclerView commentRecycler;
    private CommentAdapter commentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_comment, container, false);

        commentController = new CommentController(getContext());
        commentController.setVolleyListener(this);

        commentRecycler = view.findViewById(R.id.recyclerComment);
        btnComentario = view.findViewById(R.id.btnAddComment);

        btnComentario.setOnClickListener(this);

        commentController.getComments(CommunicationCode.CODE_GET_COMMENTS);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.comments));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddComment:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.add_comment,null);

                etWrite = view.findViewById(R.id.etWriteComment);
                op1 = view.findViewById(R.id.r1);
                op2 = view.findViewById(R.id.r2);
                op3 = view.findViewById(R.id.r3);
                op4 = view.findViewById(R.id.r4);
                op5 = view.findViewById(R.id.r5);

                alert.setView(view).
                setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etWrite.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Debes agregar un comentario", Toast.LENGTH_SHORT).show();
                        }else{
                            Comment comment = new Comment();
                            comment.setComment(etWrite.getText().toString());
                            if(op1.isChecked()){
                                comment.setScore(1);
                            }else if(op2.isChecked()){
                                comment.setScore(2);
                            }else if(op3.isChecked()){
                                comment.setScore(3);
                            }else if(op4.isChecked()){
                                comment.setScore(4);
                            }else if(op5.isChecked()){
                                comment.setScore(5);
                            }
                            comment.setCustomer(new CustomerController(getContext()).getCustomer());
                            commentController.insert(comment);
                        }
                    }
                }).
                setNegativeButton(getString(R.string.cancel),null);
                alert.show();

                break;
        }
    }

    @Override
    public void requestGetComments(ArrayList<Comment> comments, int code) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentRecycler.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(comments, R.layout.item_comment, getActivity());
        commentRecycler.setAdapter(commentAdapter);
        if(code == CommunicationCode.CODE_COMMENT_INSERT){
            Snackbar.make(view, "Tu comentario ha sido agregado", Snackbar.LENGTH_LONG).show();
        }
    }
}