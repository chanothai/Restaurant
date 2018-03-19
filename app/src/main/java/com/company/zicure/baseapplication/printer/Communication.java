package com.company.zicure.baseapplication.printer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.stario.StarPrinterStatus;

/**
 * Created by ballomo on 3/18/2018 AD.
 */

public class Communication {
    public enum Result {
        Success,
        ErrorUnknown,
        ErrorOpenPort,
        ErrorBeginCheckedBlock,
        ErrorEndCheckedBlock,
        ErrorWritePort,
        ErrorReadPort,
    }

    public interface SendCallback {
        void onStatus(boolean result, Communication.Result communicateResult);
    }

    public static void sendCommands(Object lock, byte[] commands, StarIOPort port,Context context, Communication.SendCallback callback) {
        SendCommandThread thread = new SendCommandThread(lock, commands, port, context, callback);
        thread.start();
    }

    public static void sendCommands(Object lock, byte[] commands, String portName, String portSettings, int timeout, Context context, SendCallback callback) {
        SendCommandThread thread = new SendCommandThread(lock, commands, portName, portSettings, timeout, context, callback);
        thread.start();
    }
}

class SendCommandThread extends Thread {
    private final Object mLock;
    private Communication.SendCallback mCallback;
    private byte[] mCommands;

    private StarIOPort mPort;

    private String  mPortName = null;
    private String  mPortSettings;
    private int     mTimeout;
    private Context mContext;

    SendCommandThread(Object lock, byte[] commands, StarIOPort port, Context context, Communication.SendCallback callback) {
        mCommands = commands;
        mPort     = port;
        mCallback = callback;
        mLock     = lock;
        mContext      = context;
    }

    SendCommandThread(Object lock, byte[] commands, String portName, String portSettings, int timeout, Context context, Communication.SendCallback callback) {
        mCommands     = commands;
        mPortName     = portName;
        mPortSettings = portSettings;
        mTimeout      = timeout;
        mContext      = context;
        mCallback     = callback;
        mLock         = lock;
    }

    @Override
    public void run() {
        Communication.Result communicateResult = Communication.Result.ErrorOpenPort;
        boolean result = false;

        synchronized (mLock) {
            try {
                if (mPort == null) {

                    if (mPortName == null) {
                        resultSendCallback(false, communicateResult, mCallback);
                        return;
                    } else {
                        mPort = StarIOPort.getPort(mPortName, mPortSettings, mTimeout, mContext);
                    }
                }
                if (mPort == null) {
                    communicateResult = Communication.Result.ErrorOpenPort;
                    resultSendCallback(false, communicateResult, mCallback);
                    return;
                }

//              // When using USB interface with mPOP(F/W Ver 1.0.1), you need to send the following data.
//              byte[] dummy = {0x00};
//              mPort.writePort(dummy, 0, dummy.length);

                StarPrinterStatus status;

                communicateResult = Communication.Result.ErrorBeginCheckedBlock;

                status = mPort.beginCheckedBlock();

                if (status.offline) {
                    throw new StarIOPortException("A printer is offline.");
                }

                communicateResult = Communication.Result.ErrorWritePort;

                mPort.writePort(mCommands, 0, mCommands.length);

                communicateResult = Communication.Result.ErrorEndCheckedBlock;

                mPort.setEndCheckedBlockTimeoutMillis(30000);     // 30000mS!!!

                status = mPort.endCheckedBlock();

                if (status.coverOpen) {
                    throw new StarIOPortException("Printer cover is open");
                } else if (status.receiptPaperEmpty) {
                    throw new StarIOPortException("Receipt paper is empty");
                } else if (status.offline) {
                    throw new StarIOPortException("Printer is offline");
                }

                result = true;
                communicateResult = Communication.Result.Success;
            } catch (StarIOPortException e) {
                // Nothing
            }

            if (mPort != null && mPortName != null) {
                try {
                    StarIOPort.releasePort(mPort);
                } catch (StarIOPortException e) {
                    // Nothing
                }
                mPort = null;
            }

            resultSendCallback(result, communicateResult, mCallback);
        }
    }

    private static void resultSendCallback(final boolean result, final Communication.Result communicateResult, final Communication.SendCallback callback) {
        if (callback != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onStatus(result, communicateResult);
                }
            });
        }
    }
}
