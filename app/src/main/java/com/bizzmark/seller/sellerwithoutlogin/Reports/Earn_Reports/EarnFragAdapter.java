package com.bizzmark.seller.sellerwithoutlogin.Reports.Earn_Reports;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;

import java.util.List;

/**
 * Created by Tharun on 23-05-2017.
 */

public class EarnFragAdapter extends RecyclerView.Adapter<EarnFragAdapter.ViewHolder> {

    public List<EarnFragTansList> earnFragTansLists;
    public Context context;

    public EarnFragAdapter(List<EarnFragTansList> earnFragTansLists, Context context) {
        this.earnFragTansLists = earnFragTansLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_earn_frag_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EarnFragTansList earnFragTansList = earnFragTansLists.get(position);
        holder.earnTransId.setText(earnFragTansList.getEarnTransId());
        holder.earnOriginalBillReport.setText(earnFragTansList.getEarnOriginalBillReport());
        holder.earnBillpoints.setText(earnFragTansList.getEarnBillpoints());
        holder.earnTransactionAt.setText(earnFragTansList.getEarnTransactionAt());
    }

    @Override
    public int getItemCount() {
        return earnFragTansLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView earnTransId;
        public TextView earnOriginalBillReport;
        public TextView earnBillpoints;
        public TextView earnTransactionAt;

        public ViewHolder(View itemView) {
            super(itemView);

            earnTransId = (TextView)itemView.findViewById(R.id.earnTransId);
            earnOriginalBillReport = (TextView) itemView.findViewById(R.id.earnOriginalBillReport);
            earnBillpoints = (TextView) itemView.findViewById(R.id.earnBillpoints);
            earnTransactionAt = (TextView) itemView.findViewById(R.id.earnTransactionAt);
        }
    }
}
