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

public class DatesCustomerAdapter extends RecyclerView.Adapter<DatesCustomerAdapter.DatesCustomerViewHolder> {

    private ArrayList<Date> dates;
    private int resource;
    private Activity activity;

    public DatesCustomerAdapter(ArrayList<Date> dates, int resource, Activity activity) {
        this.dates = dates;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public DatesCustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DatesCustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DatesCustomerViewHolder holder, int position) {
        Date date = dates.get(position);
        holder.txtDate.setText(date.getDate());
        holder.txtHour.setText(date.getHour());
        if(date.getResidenceCus().equals(activity.getString(R.string.na))){
            holder.txtRevision.setText(activity.getString(R.string.revision_laptop_fix));
        }else{
            holder.txtRevision.setText(activity.getString(R.string.revision_home_service));
        }
        holder.txtProblem.setText(date.getDesProblem());
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class DatesCustomerViewHolder extends RecyclerView.ViewHolder{

        private TextView txtDate;
        private TextView txtHour;
        private TextView txtRevision;
        private TextView txtProblem;

        public DatesCustomerViewHolder(View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtHour = itemView.findViewById(R.id.txtHour);
            txtRevision = itemView.findViewById(R.id.txtRevision);
            txtProblem = itemView.findViewById(R.id.txtProblem);
        }
    }

}