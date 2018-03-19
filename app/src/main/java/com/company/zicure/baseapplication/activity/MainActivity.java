package com.company.zicure.baseapplication.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.adapter.MainMenuAdapter;
import com.company.zicure.baseapplication.common.BaseActivity;
import com.company.zicure.baseapplication.database.DBHelper;
import com.company.zicure.baseapplication.database.DBManager;
import com.company.zicure.baseapplication.models.MealModel;
import com.company.zicure.baseapplication.utility.ModelCart;

import com.jgabrielfreitas.core.BlurImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends BaseActivity {

    //Properties
    private DBHelper mHelper = null;
    private SQLiteDatabase sqlDatabase = null;
    private Cursor mCursor = null;

    //View
    private BlurImageView imageBg = null;
    private RelativeLayout layoutIngredient = null;
    private Button btnMealMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        checkDataBase();

        PowerManager.WakeLock wl = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        wl.acquire();

        if (savedInstanceState == null) {
            mHelper = new DBHelper(this);
            sqlDatabase = mHelper.getWritableDatabase();
        }
    }

    private void bindView(){
        imageBg = findViewById(R.id.bg_image_food);
        imageBg.setBlur(3);
        layoutIngredient = findViewById(R.id.layout_title);
        layoutIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("view_pattern", 2);
                queryIngredient();
                openActivity(ShowItemActivity.class, bundle);
            }
        });

        btnMealMenu = findViewById(R.id.btn_meal_menu);
        btnMealMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("view_pattern", 0);
                queryMeal();

                openActivity(ShowItemActivity.class, bundle);
            }
        });
    }

    private void runAnimation(RecyclerView recyclerMenu){
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerMenu.getContext(), R.anim.layout_animation_fall_down);
        recyclerMenu.setLayoutAnimation(controller);
        recyclerMenu.getAdapter().notifyDataSetChanged();
        recyclerMenu.scheduleLayoutAnimation();
    }

    public void queryIngredient() {
        if (ModelCart.getInstance().getIngredientModel().size() == 0) {
            mCursor = sqlDatabase.rawQuery("SELECT * FROM " + DBManager.getInstance().getTableIngredient() + " WHERE type = 0", null);
            mCursor.moveToFirst();

            while (!mCursor.isAfterLast()) {
                MealModel mealModel = new MealModel();
                mealModel.setName(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealName())));
                mealModel.setActive(false);
                mealModel.setFoodID(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getMealFoodID())));

                if (ModelCart.getInstance().getIngredientModel().size() < 24){
                    ModelCart.getInstance().getIngredientModel().add(mealModel);
                }

                mCursor.moveToNext();
            }
        }
    }

    public void queryMeal() {
        if (ModelCart.getInstance().getMealModel().size() == 0) {
            mCursor = sqlDatabase.rawQuery("SELECT * FROM " + DBManager.getInstance().getTablemeal(), null);
            mCursor.moveToFirst();

            while (!mCursor.isAfterLast()) {
                MealModel mealModel = new MealModel();
                mealModel.setName(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealName())));
                mealModel.setImageName(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealImageName())));
                mealModel.setQrcode(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealQRcode())));

                ModelCart.getInstance().getMealModel().add(mealModel);
                mCursor.moveToNext();
            }
        }
    }

    public void queryCondiment(){
        if (ModelCart.getInstance().getCondimentModel().size() == 0) {
            mCursor = sqlDatabase.rawQuery("SELECT * FROM " + DBManager.getInstance().getTableIngredient() + " WHERE type = 1", null);
            mCursor.moveToFirst();

            while (!mCursor.isAfterLast()) {
                MealModel mealModel = new MealModel();
                mealModel.setName(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealName())));
                mealModel.setImageName(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getmealImageName())));
                mealModel.setActive(false);
                mealModel.setFoodID(mCursor.getString(mCursor.getColumnIndex(DBManager.getInstance().getMealFoodID())));

                if (ModelCart.getInstance().getCondimentModel().size() < 6){
                    ModelCart.getInstance().getCondimentModel().add(mealModel);
                }
                mCursor.moveToNext();
            }
        }
    }

    public void checkDataBase(){
        String url = "/data/data/" + this.getPackageName() + "/databases/" + DBManager.getInstance().getDB_NAME();
        File file = new File(url);
        if (!file.exists()) {
            try{
                mHelper = new DBHelper(this);
                sqlDatabase = mHelper.getWritableDatabase();
                sqlDatabase.close();
                mHelper.close();
                InputStream in = this.getAssets().open(DBManager.getInstance().getDB_NAME());
                OutputStream out = new FileOutputStream(url);
                byte[] buffer = new byte[in.available()];
                in.read(buffer);
                out.write(buffer, 0, buffer.length);
                in.close();
                out.close();
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryMeal();
        queryCondiment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.close();
        sqlDatabase.close();
    }
}
