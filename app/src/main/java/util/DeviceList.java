package util;

import com.telink.bluetooth.light.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class DeviceList {

    static  List<DeviceInfo> list = new ArrayList<DeviceInfo>();


    public static void add(DeviceInfo deviceInfo){
        list.add(deviceInfo);
    }

    public static List<DeviceInfo> getList() {
        return list;
    }
}
