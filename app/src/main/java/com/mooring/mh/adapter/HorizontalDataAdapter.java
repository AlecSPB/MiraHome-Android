package com.mooring.mh.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooring.mh.R;
import com.mooring.mh.activity.AddUserActivity;
import com.mooring.mh.utils.MConstants;

import java.util.List;

/**
 * Created by Will on 16/4/7.
 */
public class HorizontalDataAdapter<T extends HorizontalDataAdapter.AdapterData> extends RecyclerView.Adapter<HorizontalViewHolder> {

    public interface AdapterData {
        String getTitle();

        String getThumbnailUrl();
    }

    public interface OnClickListener<T> {
        void onClick(T obj);
    }

    private List<T> dataList;
    private int layoutId;
    private int imageViewId;
    private int textViewId;
    private Resources resources;
    private OnClickListener<T> listener;
    private Activity context;

    public HorizontalDataAdapter(Activity context, List<T> dataList, int layoutId, int imageViewId, int textViewId, Resources resources) {
        this.context = context;
        this.dataList = dataList;
        this.layoutId = layoutId;
        this.imageViewId = imageViewId;
        this.textViewId = textViewId;
        this.resources = resources;
    }

    public void setOnClickListener(OnClickListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new HorizontalViewHolder(v, imageViewId, textViewId);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder viewHolder, int position) {
        final T data = dataList.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(data);
                }
            }
        });

//        viewHolder.getTextView().setText(data.getTitle());
//        Bitmap bm = BitmapFactory.decodeFile(data.getThumbnailUrl());
//        if (bm != null) {
//            viewHolder.getImageView().setImageBitmap(bm);
//        }

        viewHolder.getTextView().setText(data.getTitle());
        Bitmap bm = null;
        if (position != dataList.size() - 1) {
            bm = BitmapFactory.decodeFile(data.getThumbnailUrl());
        } else {
            BitmapDrawable bd = (BitmapDrawable) resources.getDrawable(R.mipmap.ic_launcher);
            bm = bd.getBitmap();
        }
        if (bm != null) {
            viewHolder.getImageView().setImageBitmap(bm);
        }

        if (position == dataList.size() - 1) {
            viewHolder.getImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加用户
                    Intent it = new Intent();
                    it.setClass(context, AddUserActivity.class);
                    context.startActivityForResult(it, MConstants.ADD_USER_REQUEST);
                }
            });
        }

//        try {
//            InputStream is = resources.getAssets().open(data.getThumbnailUrl());
//            Bitmap bm = BitmapFactory.decodeStream(is);
//            viewHolder.getImageView().setImageBitmap(bm);
//        } catch (IOException e) {
//        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}

