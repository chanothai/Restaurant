package com.company.zicure.baseapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.company.zicure.baseapplication.models.MealModel;
import com.company.zicure.baseapplication.utility.ModelCart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Pakgon on 11/7/2017 AD.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, DBManager.getInstance().getDB_NAME(), null, DBManager.getInstance().getDB_VERSION());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DBManager.getInstance().getTablemeal());
        onCreate(db);
    }
}
