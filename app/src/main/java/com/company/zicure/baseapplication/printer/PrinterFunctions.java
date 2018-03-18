package com.company.zicure.baseapplication.printer;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;

import static com.starmicronics.starioextension.ICommandBuilder.CutPaperAction;
import static com.starmicronics.starioextension.StarIoExt.Emulation;

public class PrinterFunctions {

    public static byte[] createTextReceiptData(Emulation emulation, ILocalizeReceipts localizeReceipts, boolean utf8) {
        ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);

        builder.beginDocument();

        localizeReceipts.append2inchTextReceiptData(builder, utf8);

        builder.appendCutPaper(CutPaperAction.PartialCutWithFeed);

        builder.endDocument();

        return builder.getCommands();
    }

    public static byte[] createCouponData(Emulation emulation, ILocalizeReceipts localizeReceipts, Resources resources, int width, ICommandBuilder.BitmapConverterRotation rotation) {
        ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);

        builder.beginDocument();

        Bitmap image = localizeReceipts.createCouponImage(resources);

        builder.appendBitmap(image, false, width, true, rotation);

        builder.appendCutPaper(CutPaperAction.PartialCutWithFeed);

        builder.endDocument();

        return builder.getCommands();
    }
}
