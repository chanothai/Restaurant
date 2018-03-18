package com.company.zicure.baseapplication.printer;

import android.content.Context;
import android.util.Log;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ballomo on 3/18/2018 AD.
 */

public class ManagerPrint {
    /**
     * This function checks the status of the printer
     * @param context - Activity for displaying messages to the user
     * @param portName - Port name to use for communication. This should be (TCP:<IPAddress>)
     * @param portSetting - Should be blank
     */

    private static ManagerPrint me = null;
    private Context context = null;
    private String portName = null;
    private String portSetting = null;

    private StarIOPort port = null;

    private String LOG = "Printer";

    public ManagerPrint(Context context, String portName, String portSetting) {
        this.context = context;
        this.portName = portName;
        this.portSetting = portSetting;
    }

    public static ManagerPrint getInstance(Context context, String portName, String portSetting) {
        if (me == null) {
            me = new ManagerPrint(context, portName, portSetting);
        }
        return me;
    }

    public StarPrinterStatus getStatus(StarIOPort port){
        StarPrinterStatus status = null;

        try {
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            status = port.retreiveStatus();
        }catch (StarIOPortException e){
            e.printStackTrace();
        }

        return status;
    }

    /**
     * Using the portNameSearch parameter, this method searches for the first printer that corresponds to the
     * search
     * @param portNameSearch the name of the printer to search (example BT: or TCP:xxx.xxx.xxx.xxx)
     * @return
     */

    public static String getFirstPrinter(String portNameSearch) {
        String portName = "";
        ArrayList<PortInfo> portList;
        try {
            portList  = StarIOPort.searchPrinter(portNameSearch);

            for (PortInfo portInfo : portList) {
                portName = portInfo.getPortName();
                break;
            }
        } catch (StarIOPortException e) {
            e.printStackTrace();
        }
        return portName;
    }


    private static byte[] convertFromListByteArrayTobyteArray(List<Byte> ByteArray) {
        byte[] byteArray = new byte[ByteArray.size()];
        for(int index = 0; index < byteArray.length; index++) {
            byteArray[index] = ByteArray.get(index);
        }

        return byteArray;
    }

    public static void CopyArray(byte[] srcArray, Byte[] cpyArray) {
        for (int index = 0; index < cpyArray.length; index++) {
            cpyArray[index] = srcArray[index];
        }
    }

    public static void SendCommand(Context context, String portName, String portSettings, byte[] commandToSendToPrinter) throws StarIOPortException {
        StarIOPort port = null;
        try {
            /*
                using StarIOPort3.1.jar (support USB Port)
                Android OS Version: upper 2.2
            */
            port = StarIOPort.getPort(portName, portSettings, 10000, context);
            /*
                using StarIOPort.jar
                Android OS Version: under 2.1
                port = StarIOPort.getPort(portName, portSettings, 10000);
            */
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) { }

            /*
               Using Begin / End Checked Block method
               When sending large amounts of raster data,
               adjust the value in the timeout in the "StarIOPort.getPort"
               in order to prevent "timeout" of the "endCheckedBlock method" while a printing.
               *If receipt print is success but timeout error occurs(Show message which is "There was no response of the printer within the timeout period."),
                 need to change value of timeout more longer in "StarIOPort.getPort" method. (e.g.) 10000 -> 30000
             */
            StarPrinterStatus status = port.retreiveStatus();
//          StarPrinterStatus status = port.beginCheckedBlock();

            if (status.offline) {
                throw new StarIOPortException("A printer is offline");
            }

            port.writePort(commandToSendToPrinter, 0, commandToSendToPrinter.length);

//          status = port.endCheckedBlock();

            if (status.coverOpen) {
                throw new StarIOPortException("Printer cover is open");
            }
            else if (status.receiptPaperEmpty) {
                throw new StarIOPortException("Receipt paper is empty");
            }
            else if (status.offline) {
                throw new StarIOPortException("Printer is offline");
            }
        }
        catch (StarIOPortException e) {
            // Bubbling the exception up
            throw e;
        }
        finally {
            if (port != null) {
                try {
                    StarIOPort.releasePort(port);
                }
                catch (StarIOPortException e) { }
            }
        }
    }
}
