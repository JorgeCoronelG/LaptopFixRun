package com.laptopfix.laptopfixrun.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class DatesTechnicalAdapter extends RecyclerView.Adapter<DatesTechnicalAdapter.DatesTechnicalViewHolder> {

    private ArrayList<DateHome> dates;
    private int resource;
    private Activity activity;
    private OnDateListener onDateListener;

    public DatesTechnicalAdapter(ArrayList<DateHome> dates, int resource, Activity activity, OnDateListener onDateListener) {
        this.dates = dates;
        this.resource = resource;
        this.activity = activity;
        this.onDateListener = onDateListener;
    }

    @NonNull
    @Override
    public DatesTechnicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DatesTechnicalViewHolder(view, onDateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DatesTechnicalViewHolder holder, int position) {
        DateHome date = dates.get(position);
        holder.txtName.setText(date.getCustomer().getName());
        if(date.getService() == 0){
            holder.llUrgent.setVisibility(View.GONE);
            holder.txtDate.setText("Fecha: "+ date.getDate());
            holder.txtHour.setText("Hora: "+ date.getHour());
        }else{
            holder.llDateHour.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class DatesTechnicalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtName;
        private TextView txtDate;
        private TextView txtHour;
        private LinearLayout llDateHour;
        private LinearLayout llUrgent;
        private OnDateListener onDateListener;

        public DatesTechnicalViewHolder(@NonNull View itemView, OnDateListener onDateListener) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtNameCustomer);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtHour = itemView.findViewById(R.id.txtHour);
            llDateHour = itemView.findViewById(R.id.llDateHour);
            llUrgent = itemView.findViewById(R.id.llUrgent);
            this.onDateListener = onDateListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDateListener.onDateClick(getAdapterPosition());
        }
    }


    public interface OnDateListener{
        void onDateClick(int position);
    }

}