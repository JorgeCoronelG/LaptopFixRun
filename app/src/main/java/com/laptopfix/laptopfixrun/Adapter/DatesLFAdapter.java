package com.laptopfix.laptopfixrun.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Model.Date;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class DatesLFAdapter extends RecyclerView.Adapter<DatesLFAdapter.DatesLFViewHolder> {

    private ArrayList<Date> dates;
    private int resource;
    private Activity activity;
    private OnDateListener onDateListener;

    public DatesLFAdapter(ArrayList<Date> dates, int resource, Activity activity, OnDateListener onDateListener) {
        this.dates = dates;
        this.resource = resource;
        this.activity = activity;
        this.onDateListener = onDateListener;
    }

    @Override
    public DatesLFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DatesLFViewHolder(view, onDateListener);
    }

    @Override
    public void onBindViewHolder(DatesLFViewHolder holder, int position) {
        Date date = dates.get(position);
        holder.txtName.setText(date.getCustomer().getName());
        holder.txtDate.setText(date.getDate());
        holder.txtHour.setText(date.getHour());
        if(date.getResidenceCus().equals(activity.getString(R.string.na))){
            holder.txtRevision.setText(activity.getString(R.string.revision_laptop_fix));
        }else{
            holder.txtRevision.setText(activity.getString(R.string.revision_home_service));
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class DatesLFViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtName;
        private TextView txtDate;
        private TextView txtHour;
        private TextView txtRevision;
        private OnDateListener onDateListener;

        public DatesLFViewHolder(View itemView, OnDateListener onDateListener) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtNameCustomer);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtHour = itemView.findViewById(R.id.txtHour);
            txtRevision = itemView.findViewById(R.id.txtRevision);
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