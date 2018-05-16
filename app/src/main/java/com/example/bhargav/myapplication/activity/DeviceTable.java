package com.example.bhargav.myapplication.activity;

import com.example.bhargav.myapplication.modal.Device;
import com.example.bhargav.myapplication.modal.Device_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by Shanmuka on 5/2/2018.
 */
public class DeviceTable {
    public static Device findByMac(String mac) {
        return SQLite.select().from(Device.class).where(Device_Table.macAddress.eq(mac)).querySingle();
    }
    public static List<Device> getAll() {
        return SQLite.select().from(Device.class).queryList();
    }
}
