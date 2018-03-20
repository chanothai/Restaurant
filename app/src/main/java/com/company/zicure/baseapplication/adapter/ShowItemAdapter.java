package com.company.zicure.baseapplication.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.activity.MainActivity;
import com.company.zicure.baseapplication.activity.ResultMealActivity;
import com.company.zicure.baseapplication.activity.ShowItemActivity;
import com.company.zicure.baseapplication.customview.ButtonView;
import com.company.zicure.baseapplication.customview.LabelView;
import com.company.zicure.baseapplication.fragment.ListItemFragment;
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
    public void onBindViewHolder(final ShowItemViewHolder holder, final int position) {
        String nameItem = getArrModel().get(position).getName();
        Log.d("Data_Query", nameItem);
        holder.itemName.setText(nameItem);

        setVisibleImage(holder, position);

        if (typeLayout == 3){
            if (ModelCart.getInstance().getArrListItem().size() == 0) {
                getArrModel().get(position).setActive(false);
            }
        }else if (typeLayout == 2) {
            if (ModelCart.getInstance().getSumCondiment().size() == 0) {
                getArrModel().get(position).setActive(false);
            }
        }

        if (typeLayout == 1) {
            holder.linearLayout.setBackground(context.getDrawable(R.drawable.select_meal_item));
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ModelCart.getInstance().getMealModel().size() > 0) {
                        /***********/
                        int imgID = context.getResources().getIdentifier(getArrModel().get(position).getQrcode(),
                                "drawable",
                                context.getPackageName());
                        String name = getArrModel().get(position).getName();
                        createDialogPrint(name, imgID);
                    }
                }
            });
        } else if (typeLayout == 3 || typeLayout == 2) { // Show Ingredient, Show Condiment
            holder.itemName.setGravity(Gravity.CENTER);
            holder.itemName.setTextColor(Color.BLACK);

            setSelectActive(holder, position);
        }else if (typeLayout == 4){ // List Item
            int rang = position + 1;
            String data = rang + ". " + ModelCart.getInstance().getArrListItem().get(position).getName();
            holder.itemName.setText(data);
            holder.itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
            holder.itemIMG.setVisibility(View.GONE);
            holder.itemName.setGravity(Gravity.START|Gravity.BOTTOM);

            resizeLayout(holder);
        }
    }

    private void setVisibleImage(final ShowItemViewHolder holder, final int position){
        if (getArrModel().get(position).getImageName() != null) {
            int imgID = context.getResources().getIdentifier(getArrModel().get(position).getImageName(),
                    "drawable",
                    context.getPackageName());

            holder.itemIMG.setImageResource(imgID);
        }else{
            holder.itemIMG.setVisibility(View.GONE);
        }
    }

    private void createDialogPrint(String name, final int qrcode){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_custom);
        dialog.setCancelable(true);

        LabelView itemName = dialog.findViewById(R.id.name_item);
        itemName.setText("ชื่อ: " +name);

        ImageView imgQR = dialog.findViewById(R.id.img_qrcode);
        imgQR.setImageResource(qrcode);

        ButtonView btnPrint = dialog.findViewById(R.id.btn_print);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ResultMealActivity)context).startPrint(qrcode);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setSelectActive(final ShowItemViewHolder holder, final int position) {
        if (getArrModel().get(position).isActive()) {
            holder.itemName.setTextColor(Color.WHITE);
            holder.linearLayout.setBackgroundColor(Color.parseColor("#caff4444"));
        }else {
            holder.itemName.setTextColor(Color.BLACK);
            holder.linearLayout.setBackgroundColor(Color.WHITE);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArrModel().get(position).isActive()) {
                    if (ModelCart.getInstance().getArrListItem().size() > 0){
                        for (int i = 0; i < ModelCart.getInstance().getArrListItem().size(); i++){
                            if (ModelCart.getInstance().getArrListItem().get(i).getName().equalsIgnoreCase(getArrModel().get(position).getName())) {
                                ModelCart.getInstance().getArrListItem().remove(i);
                            }
                        }
                    }

                    if (typeLayout == 2) { // clear choose 1 item
                        if (ModelCart.getInstance().getSumCondiment().size() > 0) {
                            ModelCart.getInstance().getSumCondiment().clear();
                            holder.itemName.setTextColor(Color.BLACK);
                            holder.linearLayout.setBackgroundColor(Color.WHITE);
                            getArrModel().get(position).setActive(false);
                        }
                    }else{
                        holder.itemName.setTextColor(Color.BLACK);
                        holder.linearLayout.setBackgroundColor(Color.WHITE);
                        getArrModel().get(position).setActive(false);
                    }
                }else{

                    MealModel mealModel = new MealModel();
                    mealModel.setName(getArrModel().get(position).getName());
                    mealModel.setFoodID(getArrModel().get(position).getFoodID());

                    if (typeLayout == 2){
                        if (ModelCart.getInstance().getSumCondiment().size() == 0) {
                            ModelCart.getInstance().getSumCondiment().add(mealModel);
                            ModelCart.getInstance().getArrListItem().add(mealModel);

                            holder.itemName.setTextColor(Color.WHITE);
                            holder.linearLayout.setBackgroundColor(Color.parseColor("#caff4444"));
                            getArrModel().get(position).setActive(true);
                        }
                    }else{
                        getArrModel().get(position).setActive(true);
                        holder.itemName.setTextColor(Color.WHITE);
                        holder.linearLayout.setBackgroundColor(Color.parseColor("#caff4444"));
                        ModelCart.getInstance().getArrListItem().add(mealModel);
                    }
                }

                ((ShowItemActivity) context).updateListItem();
            }
        });
    }

    @Override
    public int getItemCount() {
        switch (typeLayout) {
            case 1:
                return ModelCart.getInstance().getMealModel().size();
            case 2:
                return ModelCart.getInstance().getCondimentModel().size();
            case 3:
                return ModelCart.getInstance().getIngredientModel().size();
            default:
                return ModelCart.getInstance().getArrListItem().size();
        }
    }

    private ArrayList<MealModel> getArrModel() {
        if (typeLayout == 1) {
            return ModelCart.getInstance().getMealModel();
        }else if (typeLayout == 2) {
            return ModelCart.getInstance().getCondimentModel();
        }else{
            return ModelCart.getInstance().getIngredientModel();
        }
    }

    private void resizeLayout(ShowItemViewHolder holder){
        CardView.LayoutParams params = (CardView.LayoutParams) holder.linearLayout.getLayoutParams();
        params.height = 100;
        holder.linearLayout.setLayoutParams(params);
    }

    static class ShowItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public LabelView itemName;
        public ImageView itemIMG;
        public CardView cardView;

        public ShowItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemIMG = itemView.findViewById(R.id.item_img);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}
