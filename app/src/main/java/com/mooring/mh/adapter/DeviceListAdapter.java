package com.mooring.mh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.machtalk.sdk.domain.Device;
import com.mooring.mh.R;

import java.util.List;

/**
 * 设备列表适配器
 * <p/>
 * Created by Will on 16/5/13.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceHolder> {

    private OnRecyclerItemClickListener itemClickListener;
    private List<Device> data;
    private String currDeviceId;

    public DeviceListAdapter(List<Device> mDeviceList, String currDeviceId) {
        this.data = mDeviceList;
        this.currDeviceId = currDeviceId;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.existing_device_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {
        Device device = data.get(position);
        if (device != null) {
            holder.tv_device_name.setText(device.getName());
            if (device.getId().equals(currDeviceId)) {
                holder.imgView_check_box.setVisibility(View.VISIBLE);
            }
            if (!device.isOnline()) {
                holder.imgView_device_img.setAlpha(0.5f);
            }
        }
        holder.v.setTag(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 设置item点击监听
     *
     * @param itemClickListener item点击监听
     */
    public void setItemClickListener(OnRecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * ViewHolder 继承自RecycleView中的ViewHolder
     */
    public static class DeviceHolder extends RecyclerView.ViewHolder {
        public View v;
        public ImageView imgView_check_box;
        public ImageView imgView_device_img;
        public TextView tv_device_name;

        public DeviceHolder(View view) {
            super(view);
            v = view;
            imgView_check_box = (ImageView) view.findViewById(R.id.imgView_check_box);
            imgView_device_img = (ImageView) view.findViewById(R.id.imgView_device_img);
            tv_device_name = (TextView) view.findViewById(R.id.tv_device_name);
        }
    }
}
