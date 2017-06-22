package com.bizzmark.seller.sellerwithoutlogin.sellerInfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;

import java.util.List;

/**
 * Created by Tharun on 10-06-2017.
 */

public class SellerInfoAdapter extends RecyclerView.Adapter<SellerInfoAdapter.ViewHolder> {

    public List<SellerInfoList> sellerInfoLists;
    public Context context;

    public SellerInfoAdapter(List<SellerInfoList> sellerInfoLists, Context context){
        this.sellerInfoLists = sellerInfoLists;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_info_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SellerInfoList sellerInfoList = sellerInfoLists.get(position);
        holder.sellerStoreName.setText(sellerInfoList.getSellerStoreName());
        holder.sellerStoreId.setText(sellerInfoList.getSellerStoreId());
        holder.sellerBranchName.setText(sellerInfoList.getSellerBranchName());
        holder.sellerBranchId.setText(sellerInfoList.getSellerBranchId());
        holder.storePointsPercentage.setText(sellerInfoList.getStorePointsPercentage());
        holder.storePointsValue.setText(sellerInfoList.getStorePointsValue());
        holder.sellerStoreEmail.setText(sellerInfoList.getSellerStoreEmail());
        holder.sellerStoreAddress.setText(sellerInfoList.getSellerStoreAddress());
    }

    @Override
    public int getItemCount() {
        return sellerInfoLists.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        public TextView sellerStoreName;
        public TextView sellerStoreId;
        public TextView sellerBranchName;
        public TextView sellerBranchId;
        public TextView storePointsPercentage;
        public TextView storePointsValue;
        public TextView sellerStoreEmail;
        public TextView sellerStoreAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            sellerStoreName = (TextView) itemView.findViewById(R.id.sellerStoreName);
            sellerStoreId = (TextView) itemView.findViewById(R.id.sellerStoreId);
            sellerBranchName = (TextView) itemView.findViewById(R.id.sellerBranchName);
            sellerBranchId = (TextView) itemView.findViewById(R.id.sellerBranchId);
            storePointsPercentage = (TextView) itemView.findViewById(R.id.storePointsPercentage);
            storePointsValue = (TextView) itemView.findViewById(R.id.storePointsValue);
            sellerStoreEmail = (TextView) itemView.findViewById(R.id.sellerStoreEmail);
            sellerStoreAddress = (TextView) itemView.findViewById(R.id.sellerStoreAddress);

        }
    }
}
