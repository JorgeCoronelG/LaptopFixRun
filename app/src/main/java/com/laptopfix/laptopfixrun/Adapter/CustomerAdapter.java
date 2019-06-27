package com.laptopfix.laptopfixrun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.MessageActivity;
import com.laptopfix.laptopfixrun.Model.Customer;
import com.laptopfix.laptopfixrun.R;

import java.util.List;

/*public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private Context mContext;
    private List<Customer> mCusto;

    public CustomerAdapter(Context mContext, List<Customer> mCusto){
        this.mCusto = mCusto;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new CustomerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Customer user = mCusto.get(position);
        holder.username.setText(user.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getIdCus());
                mContext.startActivity(intent);


        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
        }
    }
}*/
