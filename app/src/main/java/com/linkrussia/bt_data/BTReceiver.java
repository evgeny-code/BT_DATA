package com.linkrussia.bt_data;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

public class BTReceiver extends BroadcastReceiver {
    private static final UUID SERVER_UUID = UUID.fromString("00000002-0000-2000-0000-000000000000");

    private BluetoothDevice bluetoothDevice;

    public BTReceiver(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BTReceiver", intent.getAction());


        try (BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SERVER_UUID)) {
            bluetoothSocket.connect();
            try (DataInputStream dis = new DataInputStream(bluetoothSocket.getInputStream())) {
                try (DataOutputStream dos = new DataOutputStream(bluetoothSocket.getOutputStream())) {
                    dos.writeUTF("Hey " + intent.hashCode());
                    String response = dis.readUTF();

                    Log.d("DATA", response);
                }
            }
        } catch (Exception e) {
            Log.e("DATA", "Exception on bluetoothSocket", e);
        }
    }
}