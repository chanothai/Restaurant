package com.company.zicure.baseapplication.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.common.BaseActivity;
import com.company.zicure.baseapplication.fragment.ChooseIngredientFragment;
import com.company.zicure.baseapplication.fragment.FragmentShowCondiment;
import com.company.zicure.baseapplication.fragment.FragmentShowProduct;
import com.company.zicure.baseapplication.fragment.ListItemFragment;
import com.company.zicure.baseapplication.utility.ModelCart;
import com.jgabrielfreitas.core.BlurImageView;

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

        BlurImageView blurImageView = findViewById(R.id.bg_image_food);
        blurImageView.setBlur(3);

    }

    private void setupView(){
        int viewPattern = getIntent().getExtras().getInt("view_pattern");
        if (viewPattern == 0) {
            callItemMeal();
            containerItem.setVisibility(View.GONE);
        }else if (viewPattern == 1){
            callItemCondiment();
            containerItem.setVisibility(View.GONE);
        }else if (viewPattern == 2) {
            callItemIngredient();
            callListItem();
            containerItem.setVisibility(View.VISIBLE);
        }
    }

    private void callListItem(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new ListItemFragment(), "Fragment_list_item");
        transaction.commit();
    }

    public void updateListItem() {
        FragmentManager fm = getSupportFragmentManager();
        ListItemFragment fragment = (ListItemFragment) fm.findFragmentByTag("Fragment_list_item");
        if (fragment != null) {
            fragment.getUpdateItem();
        }
    }

    public void callItemIngredient(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_list, new ChooseIngredientFragment(), "Fragment_choose_ingredient");
        transaction.commit();
    }

    public void callItemMeal(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_list, new FragmentShowProduct(), "Fragment_show_product");
        transaction.commit();
    }

    public void callItemCondiment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_list, new FragmentShowCondiment(), "Fragment_show_condiment");
        transaction.commit();
    }
}
