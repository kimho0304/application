package com.example.application;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.usb.UsbDeviceConnection;
import android.os.Build;
import android.provider.SyncStateContract;
import android.widget.Toast;
import android.content.Context;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import kr.taemin.android.usbserial.driver.*;
import kr.taemin.android.usbserial.util.SerialInputOutputManager;


import android.hardware.usb.UsbManager;

import com.example.application.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;


public class Terminal extends AppCompatActivity {
    // Used to load the 'application' library on application startup.
    static {
        System.loadLibrary("application");
    }
    protected static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private ActivityMainBinding binding;
    private BroadcastReceiver permission = new OnReceive();

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(Intent.ACTION_POWER_CONNECTED); //실험용
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED); //실험용
        registerReceiver(permission, filter);
    }

    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(permission);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hello_terminal", "onCreate of Terminal");

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(permission, filter);

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Log.d("hello_terminal", "Devices: "+device+", PID: "+String.valueOf(device.getProductId())+", VID: "+String.valueOf(device.getVendorId()));
            OnReceive.msg = "Device name: "+device.getDeviceName()+", PID: "+String.valueOf(device.getProductId())+", VID: "+String.valueOf(device.getVendorId());
        }

        //디바이스 목록 불러오기
        /*HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        UsbDevice device = deviceList.get("deviceName");*/
        //String deviceName = device.getDeviceName();

        /*PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE| PendingIntent.FLAG_UPDATE_CURRENT);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(Main.usbReceiver, filter);*/

        //UsbDevice device = Main.device;

        /*
        if(device == null) {
            Log.d("hello_terminal", "connection failed: device not found");
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if(driver == null) {
            Log.d("hello_terminal", "connection failed: no driver for device");
        }
        if(driver.getPorts().size() < portNum) {
            Log.d("hello_terminal", "connection failed: not enough ports at device");
        }
        UsbSerialPort usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = manager.openDevice(driver.getDevice());

        UsbDeviceConnection usbDeviceConnection = manager.openDevice(driver.getDevice());
        int fileDescriptor = usbDeviceConnection.getFileDescriptor();
        unrootedUsbDescription(fileDescriptor);


        if (usbConnection == null && usbPermission == UsbPermission.Unknown && !manager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), flags);
            manager.requestPermission(driver.getDevice(), usbPermissionIntent);
        }
        if (usbConnection == null) {
            if (!manager.hasPermission(driver.getDevice()))
                Log.d("hello_terminal","connection failed: permission denied");
            else
                Log.d("hello_terminal","connection failed: open failed");
        }

        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(Integer.parseInt("119200"), 8, 1, UsbSerialPort.PARITY_NONE);
            if (withIoManager) {
                usbIoManager = new SerialInputOutputManager(usbSerialPort, (SerialInputOutputManager.Listener) this);
                usbIoManager.start();
            }
            Log.d("hello_terminal","connected");
            connected = true;
        } catch (Exception e) {
            //status("connection failed: " + e.getMessage());
            disconnect();
        }
*/
        //UsbDeviceConnection usbDeviceConnection = manager.openDevice(device);
        //int fileDescriptor = usbDeviceConnection.getFileDescriptor();

        //권한 요청 대화 상자 표시
        //manager.requestPermission(device, permissionIntent);

        //native function call
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView tv = findViewById(R.id.sample_text);

        Log.d("hello_terminal", "Binding process");

        //strings();
        tv.setText(OnReceive.msg);
        //strings();
        //tv.setText(strings());
    }

    public native void main();
    public native String example();
    public native void strings();
    public native String test();
    public native int unrootedUsbDescription(int fileDescriptor);
}