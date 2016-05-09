package com.mooring.mh.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mooring.mh.R;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.views.AlarmDaySelectView;

import java.util.List;

/**
 * 自定义闹钟RecycleView 的Adapter
 * <p>
 * Created by Will on 16/4/21.
 */
public class AlarmClockAdapter extends RecyclerView.Adapter<AlarmClockAdapter.ViewHolder> {

    private List<String> data;
    private OnRecyclerItemClickListener itemClickListener;

    public AlarmClockAdapter(List<String> data) {
        this.data = data;
    }

    public void addData(int position) {
        data.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_clock_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String times = data.get(position);
        if (times != null && !"".equals(times) && times.length() == 13) {
            String time = times.substring(0, 4);
            String alarmDay = times.substring(4, 11);
            final String smart = times.substring(11, 12);
            String clockSwitch = times.substring(12);

            holder.tv_clock_time.setText(CommonUtils.ParsingTime(time));
            holder.daySV_clock.setTvData(CommonUtils.ParsingDay(alarmDay));
            holder.tglBtn_wake_up.setChecked("1".equals(smart));
            holder.tglBtn_clock_set.setChecked("1".equals(clockSwitch));

            holder.v.setTag(position);
        }
    }


    /**
     * 设置item点击监听
     *
     * @param itemClickListener
     */
    public void setItemClickListener(OnRecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder 继承自RecycleView中的ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_clock_time;
        public AlarmDaySelectView daySV_clock;
        public ToggleButton tglBtn_clock_set;
        public ToggleButton tglBtn_wake_up;
        public View v;

        public ViewHolder(View view) {
            super(view);
            v = view;
            tv_clock_time = (TextView) view.findViewById(R.id.tv_clock_time);
            daySV_clock = (AlarmDaySelectView) view.findViewById(R.id.daySV_clock);
            tglBtn_clock_set = (ToggleButton) view.findViewById(R.id.tglBtn_clock_set);
            tglBtn_wake_up = (ToggleButton) view.findViewById(R.id.tglBtn_wake_up);
        }
    }

    /**
     * item之间的间隔
     */
    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
            outRect.bottom = space;
        }
    }
}
