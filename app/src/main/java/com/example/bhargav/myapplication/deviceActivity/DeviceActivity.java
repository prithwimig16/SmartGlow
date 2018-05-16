package com.example.bhargav.myapplication.deviceActivity;

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
import android.widget.Toast;

import com.example.bhargav.myapplication.R;
import com.example.bhargav.myapplication.activity.DeviceTable;
import com.example.bhargav.myapplication.activity.LightItemAdapter;
import com.example.bhargav.myapplication.activity.LightView;
import com.example.bhargav.myapplication.modal.Device;
import com.jingxun.meshlibtelink.TelinkLightService;
import com.telink.bluetooth.event.ServiceEvent;
import com.telink.bluetooth.light.DeviceInfo;
import com.telink.bluetooth.light.LeScanParameters;
import com.telink.bluetooth.light.LeUpdateParameters;
import com.telink.bluetooth.light.Manufacture;
import com.telink.bluetooth.light.Opcode;
import com.telink.bluetooth.light.Parameters;
import com.telink.util.Event;
import com.telink.util.EventListener;
import com.telink.util.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shanmuka on 5/2/2018.
 */
public class DeviceActivity extends AppCompatActivity implements LightView, LightItemAdapter.ItemListener, EventListener<ServiceEvent> {

    private RecyclerView recyclerView;
    private LightItemAdapter lightItemAdapter;
    List<DeviceInfo> list = new ArrayList<DeviceInfo>();
    String newMeshName = "";
    String newMeshPassword = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_device);
        newMeshName = "abc123";//random(10);
        newMeshPassword = "123";//random(4);
        recyclerView = findViewById(R.id.rcv_device_info);
        lightItemAdapter = new LightItemAdapter(this, this, this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DeviceActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(lightItemAdapter);
        scan();

    }

    private void scan() {
        final LeScanParameters params = LeScanParameters.create();
        params.setMeshName(Manufacture.getDefault().getFactoryName());
        params.setOutOfMeshName("out_of_mesh");
        params.setScanMode(false);
        params.setTimeoutSeconds(60);
        TelinkLightService.Instance().startScan(params);
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DeviceInfo deviceInfo = intent.getParcelableExtra("com.telink.bluetooth.light.EXTRA_DEVICE");
            checkExist(deviceInfo);
        }
    };

    private void checkExist(DeviceInfo deviceInfo) {
        String macAddress1 = deviceInfo.macAddress;

        if (list.size() == 0) {
            list.add(deviceInfo);
            lightItemAdapter.notifyDataSetChanged();
            return;
        }
        for (DeviceInfo device : list) {
            String macAddress = device.macAddress;
            if (!macAddress.equalsIgnoreCase(macAddress1)) {
                list.add(deviceInfo);
            }
        }
        lightItemAdapter.notifyDataSetChanged();
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
            if (dbDevice == null) {
                device.save();
                Toast.makeText(this, device.macAddress + "saved", Toast.LENGTH_SHORT).show();
            } else {
                device.update();
                Toast.makeText(this, device.macAddress + "updated", Toast.LENGTH_SHORT).show();
            }
            updateMesh(new DeviceInfo[]{temDeviceInfo});
        }

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

    @Override
    public void onItemLongClick(Object o) {
        TelinkLightService.Instance().login(Strings.stringToBytes(newMeshName, 16), Strings.stringToBytes(newMeshPassword, 16));
        Toast.makeText(this, "item Long clicked ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void doOperation(DeviceInfo deviceInfo) {
        TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_D0.getValue(), 0x54, new byte[]{0x05, 0x00, 0x00});
    }

    @Override
    public void performed(Event<ServiceEvent> event) {

    }

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
}
