package com.bizzmark.seller.sellerwithoutlogin.My_Customers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;

import java.util.List;

/**
 * Created by Tharun on 29-05-2017.
 */

public class MyCustomersRCAdapter extends RecyclerView.Adapter<MyCustomersRCAdapter.ViewHolder> {

    public List<MyCustomersList> myCustomersLists;
    public Context context;

    public MyCustomersRCAdapter(List<MyCustomersList> myCustomersLists, Context context) {
        this.myCustomersLists = myCustomersLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_my_customers_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyCustomersList myCustomersList = myCustomersLists.get(position);
        holder.numberOfBranchCustomers.setText(myCustomersList.getBranchVistors());
        holder.numberOfStoreCustomers.setText(myCustomersList.getStoreVisitors());
    }

    @Override
    public int getItemCount() {
        return myCustomersLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView numberOfBranchCustomers;
        private TextView numberOfStoreCustomers;
        public ViewHolder(View itemView) {
            super(itemView);
            numberOfBranchCustomers = (TextView)itemView.findViewById(R.id.numberOfBranchCustomers);
            numberOfStoreCustomers = (TextView)itemView.findViewById(R.id.numberOfStoreCustomers);
        }
    }
}
