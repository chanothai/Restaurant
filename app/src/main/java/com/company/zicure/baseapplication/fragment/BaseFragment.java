package com.company.zicure.baseapplication.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.adapter.ItemOffsetDecoration;
import com.company.zicure.baseapplication.adapter.ShowItemAdapter;
import com.company.zicure.baseapplication.models.AnimationItem;

public abstract class BaseFragment extends Fragment {

    // View
    private RecyclerView mRecyclerView;
    private AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;

    //Properties
    private int typeLayout = 0;

    public BaseFragment() {
        // Required empty public constructor
    }

    protected abstract int getLayoutResId();

    protected abstract RecyclerView.LayoutManager getLayoutManager(Context context);

    protected abstract AnimationItem[] getAnimationItems();

    protected abstract int getTypeLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAnimationItems = getAnimationItems();
        mSelectedItem = mAnimationItems[0];

        mRecyclerView = view.findViewById(R.id.recycler_view);

        setupRecyclerView();
        runLayoutAnimation(mRecyclerView, mSelectedItem);
    }

    private void setupRecyclerView(){
        int spacing = getResources().getDimensionPixelOffset(R.dimen.default_spacing_small);
        Context context = mRecyclerView.getContext();
        mRecyclerView.setLayoutManager(getLayoutManager(context));
        mRecyclerView.setAdapter(new ShowItemAdapter(getActivity(), getTypeLayout()));
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(spacing));
    }

    private void runLayoutAnimation(final RecyclerView recyclerView, final AnimationItem item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, item.getResourceId());

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
