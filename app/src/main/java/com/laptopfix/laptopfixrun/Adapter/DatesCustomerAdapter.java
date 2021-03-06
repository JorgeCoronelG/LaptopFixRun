package com.laptopfix.laptopfixrun.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Model.DateHome;
import com.laptopfix.laptopfixrun.R;

import java.util.ArrayList;

public class DatesCustomerAdapter extends RecyclerView.Adapter<DatesCustomerAdapter.DatesCustomerViewHolder> {

    private ArrayList<DateHome> dates;
    private int resource;
    private Activity activity;
    private OnDateListener onDateListener;

    public DatesCustomerAdapter(ArrayList<DateHome> dates, int resource, Activity activity, OnDateListener onDateListener) {
        this.dates = dates;
        this.resource = resource;
        this.activity = activity;
        this.onDateListener = onDateListener;
    }

    @Override
    public DatesCustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DatesCustomerViewHolder(view, onDateListener);
    }

    @Override
    public void onBindViewHolder(DatesCustomerViewHolder holder, int position) {
        DateHome dateHome = dates.get(position);
        holder.txtId.setText("Cita #"+dateHome.getId());
        holder.txtProblem.setText("Problema: "+dateHome.getProblem());
        if(dateHome.getService() == 0){
            holder.llUrgent.setVisibility(View.GONE);
            holder.txtDate.setText("Fecha: "+dateHome.getDate());
            holder.txtHour.setText("Hora: "+dateHome.getHour());
        }else{
            holder.llDateHour.setVisibility(View.GONE);
        }
        switch(dateHome.getStatus()){
            case 0:
                holder.txtStatus.setText("Estatus: Pendiente");
                break;
            case 1:
                holder.txtStatus.setText("Estatus: Aceptado");
                break;
            case 2:
                holder.txtStatus.setText("Estatus: En reparación");
                break;
            case 3:
                holder.txtStatus.setText("Estatus: Equipo reparado");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class DatesCustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtId;
        private TextView txtDate;
        private TextView txtHour;
        private TextView txtProblem;
        private TextView txtStatus;
        private LinearLayout llDateHour;
        private LinearLayout llUrgent;
        private OnDateListener onDateListener;

        public DatesCustomerViewHolder(View itemView, OnDateListener onDateListener) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtHour = itemView.findViewById(R.id.txtHour);
            txtProblem = itemView.findViewById(R.id.txtProblem);
            txtStatus = itemView.findViewById(R.id.txtStatus);
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