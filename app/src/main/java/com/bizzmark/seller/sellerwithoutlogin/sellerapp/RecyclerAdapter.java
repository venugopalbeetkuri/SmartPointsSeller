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
 * Created by Tharun on 05-05-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public List<TransactionList> transactionLists;
    public Context context;

    public RecyclerAdapter(List<TransactionList> transactionLists, Context context) {
        this.transactionLists = transactionLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactions_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final TransactionList transactionList = transactionLists.get(position);
        holder.billReport.setText(transactionList.getBillReport());
        holder.billDateAndTime.setText(transactionList.getBillDateAndTime());
        holder.billPoints.setText(transactionList.getBillPoints());
        holder.billDeviceId.setText(transactionList.getBillDeviceId());
        holder.storename.setText(transactionList.getStorename());
        holder.type.setText(transactionList.getType());
    }

    @Override
    public int getItemCount() {
        return transactionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView billReport;
        public TextView billDateAndTime;
        public TextView billPoints;
        public TextView billDeviceId;
        public TextView storename;
        public TextView type;

        public ViewHolder(View itemView) {
            super(itemView);
            billReport = (TextView) itemView.findViewById(R.id.billReport);
            billDateAndTime = (TextView) itemView.findViewById(R.id.billDateAndTime);
            billPoints = (TextView) itemView.findViewById(R.id.billPoints);
            billDeviceId = (TextView) itemView.findViewById(R.id.billDeviceId);
            storename = (TextView) itemView.findViewById(R.id.storename);
            type = (TextView) itemView.findViewById(R.id.type);
        }
    }
}
