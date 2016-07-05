package com.mooring.mh.adapter;

import android.view.View;

/**
 * RecycleView 对应的Item点击监听
 * <p/>
 * Created by Will on 16/5/13.
 */
public interface OnRecyclerItemClickListener {
    /**
     * item view 回调方法
     *
     * @param view     被点击的view
     * @param position 点击索引
     */
    void onItemClick(View view, int position);
}
