package com.company.zicure.baseapplication.utility;

import com.company.zicure.baseapplication.models.MealModel;

import java.util.ArrayList;

/**
 * Created by macintosh on 14/3/18.
 */

public class ModelCart {
    private static ModelCart me = null;
    private ArrayList<MealModel> mealModel = null;
    private ArrayList<MealModel> condimentModel = null;

    public ModelCart() {
        mealModel = new ArrayList<>();
        condimentModel = new ArrayList<>();
    }

    public static ModelCart getInstance() {
        if (me == null) {
            me = new ModelCart();
        }

        return me;
    }
    public ArrayList<MealModel> getMealModel() {
        return mealModel;
    }

    public void setMealModel(ArrayList<MealModel> mealModel) {
        this.mealModel = mealModel;
    }

    public void setCondimentModel(ArrayList<MealModel> condimentModel) {
        this.condimentModel = condimentModel;
    }

    public ArrayList<MealModel> getCondimentModel() {
        return condimentModel;
    }
}
