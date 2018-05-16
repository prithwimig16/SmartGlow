package com.example.bhargav.myapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.bhargav.myapplication.R;
import com.example.bhargav.myapplication.modal.Device;
import com.jingxun.meshlibtelink.TelinkLightService;
import com.telink.TelinkApplication;
import com.telink.bluetooth.event.ServiceEvent;
import com.telink.bluetooth.light.DeviceInfo;
import com.telink.bluetooth.light.LeAutoConnectParameters;
import com.telink.bluetooth.light.LeRefreshNotifyParameters;
import com.telink.bluetooth.light.LeScanParameters;
import com.telink.bluetooth.light.LeUpdateParameters;
import com.telink.bluetooth.light.Manufacture;
import com.telink.bluetooth.light.Opcode;
import com.telink.bluetooth.light.Parameters;
import com.telink.util.Event;
import com.telink.util.EventListener;
import com.telink.util.Strings;

import java.util.Random;

import util.DeviceList;

public class LightsActivity extends AppCompatActivity implements LightView, LightItemAdapter.ItemListener, EventListener<ServiceEvent> {

    RecyclerView recyclerView;

    LightItemAdapter lightItemAdapter;
    String newMeshName = "";
    String newMeshPassword = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        lightItemAdapter = new LightItemAdapter(this, this, this, null);
        recyclerView.setAdapter(lightItemAdapter);

        newMeshName = "abc123";//random(10);
        newMeshPassword = "123";//random(4);

        findViewById(R.id.btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });

        findViewById(R.id.btn_auto_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoConnect(newMeshName, newMeshPassword, null);
            }
        });

        findViewById(R.id.btn_light_op).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_D0.getValue(), 0x01, new byte[]{0x05, 0x00, 0x00});
                //color changes
                TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_E2.getValue(), 0x01, new byte[]{0x04, (byte) 0xF0, (byte) 0xF0, (byte) 0x15});
                //reset
//        TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_E3.getValue(), 0x01, new byte[]{0x01});
            }
        });


    }


    private void scan() {
        final LeScanParameters params = LeScanParameters.create();
        params.setMeshName(Manufacture.getDefault().getFactoryName());
        params.setOutOfMeshName("out_of_mesh");
        params.setScanMode(false);
        params.setTimeoutSeconds(100);
        TelinkLightService.Instance().startScan(params);
    }


    @Override
    protected void onStop() {
        super.onStop();
        TelinkApplication.getInstance().doDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        TelinkApplication.getInstance().doInit(getApplicationContext(), TelinkLightService.class);
    }

    @Override
    public void doOperation(DeviceInfo deviceInfo) {
        //TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_E2.getValue(), deviceInfo.meshAddress, new byte[]{0x04, (byte) 0xF0, (byte) 0xF0, (byte) 0xF6});
//        TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_E3.getValue(), deviceInfo.meshAddress, new byte[]{0x01});
        TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_D0.getValue(), 0x54, new byte[]{0x05, 0x00, 0x00});

        //    TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_D0.getValue(), 143, new byte[]{0x04, 0x00, 0x00});

        //    TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_E2.getValue(), 143, new byte[]{0x04, (byte)0xFF, 0x66, 0x00});
        //TelinkLightService.Instance().getAdapter().disconnect();
    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DeviceInfo deviceInfo = intent.getParcelableExtra("com.telink.bluetooth.light.EXTRA_DEVICE");
            DeviceList.add(deviceInfo);
            lightItemAdapter.setDeviceInfos(DeviceList.getList());
        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("com.telink.bluetooth.light.ACTION_LE_SCAN"));
        super.onResume();
    }

    @Override
    public void onItemClick(Object o) {

        if (o instanceof DeviceInfo) {
            DeviceInfo deviceInfo = (DeviceInfo) o;
            DeviceInfo temDeviceInfo = new DeviceInfo();
            temDeviceInfo.deviceName = deviceInfo.deviceName + "_NEW";
            temDeviceInfo.firmwareRevision = deviceInfo.firmwareRevision;
            temDeviceInfo.longTermKey = deviceInfo.longTermKey;
            temDeviceInfo.macAddress = deviceInfo.macAddress;
            temDeviceInfo.meshUUID = deviceInfo.meshUUID;
            temDeviceInfo.productUUID = deviceInfo.productUUID;
            temDeviceInfo.manufactureID = deviceInfo.manufactureID;
            temDeviceInfo.meshName = newMeshName;
            temDeviceInfo.status = deviceInfo.status;
            temDeviceInfo.meshAddress = 0x0001;

            Device device = new Device();
            device.deviceName = deviceInfo.deviceName + "_NEW";
            device.firmwareRevision = deviceInfo.firmwareRevision;
            device.longTermKey = deviceInfo.longTermKey;
            device.macAddress = deviceInfo.macAddress;
            device.meshUUID = deviceInfo.meshUUID;
            device.productUUID = deviceInfo.productUUID;
            device.manufactureID = deviceInfo.manufactureID;
            device.meshName = newMeshName;
            device.status = deviceInfo.status;
            device.meshAddress = 0x0001;
            Device dbDevice = DeviceTable.findByMac(deviceInfo.macAddress);
            if (dbDevice == null){
                device.save();
            } else {
                device.update();
            }
            updateMesh(new DeviceInfo[]{temDeviceInfo});
        }


        Toast.makeText(this, "item clicked ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(Object o) {
        TelinkLightService.Instance().login(Strings.stringToBytes(newMeshName, 16), Strings.stringToBytes(newMeshPassword, 16));
        Toast.makeText(this, "item Long clicked ", Toast.LENGTH_SHORT).show();
    }

    private void updateMesh(DeviceInfo[] deviceInfoList) {
        LeUpdateParameters parameters = Parameters.createUpdateParameters();
        parameters.setOldMeshName(Manufacture.getDefault().getFactoryName());
        parameters.setOldPassword(Manufacture.getDefault().getFactoryPassword());
        parameters.setNewMeshName(newMeshName);
        parameters.setNewPassword(newMeshPassword);
        parameters.setUpdateDeviceList(deviceInfoList);
        TelinkLightService.Instance().updateMesh(parameters);
    }

    public void autoConnect(String meshName, String meshPassword, String macAddress) {
        LeAutoConnectParameters parameters = Parameters.createAutoConnectParameters();
        parameters.setMeshName(meshName);
        parameters.setPassword(meshPassword);
        parameters.setConnectMac(macAddress);
        parameters.setTimeoutSeconds(10);
        parameters.autoEnableNotification(true);
        TelinkLightService.Instance().autoConnect(parameters);
        enableAutoRefreshNotify();
    }


    public void enableAutoRefreshNotify() {
        LeRefreshNotifyParameters parameters = Parameters.createRefreshNotifyParameters();
        parameters.setRefreshRepeatCount(10);
        parameters.setRefreshInterval(2000);
        TelinkLightService.Instance().autoRefreshNotify(parameters);
    }

    public static String random(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void performed(Event<ServiceEvent> event) {

    }
}
