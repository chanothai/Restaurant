package com.company.zicure.baseapplication.database;

/**
 * Created by macintosh on 14/3/18.
 */

public class DBManager {
    private static DBManager me = null;
    private String Tablemeal = null, mealName = null, mealDetail = null, mealQRcode = null, mealImageName = null;
    private String TableIngredient = null, nameIGD = null, imageNameIGD = null;

    private String DB_NAME = "makro.db";
    private int DB_VERSION = 3;
    
    public DBManager() {
        initTablemeal();
        initTableIngredient();
    }
    
    public static DBManager getInstance() {
        if (me == null) {
            me = new DBManager();
        }
        
        return me;
    }

    private void initTablemeal(){
        Tablemeal = "meal_master";
        mealName = "name";
        mealDetail = "description";
        mealQRcode = "qrcode";
        mealImageName = "image_name";
    }

    private void initTableIngredient(){
        TableIngredient = "ingredients";
        nameIGD = "name";
        imageNameIGD = "image_name";
    }

    public String getTablemeal() {
        return Tablemeal;
    }

    public String getmealName() {
        return mealName;
    }

    public String getmealDetail() {
        return mealDetail;
    }

    public String getmealQRcode() {
        return mealQRcode;
    }

    public String getmealImageName() {
        return mealImageName;
    }

    public String getDB_NAME() {
        return DB_NAME;
    }

    public int getDB_VERSION() {
        return DB_VERSION;
    }

    public String getTableIngredient() {
        return TableIngredient;
    }

    public String getNameIGD() {
        return nameIGD;
    }

    public String getImageNameIGD() {
        return imageNameIGD;
    }

}
