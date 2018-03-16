package com.company.zicure.baseapplication.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.models.AnimationItem;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentShowProduct extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_show_product;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context, 4);
    }

    @Override
    protected AnimationItem[] getAnimationItems() {
        return new AnimationItem[] {
                new AnimationItem("Scale random", R.anim.grid_layout_animation_scale_random)
        };
    }

    @Override
    protected int getTypeLayout() {
        return 1;
    }
}
