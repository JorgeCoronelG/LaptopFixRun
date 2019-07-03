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

public class AdapterDatesLF extends RecyclerView.Adapter<AdapterDatesLF.DatesLFViewHolder> {

    private ArrayList<Date> dates;
    private int resource;
    private Activity activity;

    public AdapterDatesLF(ArrayList<Date> dates, int resource, Activity activity) {
        this.dates = dates;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public DatesLFViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DatesLFViewHolder(view);
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

    public class DatesLFViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private TextView txtDate;
        private TextView txtHour;
        private TextView txtRevision;

        public DatesLFViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtNameCustomer);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtHour = itemView.findViewById(R.id.txtHour);
            txtRevision = itemView.findViewById(R.id.txtRevision);
        }
    }

}