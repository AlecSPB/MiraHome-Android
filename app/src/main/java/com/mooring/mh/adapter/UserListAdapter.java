package com.mooring.mh.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.db.LocalUser;
import com.mooring.mh.views.CircleImgView.CircleImageView;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 用户列表横向滑动RecycleView的适配器
 * <p/>
 * Created by Will on 16/4/7.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.HorizontalViewHolder> {

    private List<LocalUser> dataList;
    private OnRecyclerItemClickListener itemClickListener;

    public UserListAdapter(List<LocalUser> dataList) {
        this.dataList = dataList;
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.horizontal_scroll_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder viewHolder, int position) {
        final LocalUser data = dataList.get(position);
        LogUtil.w("viewHolder.getAdapterPosition():    " + viewHolder.getAdapterPosition());

        if (position != dataList.size() - 1) {
            viewHolder.tv_name.setVisibility(View.VISIBLE);
            viewHolder.img_header.setVisibility(View.VISIBLE);
            viewHolder.img_add.setVisibility(View.GONE);

            viewHolder.tv_name.setText(data.get_name());
            Bitmap bm = BitmapFactory.decodeFile(data.get_header());
            if (bm != null) {
                viewHolder.img_header.setImageBitmap(bm);
            }
        } else {
            viewHolder.tv_name.setVisibility(View.GONE);
            viewHolder.img_header.setVisibility(View.GONE);
            viewHolder.img_add.setVisibility(View.VISIBLE);
        }
        viewHolder.v.setTag(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * ViewHolder
     */
    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img_header;
        public ImageView img_add;
        public TextView tv_name;
        public View v;

        HorizontalViewHolder(View view) {
            super(view);

            v = view;
            img_header = (CircleImageView) view.findViewById(R.id.imgView_horizontal_header);
            img_add = (ImageView) view.findViewById(R.id.imgView_horizontal_add);
            tv_name = (TextView) view.findViewById(R.id.tv_horizontal_name);
        }
    }
}

