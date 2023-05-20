package kr.taemin.android.usbserial.func;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import kr.taemin.android.usbserial.driver.UsbSerialDriver;
import kr.taemin.android.usbserial.driver.UsbSerialPort;
import kr.taemin.android.usbserial.driver.UsbSerialProber;
import kr.taemin.android.usbserial.util.HexDump;
import kr.taemin.android.usbserial.util.SerialInputOutputManager;
import kr.taemin.android.usbserial.driver.CustomProber;

import java.io.IOException;

public class Board {
    Context mContext;

    public Board(Context context) {
        this.mContext = context;
        mainLooper = new Handler(Looper.getMainLooper());;
    }

    private enum UsbPermission { Unknown, Requested, Granted, Denied }

    private static final int WRITE_WAIT_MILLIS = 2000;
    private static final int READ_WAIT_MILLIS = 2000;

    private int deviceId, portNum;
    private int baudRate = 921600;

    private final Handler mainLooper;

    private SerialInputOutputManager usbIoManager;
    private UsbSerialPort usbSerialPort;
    private UsbPermission usbPermission = UsbPermission.Unknown;
    private boolean connected = false;

    private void connect() {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        for(UsbDevice v : usbManager.getDeviceList().values())
            if(v.getDeviceId() == deviceId)
                device = v;
        if(device == null) {
            Log.e("Error", "connection failed: device not found");
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if(usbConnection == null && usbPermission == UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("kr.taemin.android.usbserial.examples.GRANT_USB"), flags);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if(usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
                Log.e("error","connection failed: permission denied");
            else
                Log.e("error", "connection failed: open failed");
            return;
        }

        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(baudRate, 8, 1, UsbSerialPort.PARITY_NONE);

            Log.i("info","connected");
            connected = true;
        } catch (Exception e) {
            Log.e("error", "connection failed: " + e.getMessage());
            disconnect();
        }
    }

    private void disconnect() {
        connected = false;
        if(usbIoManager != null) {
            usbIoManager.setListener(null);
            usbIoManager.stop();
        }
        usbIoManager = null;
        try {
            usbSerialPort.close();
        } catch (IOException ignored) {}
        usbSerialPort = null;
    }

    private void receive(byte[] data) {
        SpannableStringBuilder spn = new SpannableStringBuilder();
        spn.append("receive " + data.length + " bytes\n");
        if(data.length > 0)
            spn.append(HexDump.dumpHexString(data)).append("\n");
    }

    private void send(byte[] data) {
        try {
            usbSerialPort.write(data, WRITE_WAIT_MILLIS);
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }
}
