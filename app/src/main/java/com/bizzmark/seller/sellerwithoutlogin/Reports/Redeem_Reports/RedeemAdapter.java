package com.bizzmark.seller.sellerwithoutlogin.Reports.Redeem_Reports;

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

    public List<RedeemFragTransList> redeemFragTransLists;
    public Context context;

    public RedeemAdapter(List<RedeemFragTransList> redeemFragTransLists, Context context) {
        this.redeemFragTransLists = redeemFragTransLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_redeem_frag_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final RedeemFragTransList redeemFragTransList = redeemFragTransLists.get(position);
        holder.redeemTransId.setText(redeemFragTransList.getRedeemTransId());
        holder.redeemOriginalBillReport.setText(redeemFragTransList.getRedeemOriginalBillReport());
        holder.redeemBillpoints.setText(redeemFragTransList.getRedeemBillpoints());
        holder.redeemDiscountedAmount.setText(redeemFragTransList.getRedeemDiscountedAmount());
        holder.redeemnewbillamount.setText(redeemFragTransList.getRedeemnewbillamount());
        holder.redeemTransactionAt.setText(redeemFragTransList.getRedeemTransactionAt());
    }

    @Override
    public int getItemCount() {
        return redeemFragTransLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView redeemTransId;
        public TextView redeemOriginalBillReport;
        public TextView redeemBillpoints;
        public TextView redeemDiscountedAmount;
        public TextView redeemnewbillamount;
        public TextView redeemTransactionAt;

        public ViewHolder(View itemView) {
            super(itemView);

            redeemTransId = (TextView) itemView.findViewById(R.id.redeemTransId);
            redeemOriginalBillReport = (TextView) itemView.findViewById(R.id.redeemOriginalBillReport);
            redeemBillpoints = (TextView) itemView.findViewById(R.id.redeemBillpoints);
            redeemDiscountedAmount = (TextView) itemView.findViewById(R.id.redeemDiscountedAmount);
            redeemnewbillamount = (TextView) itemView.findViewById(R.id.redeemnewbillamount);
            redeemTransactionAt = (TextView) itemView.findViewById(R.id.redeemTransactionAt);
        }
    }
}
