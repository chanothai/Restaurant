package com.company.zicure.baseapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.activity.MainActivity;
import com.company.zicure.baseapplication.activity.ShowItemActivity;
import com.company.zicure.baseapplication.customview.LabelView;

import java.util.List;

/**
 * Created by macintosh on 15/3/18.
 */

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MainMenuViewHolder> {

    private String[] arrTitle = {"อาหาร", "เครื่องปรุง", "เลือกวัตถุดิบ"};
    private Context context = null;

    public MainMenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MainMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_model_menu, parent, false);
        return new MainMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainMenuViewHolder holder, final int position) {
        switch (position) {
            case 0:
                holder.menuImage.setImageResource(R.drawable.bg_menu_meal);
                holder.menuTitle.setText(arrTitle[0]);
                break;
            case 1:
                holder.menuImage.setImageResource(R.drawable.bg_menu_condiment);
                holder.menuTitle.setText(arrTitle[1]);
                break;
            case 2:
                holder.menuImage.setImageResource(R.drawable.bg_menu_ingredient);
                holder.menuTitle.setText(arrTitle[2]);
                break;
        }

        holder.layoutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("view_pattern", 0);
                    ((MainActivity) context).openActivity(ShowItemActivity.class, bundle);
                }else if (position == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("view_pattern", 1);
                    ((MainActivity) context).openActivity(ShowItemActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrTitle.length;
    }

    public class MainMenuViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layoutTitle;
        public ImageView menuImage;
        public LabelView menuTitle;

        public MainMenuViewHolder(View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.img_menu);
            menuTitle = itemView.findViewById(R.id.title_menu);
            layoutTitle = itemView.findViewById(R.id.layout_title);
        }
    }
}
