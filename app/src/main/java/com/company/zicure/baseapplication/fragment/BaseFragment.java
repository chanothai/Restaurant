package com.company.zicure.baseapplication.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.activity.MainActivity;
import com.company.zicure.baseapplication.activity.ResultMealActivity;
import com.company.zicure.baseapplication.activity.ShowItemActivity;
import com.company.zicure.baseapplication.adapter.ItemOffsetDecoration;
import com.company.zicure.baseapplication.adapter.ShowItemAdapter;
import com.company.zicure.baseapplication.customview.ButtonView;
import com.company.zicure.baseapplication.customview.LabelView;
import com.company.zicure.baseapplication.database.DBHelper;
import com.company.zicure.baseapplication.database.DBManager;
import com.company.zicure.baseapplication.models.AnimationItem;
import com.company.zicure.baseapplication.models.IngredientModel;
import com.company.zicure.baseapplication.models.MealModel;
import com.company.zicure.baseapplication.utility.ModelCart;
import com.google.gson.Gson;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    // View
    private RecyclerView mRecyclerView;
    private AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;
    private ButtonView mBtnView, mBtnCancel;
    private LabelView labelView;

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
        bindView(view);

        setupRecyclerView();
        runLayoutAnimation(mRecyclerView, mSelectedItem);
    }

    private void bindView(View view){
        mAnimationItems = getAnimationItems();
        mSelectedItem = mAnimationItems[0];

        labelView = view.findViewById(R.id.label_title);
        if (getTypeLayout() == 1) {
            labelView.setText("เมนูอาหาร");
        }else if (getTypeLayout() == 3){
            labelView.setText("เริ่มเลือกวัตถุดิบ");
        }else if (getTypeLayout() == 2){
            labelView.setText("เริ่มเลือกเครื่องปรุง");
        }

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mBtnView = view.findViewById(R.id.btn_confirm);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        if (mBtnView != null && mBtnCancel != null) {
            mBtnView.setOnClickListener(this);
            mBtnCancel.setOnClickListener(this);
        }
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

    public void getUpdateItem(){
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
    }

    @Override
    public void onClick(View v) {
        String name = mBtnView.getText().toString();

        if (v.getId() == R.id.btn_confirm) {
            if (name.equalsIgnoreCase("ถัดไป")) {
                if (ModelCart.getInstance().getArrListItem().size() > 0) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_list,  new FragmentShowCondiment(), "Fragment_show_condiment");
                    transaction.commit();
                }

                mBtnView.setText(R.string.txt_btn_confirm);
            }else{
                queryResultMeal();
            }
        }else {
            if (name.equalsIgnoreCase("ถัดไป")){
                getActivity().finish();
            }else{
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container_list, new FragmentShowProduct(), "Fragment_show_product");
                transaction.commit();
            }
        }
    }

    private void queryResultMeal(){
        DBHelper mHelper = new DBHelper(getContext());
        SQLiteDatabase sqlDatabase = mHelper.getWritableDatabase();

        resetMeal();

        queryFoodID(sqlDatabase);
    }

    private void queryFoodID(SQLiteDatabase sqlDatabase){
        Cursor mCursor;
        for (int i = 0; i < ModelCart.getInstance().getArrListItem().size(); i++) {
            String query = "SELECT * FROM " + DBManager.getInstance().getTableIngredient() + " WHERE name = '"
                    + ModelCart.getInstance().getArrListItem().get(i).getName() + "'";
            mCursor = sqlDatabase.rawQuery(query, null);
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                ModelCart.getInstance().getResultMealModel().add(setMealModel(mCursor, 1));


                String name = new Gson().toJson(ModelCart.getInstance().getResultMealModel());
                Log.d("DB_QUERY", name);

                mCursor.moveToNext();
            }
        }

        queryMeal(sqlDatabase);
    }

    private void queryMeal(SQLiteDatabase sqlDatabase){
        Cursor mCursor;
        for (int i = 0; i < ModelCart.getInstance().getResultMealModel().size(); i++){
            mCursor = sqlDatabase.rawQuery("SELECT * FROM " + DBManager.getInstance().getTablemeal()
                    + " WHERE _id = '" + ModelCart.getInstance().getResultMealModel().get(i).getFoodID() + "'", null);

            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                ModelCart.getInstance().getMealModel().add(setMealModel(mCursor,0));

                String name = new Gson().toJson(ModelCart.getInstance().getMealModel());
                Log.d("DB_QUERY", name);

                mCursor.moveToNext();
            }
        }

        ((ShowItemActivity) getActivity()).openActivity(ResultMealActivity.class, true);
    }

    private MealModel setMealModel(Cursor mCursor, int type){
        MealModel mealModel = new MealModel();
        mealModel.setName(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealName())));
        mealModel.setImageName(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealImageName())));

        if (type == 0){
            mealModel.setQrcode(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealQRcode())));
        }else{
            mealModel.setFoodID(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getMealFoodID())));
        }

        return mealModel;
    }

    private void resetMeal(){
        ModelCart.getInstance().getMealModel().clear();
        ModelCart.getInstance().getIngredientModel().clear();
        ModelCart.getInstance().getCondimentModel().clear();
    }
}
