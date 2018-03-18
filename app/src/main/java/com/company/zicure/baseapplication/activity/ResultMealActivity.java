package com.company.zicure.baseapplication.activity;

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

    public void connectPrinter(){
        try{
            portName = ManagerPrint.getFirstPrinter("BT:");
            Log.d("Printer", portName);

            port = StarIOPort.getPort(portName, "", 10000, this);
            StarPrinterStatus status = ManagerPrint.getInstance(this, portName, "").getStatus(port);
            if (!status.offline) {
                Log.d(LOG, "Connect was correct");
                //Print online
            }else{
                Log.d(LOG, "Connect was incorrect");
            }
        }catch (StarIOPortException e){
            e.printStackTrace();
        }
    }

    public void startPrint() throws StarIOPortException {
        if (port != null) {
            byte[] commands;

            PrinterSetting setting = new PrinterSetting(this);
            StarIoExt.Emulation emulation = setting.getEmulation();

            ILocalizeReceipts localizeReceipts = ILocalizeReceipts.createLocalizeReceipts(0, 832);
            commands = PrinterFunctions.createCouponData(emulation, localizeReceipts, getResources(), 832, ICommandBuilder.BitmapConverterRotation.Normal);
            Communication.sendCommands(this, commands, port, this, mCallback);

//            ManagerPrint.SendCommand(this, portName, "", commands);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ModelCart.getInstance().getArrListItem().clear();
        ModelCart.getInstance().getResultMealModel().clear();
        ModelCart.getInstance().getMealModel().clear();

        if (port != null){
            try{
                //Port close
                StarIOPort.releasePort(port);
                Log.d(LOG, "Printer was close");
            } catch (StarIOPortException e){
                e.printStackTrace();
            }
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
