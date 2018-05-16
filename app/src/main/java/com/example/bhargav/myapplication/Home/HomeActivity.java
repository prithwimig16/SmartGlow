package com.example.bhargav.myapplication.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bhargav.myapplication.R;
import com.example.bhargav.myapplication.activity.DeviceTable;
import com.example.bhargav.myapplication.activity.ItemListener;
import com.example.bhargav.myapplication.deviceActivity.DeviceActivity;
import com.example.bhargav.myapplication.modal.Device;
import com.example.bhargav.myapplication.modal.DeviceOp;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.jingxun.meshlibtelink.TelinkLightService;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.telink.TelinkApplication;
import com.telink.bluetooth.light.LeAutoConnectParameters;
import com.telink.bluetooth.light.LeRefreshNotifyParameters;
import com.telink.bluetooth.light.LeScanParameters;
import com.telink.bluetooth.light.Manufacture;
import com.telink.bluetooth.light.Opcode;
import com.telink.bluetooth.light.Parameters;
import com.telink.util.Event;
import com.telink.util.EventListener;

import java.util.List;

/**
 * Created by Shanmuka on 5/2/2018.
 */
public class HomeActivity extends AppCompatActivity implements HomeView, EventListener<String>, ItemListener {

    private TextView mtextView;
    private RecyclerView recyclerView;
    private Button mfloatingActionButton;
    String newMeshName = "";
    String newMeshPassword = "";
    private HomeAdapter homeAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelinkApplication.getInstance().doInit(getApplicationContext(), TelinkLightService.class);
        setContentView(R.layout.activity_home);
        FlowManager.init(this);
        newMeshName = "abc123";//random(10);
        newMeshPassword = "123";//random(4);
        //  autoConnect(newMeshName, newMeshPassword, null);
        mtextView = findViewById(R.id.tv_no_devices);
        recyclerView = findViewById(R.id.rcv_bulbs);
        mfloatingActionButton = findViewById(R.id.btn_fab);
        homeAdapter = new HomeAdapter(this, this, this);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(homeAdapter);
        TelinkApplication.getInstance().addEventListener("com.telink.bluetooth.light.EVENT_SERVICE_CONNECTED", this);
        mfloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DeviceActivity.class);
                startActivity(intent);

            }
        });
    }



    @Override
    public void doOperation(final Device device) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        changeColor(selectedColor, device);
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeColor(selectedColor, device);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void changeColor(int selectedColor, Device device) {
        String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor));
        String s1 = hexColor.substring(1, 3);
        String s2 = hexColor.substring(3, 5);
        String s3 = hexColor.substring(5, 7);
        TelinkLightService.Instance().getAdapter().sendCommand(Opcode.BLE_GATT_OP_CTRL_E2.getValue(), 0x01, new byte[]{0x04, (byte) hexStringToByte(s1), (byte) hexStringToByte(s2), (byte) hexStringToByte(s3)});
        DeviceOp deviceOp = new DeviceOp();
        deviceOp.color = hexColor;
        deviceOp.deviceName = device.deviceName;
        deviceOp.macAddress = device.macAddress;
        deviceOp.meshName = device.meshName;
//        deviceOp.save();
    }

    private static byte hexStringToByte(String data) {
        return (byte) ((Character.digit(data.charAt(0), 16) << 4)
                | Character.digit(data.charAt(1), 16));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Device> all = DeviceTable.getAll();
        homeAdapter.setDevices(all);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TelinkApplication.getInstance().doDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    @Override
    public void performed(Event event) {
        //  Toast.makeText(this, "temp", Toast.LENGTH_SHORT).show();
        List<Device> all = DeviceTable.getAll();
        for (Device device : all) {
            autoConnect(device.meshName, newMeshPassword, device.macAddress);
        }
    }

    @Override
    public void onItemClick(Object o) {
        doOperation((Device) o);
    }

    @Override
    public void onItemLongClick(Object o) {

    }
}
