package com.company.zicure.baseapplication.localizereceipts;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.printer.ILocalizeReceipts;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.ICommandBuilder.AlignmentPosition;
import com.starmicronics.starioextension.ICommandBuilder.BarcodeSymbology;
import com.starmicronics.starioextension.ICommandBuilder.BarcodeWidth;
import com.starmicronics.starioextension.ICommandBuilder.CodePageType;
import com.starmicronics.starioextension.ICommandBuilder.InternationalType;
import com.starmicronics.starioextension.StarIoExt.CharacterCode;


import java.nio.charset.Charset;

public class EnglishReceiptsImpl extends ILocalizeReceipts {
    public EnglishReceiptsImpl() {
        mLanguageCode = "En";

        mCharacterCode = CharacterCode.Standard;
    }

    @Override
    public void append2inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        }
        else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY    Time:HH:MM PM\n" +
                        "--------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.append((
                "SKU         Description    Total\n" +
                "300678566   PLAIN T-SHIRT  10.99\n" +
                "300692003   BLACK DENIM    29.99\n" +
                "300651148   BLUE DENIM     29.99\n" +
                "300642980   STRIPED DRESS  49.99\n" +
                "300638471   BLACK BOOTS    35.99\n" +
                "\n" +
                "Subtotal                  156.95\n" +
                "Tax                         0.00\n" +
                "--------------------------------\n").getBytes(encoding));

        builder.append(("Total     ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "--------------------------------\n" +
                "\n" +
                "Charge\n" +
                "156.95\n" +
                "Visa XXXX-XXXX-XXXX-0123\n" +
                "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public Bitmap createCouponImage(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.food3);
    }
}
