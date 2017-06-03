package com.bizzmark.seller.sellerwithoutlogin.sellerapp;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizzmark.seller.sellerwithoutlogin.R;
import com.bizzmark.seller.sellerwithoutlogin.Reports.Earn_Reports.EarnFragment;
import com.bizzmark.seller.sellerwithoutlogin.Reports.Redeem_Reports.RedeemFragment;

import static com.bizzmark.seller.sellerwithoutlogin.WifiDirectReceive.storeName;
import static com.bizzmark.seller.sellerwithoutlogin.login.Login.sellerStoreName;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backbut;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;
    TabLayout tabLayout;
    TextView reportHeader;
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
//        reportHeader = (TextView) findViewById(R.id.reportHeader);
//        reportHeader.setText(sellerStoreName);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);

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
