package com.example.bhargav.myapplication.modal;

import com.example.bhargav.myapplication.DBManager;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Shanmuka on 5/3/2018.
 */

@Table(database = DBManager.class)
public class DeviceOp extends BaseModel{

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column
    public String macAddress;

    @Column
    public String deviceName;

    @Column
    public String meshName;

    @Column
    public String color;


}
