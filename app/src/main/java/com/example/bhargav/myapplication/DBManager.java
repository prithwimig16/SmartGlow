package com.example.bhargav.myapplication;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Shanmuka on 5/2/2018.
 */

@Database(name = DBManager.NAME, version = DBManager.VERSION)
public class DBManager {
    public static final String NAME = "luminous_consumer";
    public static final int VERSION = 1;
}
