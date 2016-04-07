package com.mooring.mh.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mooring.mh.views.CustomImageView.ZoomCircleView;

/**
 * Created by Will on 16/4/7.
 */
public class HorizontalViewHolder extends RecyclerView.ViewHolder {
    private ZoomCircleView imageView;
    private TextView textView;

    HorizontalViewHolder(View view, int imageViewId, int textViewId) {
        super(view);

        imageView = (ZoomCircleView) view.findViewById(imageViewId);
        textView = (TextView) view.findViewById(textViewId);
    }

    ZoomCircleView getImageView() {
        return imageView;
    }

    TextView getTextView() {
        return textView;
    }
}
