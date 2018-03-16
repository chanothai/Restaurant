package com.company.zicure.baseapplication.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.common.BaseActivity;
import com.company.zicure.baseapplication.fragment.FragmentShowCondiment;
import com.company.zicure.baseapplication.fragment.FragmentShowProduct;

public class ShowItemActivity extends BaseActivity {

    //View
    FrameLayout containerItem = null, containerList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);
        bindView();
        setupView();

        if (savedInstanceState == null) {

        }
    }

    private void bindView(){
        containerItem = findViewById(R.id.container);
        containerList = findViewById(R.id.container_list);
    }

    private void setupView(){
        int viewPattern = getIntent().getExtras().getInt("view_pattern");
        if (viewPattern == 0) {
            callItemMeal();
            containerList.setVisibility(View.GONE);
        }else if (viewPattern == 1){
            callItemCondiment();
            containerList.setVisibility(View.GONE);
        }
    }

    private void callItemMeal(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new FragmentShowProduct(), "Fragment_show_product");
        transaction.commit();
    }

    private void callItemCondiment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new FragmentShowCondiment(), "Fragment_show_condiment");
        transaction.commit();
    }
}
