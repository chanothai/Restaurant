package com.company.zicure.baseapplication.printer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.company.zicure.baseapplication.localizereceipts.EnglishReceiptsImpl;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt.CharacterCode;

public abstract class ILocalizeReceipts {
    private int mPaperSize;
    private int mLanguage;

    protected String        mLanguageCode;
    private   String        mPaperSizeStr;
    private   String        mScalePaperSizeStr;
    protected CharacterCode mCharacterCode;

    public static ILocalizeReceipts createLocalizeReceipts(int language, int paperSize) {
        ILocalizeReceipts localizeReceipts ;
        localizeReceipts = new EnglishReceiptsImpl();

        localizeReceipts.setPaperSizeStr("4\"");
        localizeReceipts.setScalePaperSizeStr("3\"");
        localizeReceipts.setLanguage(language);
        localizeReceipts.setPaperSize(paperSize);

        return localizeReceipts;
    }

    public abstract Bitmap createCouponImage(Resources resources);
    public abstract void append2inchTextReceiptData(ICommandBuilder builder, boolean utf8);

    public int getLanguage() {
        return mLanguage;
    }

    public void setLanguage(int language) {
        mLanguage = language;
    }

    public void setPaperSize(int paperSize) {
        mPaperSize = paperSize;
    }

    public String getLanguageCode() {
        return mLanguageCode;
    }

    public String getPaperSizeStr() {
        return mPaperSizeStr;
    }

    public void setPaperSizeStr(String paperSizeStr){
        mPaperSizeStr = paperSizeStr;
    }

    public String getScalePaperSizeStr() {
        return mScalePaperSizeStr;
    }

    public void setScalePaperSizeStr(String scalePaperSizeStr){
        mScalePaperSizeStr = scalePaperSizeStr;
    }
}
