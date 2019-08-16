package com.laptopfix.laptopfixrun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laptopfix.laptopfixrun.Activities.Customer.MessageSoporteActivity;
import com.laptopfix.laptopfixrun.Model.LaptopFix;
import com.laptopfix.laptopfixrun.R;

import java.util.List;

public class LaptopFixAdapter extends RecyclerView.Adapter<LaptopFixAdapter.ViewHolder> {

    private Context mContext;
    private List<LaptopFix> mLaptopFix;

    public LaptopFixAdapter(Context mContex, List<LaptopFix> mLaptopFix){
        this.mLaptopFix = mLaptopFix;
        this.mContext = mContex;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new LaptopFixAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final LaptopFix laptopFix = mLaptopFix.get(position);
        holder.username.setText(laptopFix.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageSoporteActivity.class);
                intent.putExtra("userid", laptopFix.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLaptopFix.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;

        public ViewHolder(View itemView){

            super(itemView);
            username = itemView.findViewById(R.id.username);
        }
    }

}
