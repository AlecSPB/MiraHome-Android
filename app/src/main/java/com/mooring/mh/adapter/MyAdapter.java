package com.mooring.mh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.model.TestData;

import java.util.List;

/**
 * Created by Will on 16/4/21.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<TestData> datas;

    public MyAdapter(List<TestData> datas) {
        this.datas = datas;
    }

    /**
     * 创建新View，被LayoutManager所调用
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    /**
     * 将数据与界面进行绑定的操作
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imgView_item_bg.setImageResource(datas.get(position).getBgId());
        holder.imgView_item_icon.setImageResource(datas.get(position).getIconId());
        holder.tv_item_title.setText(datas.get(position).getTitle());
        holder.tv_item_data.setText(datas.get(position).getData());
        holder.tv_item_unit.setText(datas.get(position).getUnit());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * ViewHolder 继承自RecycleView中的ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView_item_bg;
        public ImageView imgView_item_icon;
        public TextView tv_item_title;
        public TextView tv_item_data;
        public TextView tv_item_unit;

        public ViewHolder(View view) {
            super(view);
            imgView_item_bg = (ImageView) view.findViewById(R.id.imgView_item_bg);
            imgView_item_icon = (ImageView) view.findViewById(R.id.imgView_item_icon);
            tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
            tv_item_data = (TextView) view.findViewById(R.id.tv_item_data);
            tv_item_unit = (TextView) view.findViewById(R.id.tv_item_unit);
        }
    }
}
