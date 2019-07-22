package com.laptopfix.laptopfixrun.Fragment.LaptopFix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laptopfix.laptopfixrun.Adapter.CommentAdapter;
import com.laptopfix.laptopfixrun.Communication.CommunicationCode;
import com.laptopfix.laptopfixrun.Controller.CommentController;
import com.laptopfix.laptopfixrun.Model.Comment;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class CommentFragment extends Fragment implements CommentController.VolleyListener {

    private View view;
    private CommentController commentController;
    private RecyclerView commentRecycler;
    private CommentAdapter commentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_comment_lf, container, false);

        commentController = new CommentController(getContext());
        commentController.setVolleyListener(this);

        commentRecycler = view.findViewById(R.id.recyclerComment);

        commentController.getComments(CommunicationCode.CODE_GET_COMMENTS);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.comments));
    }

    @Override
    public void requestGetComments(ArrayList<Comment> comments, int code) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentRecycler.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(comments, R.layout.item_comment, getActivity());
        commentRecycler.setAdapter(commentAdapter);
    }
}
