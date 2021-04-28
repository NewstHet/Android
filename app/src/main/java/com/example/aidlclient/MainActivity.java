package com.example.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aidl.ISimp;

public class MainActivity    extends AppCompatActivity {

    // Destination (Requested) service application information
    private static final String TARGET_PACKAGE =
            "com.example.aidlexampleservice";
    private static final String TARGET_CLASS =
            "com.example.aidlexampleservice.MySimpService";


    private static final String TAG = "MainActivity";
    private ISimp iSimpInterface;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            iSimpInterface = ISimp.Stub.asInterface(service);

            try {

                Log.d(TAG, "onServiceConnected");
                Log.d(TAG, "Received message: " +iSimpInterface.getMessage());

            } catch (RemoteException e) {

                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iSimpInterface = null;
            Log.d(TAG, "Service Disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate called");

        Intent intent = new Intent("connect_to_aidl_service");
        //intent.setPackage("com.example.aidlexampleservice");
        //intent.setAction("MySimpService");
        intent.setClassName(TARGET_PACKAGE, TARGET_CLASS);
        //if(getBaseContext().getApplicationContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE) == true){
        if(bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE) == true){
            Log.d(TAG, "Bind service Succeeded");
        } else {
            Log.d(TAG, "Bind service failed");
        }
        //bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        Log.d(TAG, "setOnClickListener create");

        findViewById(R.id.click_text).setOnClickListener(v -> {
            try {

                if (iSimpInterface == null){
                    Log.d(TAG, "iSimpInterface is null ");

                }else {
                    Log.d(TAG, "Received message: " + iSimpInterface.getMessage());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        Log.d(TAG, "onCreate done");
    }

    @Override
    protected void onStop() {
        unbindService(mServiceConnection);
        super.onStop();
    }
}