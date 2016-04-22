package com.mooring.mh.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.mooring.mh.R;
import com.mooring.mh.adapter.MyAdapter;
import com.mooring.mh.model.TestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 16/3/24.
 */
public class TimingFragment extends BaseFragment {

    private int[] bgArr = {R.mipmap.img_heart_rate, R.mipmap.img_breathing_rate,
            R.mipmap.img_body_movement, R.mipmap.img_humidity, R.mipmap.img_temperature,
            R.mipmap.img_bed_temperature, R.mipmap.img_light, R.mipmap.img_noise};
    private int[] icArr = {R.mipmap.ic_heart_rate, R.mipmap.ic_breathing_rate,
            R.mipmap.ic_body_movement, R.mipmap.ic_humidity_icon, R.mipmap.ic_temperature,
            R.mipmap.ic_bed_temperature, R.mipmap.ic_light, R.mipmap.ic_noise};

    private RecyclerView param_recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter myAdapter;
    private List<TestData> datas;

    @Override
    protected int getLayoutId() {
        return R.layout.test;
    }


    @Override
    protected void initView() {
        Button btn_ceshi = (Button) rootView.findViewById(R.id.btn_ceshi);
        btn_ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.get(1).setData("low");
                datas.get(1).setUnit("%");
//                myAdapter.notifyDataSetChanged();
                myAdapter.notifyItemChanged(1);
            }
        });
        param_recyclerView = (RecyclerView) rootView.findViewById(R.id.param_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        param_recyclerView.setLayoutManager(layoutManager);
        param_recyclerView.setHasFixedSize(true);
        datas = new ArrayList<>();


        initData();
    }

    private void initData() {


        for (int i = 0; i < 8; i++) {

            TestData data = new TestData();
            data.setBgId(bgArr[i]);
            data.setIconId(icArr[i]);
            data.setData("71");
            data.setTitle("Heart rate");
            if (i != 1 || i != 2 || i != 6 || i != 7) {
                data.setUnit("BPM");
            } else {
                data.setUnit("");
            }
            datas.add(data);

        }

        myAdapter = new MyAdapter(datas);
        param_recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void lazyLoad() {

    }


}
