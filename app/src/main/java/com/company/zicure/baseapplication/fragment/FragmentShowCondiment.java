package com.company.zicure.baseapplication.fragment;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.models.AnimationItem;

public class FragmentShowCondiment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_show_condiment;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new GridLayoutManager(context, 6);
    }

    @Override
    protected AnimationItem[] getAnimationItems() {
        return new AnimationItem[] {
                new AnimationItem("Scale random", R.anim.grid_layout_animation_scale_random)
        };
    }

    @Override
    protected int getTypeLayout() {
        return 2;
    }
}
