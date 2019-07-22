package com.laptopfix.laptopfixrun.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Model.Comment;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private ArrayList<Comment> comments;
    private int resource;
    private Activity activity;

    public CommentAdapter(ArrayList<Comment> comments, int resource, Activity activity) {
        this.comments = comments;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.txtNameCustomer.setText(comment.getCustomer().getName());
        holder.txtDateComment.setText(comment.getDateComment());
        holder.txtComment.setText(comment.getComment());

        holder.layoutStars.removeAllViews();
        for(int i = 0; i < comment.getScore(); i++){
            ImageView imgStar = new ImageView(activity);
            imgStar.setImageResource(R.drawable.ic_star);
            LinearLayout.LayoutParams paramsStar = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsStar.setMargins(0,0,0,0);
            imgStar.setLayoutParams(paramsStar);
            if(holder.layoutStars != null){
                holder.layoutStars.addView(imgStar);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNameCustomer;
        private TextView txtDateComment;
        private LinearLayout layoutStars;
        private TextView txtComment;

        public CommentViewHolder(View itemView) {
            super(itemView);

            txtNameCustomer = itemView.findViewById(R.id.txtNameCustomer);
            txtDateComment = itemView.findViewById(R.id.txtDateComment);
            layoutStars = itemView.findViewById(R.id.layoutStars);
            txtComment = itemView.findViewById(R.id.txtComment);
        }
    }

}