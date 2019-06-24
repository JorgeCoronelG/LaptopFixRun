package com.laptopfix.laptopfixrun.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Model.Comment;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolderComments> {


    ArrayList<Comment> listComments;

    public AdapterComments(ArrayList<Comment> listComments) {
        this.listComments = listComments;
    }

    @Override
    public ViewHolderComments onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,null,false);
        return new ViewHolderComments(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderComments holder, int position) {
        holder.name.setText(listComments.get(position).getUser());
        holder.comment.setText(listComments.get(position).getComment());
        holder.date.setText(listComments.get(position).getDate());
        holder.img.setImageResource(listComments.get(position).getCa());

    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    public class ViewHolderComments extends RecyclerView.ViewHolder {

        //Poner referencias de item_comment.xml

        TextView name, comment, date;
        ImageView img;

        public ViewHolderComments(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtNameC);
            comment = itemView.findViewById(R.id.txtCommentC);
            date = itemView.findViewById(R.id.txtDateC);
            img = itemView.findViewById(R.id.imgCa);
        }

        public void asignarComentarios(String s) {
            name.setText(s);
            comment.setText(s);
            date.setText(s);

        }
    }
}
