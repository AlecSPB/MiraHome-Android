package com.mooring.mh.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.model.UserHeadInfo;
import com.mooring.mh.views.CircleImgView.CircleImageView;

import java.util.List;

/**
 * 用户列表横向滑动RecycleView的适配器
 * <p/>
 * Created by Will on 16/4/7.
 */
public class UserListAdapter extends RecyclerView
        .Adapter<UserListAdapter.HorizontalViewHolder> {

    public interface AdapterData {
        String getTitle();

        String getThumbnailUrl();
    }

    public interface OnClickListener<ImageData> {
        void onClick(ImageData obj, int position);
    }

    private List<UserHeadInfo> dataList;
    private Resources resources;
    private OnClickListener<UserHeadInfo> listener;
    private Activity context;

    public UserListAdapter(Activity context, List<UserHeadInfo> dataList) {
        this.context = context;
        this.dataList = dataList;
        resources = context.getResources();
    }

    public void setOnClickListener(OnClickListener<UserHeadInfo> listener) {
        this.listener = listener;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item, parent, false);
        return new HorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder viewHolder, final int position) {
        final UserHeadInfo data = dataList.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(data, position);
                }
            }
        });

        Bitmap bm = null;
        if (position != dataList.size() - 1) {
            viewHolder.getTextView().setVisibility(View.VISIBLE);
            viewHolder.getImageHeader().setVisibility(View.VISIBLE);
            viewHolder.getImageAdd().setVisibility(View.GONE);

            viewHolder.getTextView().setText(data.getTitle());
            bm = BitmapFactory.decodeFile(data.getThumbnailUrl());
            if (bm != null) {
                viewHolder.getImageHeader().setImageBitmap(bm);
            }
        } else {
            viewHolder.getTextView().setVisibility(View.GONE);
            viewHolder.getImageHeader().setVisibility(View.GONE);
            viewHolder.getImageAdd().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * ViewHolder
     */
    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_header;
        private ImageView img_add;
        private TextView tv_name;

        HorizontalViewHolder(View view) {
            super(view);

            img_header = (CircleImageView) view.findViewById(R.id.imgView_horizontal_header);
            img_add = (ImageView) view.findViewById(R.id.imgView_horizontal_add);
            tv_name = (TextView) view.findViewById(R.id.tv_horizontal_name);
        }

        CircleImageView getImageHeader() {
            return img_header;
        }

        ImageView getImageAdd() {
            return img_add;
        }

        TextView getTextView() {
            return tv_name;
        }

    }

}

