package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.RedeemFragment;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.EarnFragment;
import com.bizzmark.seller.sellerwithoutlogin.sellerapp.ViewPageAdapter;
import com.bizzmark.seller.sellerwithoutlogin.login.Seller_Basic_Information;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;
    TabLayout tabLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);

        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new EarnFragment(),"EARN");
        viewPagerAdapter.addFragments(new RedeemFragment(),"REDEEM");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void backbut(){
        ReportActivity.super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v== backbut){
            backbut();
        }
    }
}
