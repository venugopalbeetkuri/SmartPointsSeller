package com.bizzmark.seller.sellerwithoutlogin.Reports.Last_Ten_Transactions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;

import java.util.List;

/**
 * Created by Tharun on 22-05-2017.
 */

public class LastTenTransactionsAdapter extends RecyclerView.Adapter<LastTenTransactionsAdapter.ViewHolder> {

    public List<LastTenTransactionsList> tenTransactionsLists;
    public Context context;

    public LastTenTransactionsAdapter(List<LastTenTransactionsList> tenTransactionsLists, Context context){
        this.tenTransactionsLists = tenTransactionsLists;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_ten_transaction_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LastTenTransactionsList tenTransactionsList = tenTransactionsLists.get(position);
        holder.transId.setText(tenTransactionsList.getTransId());
        holder.lastTenTransType.setText(tenTransactionsList.getLastTenTransType());
        holder.originalBillReport.setText(tenTransactionsList.getOriginalBillReport());
        holder.recentTranBillpoints.setText(tenTransactionsList.getRecentTranBillpoints());
        holder.discountedAmount.setText(tenTransactionsList.getDiscountedAmount());
        holder.newbillamount.setText(tenTransactionsList.getNewbillamount());
        holder.transactionAt.setText(tenTransactionsList.getTransactionAt());
    }

    @Override
    public int getItemCount() {
        return tenTransactionsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView transId;
        public TextView lastTenTransType;
        public TextView originalBillReport;
        public TextView recentTranBillpoints;
        public TextView discountedAmount;
        public TextView newbillamount;
        public TextView transactionAt;
        public ViewHolder(View itemView) {
            super(itemView);
            transId = (TextView) itemView.findViewById(R.id.transId);
            lastTenTransType = (TextView) itemView.findViewById(R.id.lastTenTransType);
            originalBillReport = (TextView)itemView.findViewById(R.id.originalBillReport);
            recentTranBillpoints = (TextView) itemView.findViewById(R.id.recentTranBillpoints);
            discountedAmount = (TextView) itemView.findViewById(R.id.discountedAmount);
            newbillamount = (TextView) itemView.findViewById(R.id.newbillamount);
            transactionAt = (TextView) itemView.findViewById(R.id.transactionAt);
        }
    }
}
