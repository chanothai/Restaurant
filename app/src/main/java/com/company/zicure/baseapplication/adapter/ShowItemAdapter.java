package com.company.zicure.baseapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.customview.LabelView;
import com.company.zicure.baseapplication.models.MealModel;
import com.company.zicure.baseapplication.utility.ModelCart;

import java.util.ArrayList;

/**
 * Created by macintosh on 16/3/18.
 */

public class ShowItemAdapter extends RecyclerView.Adapter<ShowItemAdapter.ShowItemViewHolder> {
    private Context context = null;
    private int typeLayout = 0;

    public ShowItemAdapter(Context context, int typeLayout) {
        this.context = context;
        this.typeLayout = typeLayout;
    }

    @Override
    public ShowItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_model_show_item, parent, false);
        return new ShowItemAdapter.ShowItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShowItemViewHolder holder, int position) {
        int imgID = context.getResources().getIdentifier(getArrModel().get(position).getImageName(),
                "drawable",
                context.getPackageName());
        holder.itemIMG.setImageResource(imgID);
        holder.itemName.setText(getArrModel().get(position).getName());
    }

    @Override
    public int getItemCount() {
        switch (typeLayout) {
            case 1:
                return ModelCart.getInstance().getMealModel().size();
            case 2:
                return ModelCart.getInstance().getCondimentModel().size();
        }
        return 0;
    }

    private ArrayList<MealModel> getArrModel() {
        if (typeLayout == 1) {
            return ModelCart.getInstance().getMealModel();
        }else {
            return ModelCart.getInstance().getCondimentModel();
        }
    }

    static class ShowItemViewHolder extends RecyclerView.ViewHolder {
        public LabelView itemName;
        public ImageView itemIMG;
        public ShowItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemIMG = itemView.findViewById(R.id.item_img);
        }
    }
}
