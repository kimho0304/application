package com.example.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

public class OnReceive extends BroadcastReceiver {
    protected static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    public static UsbDevice device;
    public static String msg;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("hello_receive", "onReceive of Main");

        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Log.d("hello_receive", "Power On");
            Toast.makeText(context, "전원 연결 상태", Toast.LENGTH_SHORT).show();
        } else if(Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            Log.d("hello_receive", "Power Off");
            Toast.makeText(context, "전원 해제 상태", Toast.LENGTH_SHORT).show();
        }
        else if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
            synchronized (this) {

                device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if(device != null){
                        //call method to set up device communication
                        Log.d("hello_receive", "Device lists: "+device);
                        msg = "Device name: "+device.getDeviceName()+", PID: "+String.valueOf(device.getProductId())+", VID: "+String.valueOf(device.getVendorId());
                    }
                    else{
                        Log.d("hello_receive", "NULL");
                        msg = "NULL";
                    }
                }
                else {
                    Log.d("hello_receive", "Permission denied for device " + device);
                }
            }
        }
    }
}
