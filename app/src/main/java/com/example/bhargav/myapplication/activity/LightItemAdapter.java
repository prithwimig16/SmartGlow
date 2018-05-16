package com.example.bhargav.myapplication.activity;

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
import com.telink.bluetooth.light.DeviceInfo;

import java.util.List;

public class LightItemAdapter extends RecyclerView.Adapter<LightItemAdapter.LightItemViewHolder> {

    private final Activity mActivity;
    private final LayoutInflater inflater;
    private final LightView lightView;
    private final ItemListener itemListener;
    private List<DeviceInfo> deviceInfos;

    public LightItemAdapter(Activity mActivity, LightView lightView, ItemListener itemListener, List<DeviceInfo> list) {
        this.mActivity = mActivity;
        inflater = LayoutInflater.from(mActivity);
        this.lightView = lightView;
        this.itemListener = itemListener;
        this.deviceInfos = list;
    }

    public void setDeviceInfos(List<DeviceInfo> deviceInfos) {
        this.deviceInfos = deviceInfos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LightItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_device, viewGroup, false);
        return new LightItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LightItemViewHolder lightItemViewHolder, int i) {
        final DeviceInfo deviceInfo = deviceInfos.get(i);
        lightItemViewHolder.tvDeviceName.setText(deviceInfo.deviceName);
        lightItemViewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lightView.doOperation(deviceInfo);
            }
        });

        lightItemViewHolder.setItem(deviceInfo);
    }

    @Override
    public int getItemCount() {
        return deviceInfos == null ? 0 : deviceInfos.size();
    }

    class LightItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{


        TextView tvDeviceName;
        Switch aSwitch;
        private DeviceInfo item;

        public LightItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            aSwitch = itemView.findViewById(R.id.switch_status);
            tvDeviceName = itemView.findViewById(R.id.tv_device_name);
        }

        @Override
        public void onClick(View v) {
            if (itemListener != null)
                itemListener.onItemClick(item);
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemListener != null)
            itemListener.onItemLongClick(item);
            return true;
        }

        public void setItem(DeviceInfo item) {
            this.item = item;
        }
    }


    public interface ItemListener {
        void onItemClick(Object o);
        void onItemLongClick(Object o);
    }
}
