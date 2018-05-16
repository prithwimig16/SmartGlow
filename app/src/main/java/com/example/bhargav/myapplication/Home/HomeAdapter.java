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
import com.example.bhargav.myapplication.activity.ItemListener;
import com.example.bhargav.myapplication.modal.Device;

import java.util.List;

/**
 * Created by Shanmuka on 5/2/2018.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeItemViewHolder> {

    private final Activity mActivity;
    private final LayoutInflater inflater;
    private final HomeView lightView;
    private List<Device> devices;
    ItemListener itemListener;


    public HomeAdapter(HomeActivity homeActivity, HomeView homeView,ItemListener itemListener) {
        this.mActivity = homeActivity;
        inflater = LayoutInflater.from(homeActivity);
        this.lightView = homeView;
        this.itemListener = itemListener;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
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
        final Device deviceInfo = devices.get(position);
        holder.tvDeviceName.setText(deviceInfo.deviceName);
        holder.aSwitch.setVisibility(View.GONE);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        holder.setItem(deviceInfo);
    }

    @Override
    public int getItemCount() {
        return devices == null ? 0 : devices.size();
    }

    class HomeItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        TextView tvDeviceName;
        Switch aSwitch;
        private Device item;

        public HomeItemViewHolder(View itemView) {
            super(itemView);
            tvDeviceName = itemView.findViewById(R.id.tv_device_name);
            aSwitch = itemView.findViewById(R.id.switch_status);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemListener != null)
                itemListener.onItemClick(item);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        public void setItem(Device item) {
            this.item = item;
        }
    }
}
