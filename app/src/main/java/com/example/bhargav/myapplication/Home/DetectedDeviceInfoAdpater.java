package com.example.bhargav.myapplication.Home;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.bhargav.myapplication.R;
import com.example.bhargav.myapplication.activity.LightView;
import com.example.bhargav.myapplication.deviceActivity.DeviceActivity;
import com.example.bhargav.myapplication.modal.Device;
import com.telink.bluetooth.light.DeviceInfo;

import java.util.List;

/**
 * Created by Shanmuka on 5/2/2018.
 */
public class DetectedDeviceInfoAdpater extends RecyclerView.Adapter<DetectedDeviceInfoAdpater.HomeItemViewHolder> {

    private final Activity mActivity;
    private final LayoutInflater inflater;
    private final LightView lightView;
    private List<DeviceInfo> deviceInfos;


    public DetectedDeviceInfoAdpater(DeviceActivity homeActivity, LightView lightView) {
        this.mActivity = homeActivity;
        inflater = LayoutInflater.from(homeActivity);
        this.lightView = lightView;
    }

    public void setDeviceInfos(List<DeviceInfo> deviceInfos) {
        this.deviceInfos = deviceInfos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_device, viewGroup, false);
        return new HomeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemViewHolder holder, int position) {
        final DeviceInfo deviceInfo = deviceInfos.get(position);
        holder.tvDeviceName.setText(deviceInfo.deviceName);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lightView.doOperation(deviceInfo);
            }
        });

        holder.setItem(deviceInfo);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HomeItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        TextView tvDeviceName;
        Switch aSwitch;
        private DeviceInfo item;

        public HomeItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        public void setItem(DeviceInfo item) {
            this.item = item;
        }
    }
}
