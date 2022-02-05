package com.linkrussia.bt_data;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final Pattern DEVICE_PATTERN = Pattern.compile("HT\\d{4}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepare();
        finish();
    }

    @SuppressLint("MissingPermission")
    private void prepare() {
        BluetoothDevice device = findBluetoothDevice();
        if (null == device) {
            sendBroadcast(new Intent().putExtra("ERROR_MESSAGE", "No device connected"));
            return;
        }

        createReceiver(device);
    }

    private void createReceiver(BluetoothDevice device) {
        BroadcastReceiver br = new BTReceiver(device);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(br, filter);
    }

    @SuppressLint("MissingPermission")
    private BluetoothDevice findBluetoothDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        for (BluetoothDevice bluetoothDevice : bluetoothAdapter.getBondedDevices()) {
            Log.d("BluetoothDevice", bluetoothDevice.getName());
            return bluetoothDevice;

            /*
            if (DEVICE_PATTERN.matcher(bluetoothDevice.getName()).matches()) {
                return bluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());
            }
            */
        }

        return null;
    }
}