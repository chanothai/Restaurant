package com.company.zicure.baseapplication.utility;

import com.company.zicure.baseapplication.models.IngredientModel;
import com.company.zicure.baseapplication.models.MealModel;
import com.company.zicure.baseapplication.models.StoreListItem;

import java.util.ArrayList;

/**
 * Created by macintosh on 14/3/18.
 */

public class ModelCart {
    private static ModelCart me = null;
    private ArrayList<MealModel> mealModel = null;
    private ArrayList<MealModel> condimentModel = null;
    private ArrayList<MealModel> ingredientModel = null;
    private ArrayList<MealModel> resultMealModel = null;

    private ArrayList<MealModel> sumCondiment = null;

    private ArrayList<IngredientModel> scanMealModel = null;

    private ArrayList<MealModel> arrListItem = null;

    private int pageView = 0;

    public ModelCart() {
        mealModel = new ArrayList<>();
        condimentModel = new ArrayList<>();
        ingredientModel = new ArrayList<>();
        arrListItem = new ArrayList<>();
        resultMealModel = new ArrayList<>();

        scanMealModel = new ArrayList<>();
        sumCondiment = new ArrayList<>();
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

    public ArrayList<MealModel> getIngredientModel() {
        return ingredientModel;
    }

    public void setIngredientModel(ArrayList<MealModel> ingredientModel) {
        this.ingredientModel = ingredientModel;
    }

    public ArrayList<MealModel> getArrListItem() {
        return arrListItem;
    }

    public ArrayList<MealModel> getResultMealModel() {
        return resultMealModel;
    }

    public void setResultMealModel(ArrayList<MealModel> resultMealModel) {
        this.resultMealModel = resultMealModel;
    }

    public ArrayList<IngredientModel> getScanMealModel() {
        return scanMealModel;
    }

    public void setScanMealModel(ArrayList<IngredientModel> scanMealModel) {
        this.scanMealModel = scanMealModel;
    }

    public int getPageView() {
        return pageView;
    }

    public void setPageView(int pageView) {
        this.pageView = pageView;
    }

    public ArrayList<MealModel> getSumCondiment() {
        return sumCondiment;
    }

    public void setSumCondiment(ArrayList<MealModel> sumCondiment) {
        this.sumCondiment = sumCondiment;
    }
}
