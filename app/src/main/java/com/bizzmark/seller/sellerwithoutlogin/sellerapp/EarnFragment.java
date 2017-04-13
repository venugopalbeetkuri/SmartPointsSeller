package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.app.DatePickerDialog;
import android.app.DialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;

import java.util.Calendar;

/**
 * Created by Tharun on 12-04-2017.
 */

public class EarnFragment extends Fragment implements View.OnClickListener {

    TextView tvFromDate,tvToDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.report_earn_fragment,container,false);
        findViewById(view);
        return view;
    }

    private void findViewById(View view) {

        tvFromDate=(TextView)view.findViewById(R.id.tvFromDate);
        tvToDate=(TextView)view.findViewById(R.id.tvToDate);

        tvFromDate.setOnClickListener(this);
        tvToDate.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(FragmentManager m, "datePicker");

//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        // Create a new instance of DatePickerDialog and return it
//        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//            }
//        }, year, month, day);
//        dialog.show();


    }
}
