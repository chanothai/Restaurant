package com.company.zicure.baseapplication.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.models.AnimationItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListItemFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_show_list_item;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Override
    protected AnimationItem[] getAnimationItems() {
        return new AnimationItem[] {
                new AnimationItem("Scale fall bottom", R.anim.layout_animation_fall_down)
        };
    }

    @Override
    protected int getTypeLayout() {
        return 4;
    }
}
