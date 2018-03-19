package com.company.zicure.baseapplication.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.company.zicure.baseapplication.R;
import com.company.zicure.baseapplication.common.BaseActivity;
import com.company.zicure.baseapplication.fragment.FragmentShowProduct;
import com.company.zicure.baseapplication.models.MealModel;
import com.company.zicure.baseapplication.printer.Communication;
import com.company.zicure.baseapplication.printer.ILocalizeReceipts;
import com.company.zicure.baseapplication.printer.ManagerPrint;
import com.company.zicure.baseapplication.printer.PrinterFunctions;
import com.company.zicure.baseapplication.printer.PrinterSetting;
import com.company.zicure.baseapplication.utility.ModelCart;
import com.jgabrielfreitas.core.BlurImageView;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;
import com.starmicronics.starioextension.StarIoExt.Emulation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ResultMealActivity extends BaseActivity {

    private ArrayList<MealModel> arrMeal = null;
    private ArrayList<String> name = null;

    //properties
    private String LOG = "Printer";
    private boolean mIsForeground;

    private StarIOPort port = null;
    private String portName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_meal);
        bindView();
        connectPrinter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsForeground = false;
    }

    private void bindView(){
        BlurImageView blurImageView = findViewById(R.id.bg_image_food);
        blurImageView.setBlur(3);

        if (ModelCart.getInstance().getArrListItem().size() > 1) {
            scanData();
        }

        callResultItem();
    }

    private void connectPrinter(){
        try{
            portName = ManagerPrint.getFirstPrinter("BT:");
            Log.d("Printer", portName);

            port = StarIOPort.getPort(portName, "", 10000, this);

        }catch (StarIOPortException e){
            e.printStackTrace();
        }
    }

    public void startPrint() {
//        try{
            if (port != null) {
                byte[] commands;
//                ILocalizeReceipts localizeReceipts = ILocalizeReceipts.createLocalizeReceipts(0, 832);
//                commands = PrinterFunctions.createCouponData(emulation, localizeReceipts, getResources(), 832, ICommandBuilder.BitmapConverterRotation.Normal);
//                Communication.sendCommands(this, commands, port, this, mCallback);
                commands = createData(Emulation.StarGraphic, 832, this);
                Communication.sendCommands(this, commands, port.getPortName(), port.getPortSettings(), 10000, this, mCallback);

//                StarPrinterStatus statusPrint = port.beginCheckedBlock();
//
//                if (statusPrint.offline) {
//                    throw new StarIOPortException("A printer is offline.");
//                }
//                commands = createData(emulation, 832, this);
//
//
//                port.writePort(commands, 0, commands.length);
//
//                port.setEndCheckedBlockTimeoutMillis(30000);
//
//                statusPrint = port.endCheckedBlock();
//
//                if (statusPrint.coverOpen) {
//                    throw new StarIOPortException("Printer cover is open");
//                } else if (statusPrint.receiptPaperEmpty) {
//                    throw new StarIOPortException("Receipt paper is empty");
//                } else if (statusPrint.offline) {
//                    throw new StarIOPortException("Printer is offline");
//                }
            }
//        }catch (StarIOPortException e){
//            e.printStackTrace();
//        }
    }

    public static byte[] createQrCodeData(Emulation emulation) {
        byte[] data;

        try {
            data = "Hello World.\n".getBytes("UTF-8");      // Use UTF-8 encoded text data for QR Code.
        }
        catch (UnsupportedEncodingException e) {
            data = "Hello World.\n".getBytes();
        }

        ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);

        builder.beginDocument();

        builder.append("*Cell:2*\n".getBytes());
        builder.appendQrCode(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.L, 2);
        builder.appendUnitFeed(32);
        builder.append("*Cell:8*\n".getBytes());
        builder.appendQrCode(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.L, 8);
        builder.appendUnitFeed(32);

        builder.append("*Level:L*\n".getBytes());
        builder.appendQrCode(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.L, 4);
        builder.appendUnitFeed(32);
        builder.append("*Level:M*\n".getBytes());
        builder.appendQrCode(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.M, 4);
        builder.appendUnitFeed(32);
        builder.append("*Level:Q*\n".getBytes());
        builder.appendQrCode(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.Q, 4);
        builder.appendUnitFeed(32);
        builder.append("*Level:H*\n".getBytes());
        builder.appendQrCode(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.H, 4);
        builder.appendUnitFeed(32);

        builder.append("\n*AbsolutePosition:40*\n".getBytes());
        builder.appendQrCodeWithAbsolutePosition(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.L, 4, 40);
        builder.appendUnitFeed(32);

        builder.append("\n*Alignment:Center*\n".getBytes());
        builder.appendQrCodeWithAlignment(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.L, 4, ICommandBuilder.AlignmentPosition.Center);
        builder.appendUnitFeed(32);
        builder.append("\n*Alignment:Right*\n".getBytes());
        builder.appendQrCodeWithAlignment(data, ICommandBuilder.QrCodeModel.No2, ICommandBuilder.QrCodeLevel.L, 4, ICommandBuilder.AlignmentPosition.Right);
        builder.appendUnitFeed(32);

        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed);

        builder.endDocument();

        return builder.getCommands();
    }

    public static byte[] createData(Emulation emulation, int width, Context context) {
        Bitmap starLogoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.qr1);

        ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);

        builder.beginDocument();

        builder.append("\n*Normal*\n".getBytes());
        builder.appendBitmap(starLogoBitmap, true);

        builder.append("\n*width:Full, bothScale:true*\n".getBytes());
        builder.appendBitmap(starLogoBitmap, true, width, true);
        builder.append("\n*width:Full, bothScale:false*\n".getBytes());
        builder.appendBitmap(starLogoBitmap, true, width, false);

        builder.append("\n*Rotate180*\n".getBytes());
        builder.appendBitmap(starLogoBitmap, true, ICommandBuilder.BitmapConverterRotation.Rotate180);

        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed);

        builder.endDocument();

        return builder.getCommands();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ModelCart.getInstance().getArrListItem().clear();
        ModelCart.getInstance().getResultMealModel().clear();
        ModelCart.getInstance().getMealModel().clear();
        ModelCart.getInstance().setPageView(0);

        try{
            //Port close
            StarIOPort.releasePort(port);
        }catch (StarIOPortException e) {
            e.printStackTrace();
        }
    }

    private void scanData(){
        arrMeal = ModelCart.getInstance().getMealModel();

        for (int i = 0; i < ModelCart.getInstance().getMealModel().size(); i++){

            name = new ArrayList<>();

            for (int j = 0; j < ModelCart.getInstance().getMealModel().size(); j++){
                MealModel mealModel = new MealModel();
                mealModel.setName(ModelCart.getInstance().getMealModel().get(j).getName());
                mealModel.setQrcode(ModelCart.getInstance().getMealModel().get(j).getQrcode());
                mealModel.setImageName(ModelCart.getInstance().getMealModel().get(j).getImageName());

                if (ModelCart.getInstance().getMealModel().get(i).getName().equalsIgnoreCase(
                        ModelCart.getInstance().getMealModel().get(j).getName())) {
                    name.add(ModelCart.getInstance().getMealModel().get(i).getName());

                    Log.d("QUERY_DATA", name.toString());
                    if (name.size() > 1) {
                        arrMeal.remove(j);
                    }
                }
            }
        }

        ModelCart.getInstance().setMealModel(arrMeal);
    }


    private void callResultItem(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new FragmentShowProduct(), "Fragment_show_product");
        transaction.commit();
    }

    private final Communication.SendCallback mCallback = new Communication.SendCallback() {
        @Override
        public void onStatus(boolean result, Communication.Result communicateResult) {
            if (!mIsForeground) {
                return;
            }

            String msg;

            switch (communicateResult) {
                case Success:
                    msg = "Success!";
                    break;
                case ErrorOpenPort:
                    msg = "Fail to openPort";
                    break;
                case ErrorBeginCheckedBlock:
                    msg = "Printer is offline (beginCheckedBlock)";
                    break;
                case ErrorEndCheckedBlock:
                    msg = "Printer is offline (endCheckedBlock)";
                    break;
                case ErrorReadPort:
                    msg = "Read port error (readPort)";
                    break;
                case ErrorWritePort:
                    msg = "Write port error (writePort)";
                    break;
                default:
                    msg = "Unknown error";
                    break;
            }
            Toast.makeText(ResultMealActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };
}
