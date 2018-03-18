package com.company.zicure.baseapplication.models;

/**
 * Created by ballomo on 3/18/2018 AD.
 */

public class IngredientModel {
    private String name;
    private String imageName;
    private String[] arrFoodID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String[] getArrFoodID() {
        return arrFoodID;
    }

    public void setArrFoodID(String[] arrFoodID) {
        this.arrFoodID = arrFoodID;
    }
}
