package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;

import java.util.List;

/**
 * Created by Tharun on 13-05-2017.
 */

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.ViewHolder> {

    public List<TransactionList> transactionLists;
    public Context context;

    public RedeemAdapter(List<TransactionList> transactionLists, Context context) {
        this.transactionLists = transactionLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.redeem_transactions,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TransactionList transactionList = transactionLists.get(position);
        holder.cust_deviceId.setText(transactionList.getBillDeviceId());
        holder.original_bill_amount.setText(transactionList.getBillReport());
        holder.transacted_at.setText(transactionList.getBillDateAndTime());
        holder.points.setText(transactionList.getBillPoints());
        holder.type.setText(transactionList.getType());
        holder.discounted_bill_amount.setText(transactionList.getDiscounted_bill_amount());
        holder.storename.setText(transactionList.getStorename());
    }

    @Override
    public int getItemCount() {
        return transactionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView cust_deviceId;
        public TextView original_bill_amount;
        public TextView transacted_at;
        public TextView points;
        public TextView type;
        public TextView discounted_bill_amount;
        public TextView storename;

        public ViewHolder(View itemView) {
            super(itemView);

            cust_deviceId = (TextView) itemView.findViewById(R.id.cust_deviceId);
            original_bill_amount = (TextView) itemView.findViewById(R.id.original_bill_amount);
            transacted_at = (TextView) itemView.findViewById(R.id.transacted_at);
            points = (TextView) itemView.findViewById(R.id.points);
            type = (TextView) itemView.findViewById(R.id.type);
            discounted_bill_amount = (TextView) itemView.findViewById(R.id.discounted_bill_amount);
            storename = (TextView) itemView.findViewById(R.id.storename);


        }
    }
}
