package com.bizzmark.seller.sellerwithoutlogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class FAQ extends AppCompatActivity implements View.OnClickListener {
    ExpandableRelativeLayout expandablelayout1,expandablelayout2,expandablelayout3,expandablelayout4,expandablelayout5,expandablelayout6,expandablelayout7,expandablelayout8,expandablelayout9,expandablelayout10,expandablelayout11;
    private ImageView backbut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_faq);
        backbut=(ImageView)findViewById(R.id.backbut);
        backbut.setOnClickListener(this);

    }
    public void expandableButton1(View view){
        expandablelayout1=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout1);
        expandablelayout1.toggle();
    }
    public void expandableButton2(View view){
        expandablelayout2=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout2);
        expandablelayout2.toggle();

    }
    public void expandableButton3(View view){
        expandablelayout3=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout3);
        expandablelayout3.toggle();
    }

    public void expandableButton4(View view){
        expandablelayout4=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout4);
        expandablelayout4.toggle();
    }

    public void expandableButton5(View view){
        expandablelayout5=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout5);
        expandablelayout5.toggle();
    }

    public void expandableButton6(View view){
        expandablelayout6=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout6);
        expandablelayout6.toggle();
    }

    public void expandableButton7(View view){
        expandablelayout7=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout7);
        expandablelayout7.toggle();
    }

    public void expandableButton8(View view){
        expandablelayout8=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout8);
        expandablelayout8.toggle();
    }

    public void expandableButton9(View view){
        expandablelayout9=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout9);
        expandablelayout9.toggle();
    }

    public void expandableButton10(View view){
        expandablelayout10=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout10);
        expandablelayout10.toggle();
    }

    public void expandableButton11(View view){
        expandablelayout11=(ExpandableRelativeLayout)findViewById(R.id.expandableLayout11);
        expandablelayout11.toggle();
    }

    //method for backbut

    private void backbut(){
        FAQ.super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if(v==backbut){
            backbut();
        }
    }
}
