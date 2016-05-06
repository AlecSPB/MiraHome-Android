package com.mooring.mh.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mooring.mh.R;
import com.mooring.mh.activity.AlarmEditActivity;
import com.mooring.mh.adapter.AlarmClockAdapter;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

import java.util.ArrayList;
import java.util.List;

import static com.mooring.mh.adapter.AlarmClockAdapter.OnRecyclerItemClickListener;

/**
 * 闹钟fragment
 * <p/>
 * Created by Will on 16/3/24.
 */
public class TimingFragment extends BaseFragment implements OnRecyclerItemClickListener {

    private RecyclerView param_recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AlarmClockAdapter adapter;
    private List<String> dataList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing;
    }


    @Override
    protected void initView() {
        param_recyclerView = (RecyclerView) rootView.findViewById(R.id.param_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        param_recyclerView.setLayoutManager(layoutManager);
        param_recyclerView.setHasFixedSize(true);
        param_recyclerView.addItemDecoration(new AlarmClockAdapter.SpaceItemDecoration(
                CommonUtils.dp2px(getContext(), 5)));

        initData();
    }

    private void initData() {
        dataList = new ArrayList<>();
        dataList.add("0800003450711");
        dataList.add("0830123450711");
        dataList.add("0900003450711");
        dataList.add("1022023450710");
        dataList.add("1111003456710");
        dataList.add("1300003450711");
        adapter = new AlarmClockAdapter(getActivity(), dataList);
        adapter.setItemClickListener(this);
        param_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void lazyLoad() {

    }


    @Override
    public void onItemClick(View view, int position) {
        Intent it = new Intent();
        it.putExtra("flag", "edit");
        it.putExtra("time", dataList.get(position).substring(0, 4));
        it.putExtra("repeat", dataList.get(position).substring(4, 11));
        it.putExtra("smart", adapter.getSmartWithPosition(position));
        it.putExtra("position", position);
        it.setClass(getActivity(), AlarmEditActivity.class);
        startActivityForResult(it, MConstants.ALARM_EDIT_REQUEST);
    }

    /**
     * ArrayList  to  String
     *
     * @param list
     * @return
     */
    private String paringArrayList(ArrayList<String> list) {
        String result = "";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                result += list.get(i);
            }
        }
        return result;
    }

    /**
     * 对现有闹钟进行排序处理
     *
     * @param list
     * @return
     */
    private List<String> sortArrayList(List<String> list) {
        int a[] = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            a[i] = Integer.parseInt(list.get(i).substring(0, 4));
        }
        int temp = 0;
        String str = "";
        for (int i = a.length - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                if (a[j + 1] < a[j]) {
                    temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;

                    str = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, str);
                }
            }
        }
        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MConstants.ALARM_EDIT_REQUEST && resultCode == MConstants.ALARM_EDIT_RESULT) {
            String flag = data.getStringExtra("flag");
            int position = data.getIntExtra("position", -1);
            String result = "";
            if ("edit".equals(flag)) {
                if (position != -1) {
                    result += data.getStringExtra("time");
                    result += paringArrayList(data.getStringArrayListExtra("repeat"));
                    result += data.getBooleanExtra("smart", false) ? "1" : "0";
                    result += adapter.getSwitchWithPosition(position) ? "1" : "0";
                    dataList.set(position, result);
                    dataList = sortArrayList(dataList);
                    adapter.setSmartAndSwitchList(dataList);
                    adapter.notifyDataSetChanged();
                }
            }
            if ("add".equals(flag)) {
                result += data.getStringExtra("time");
                result += paringArrayList(data.getStringArrayListExtra("repeat"));
                result += data.getBooleanExtra("smart", false) ? "1" : "0";
                result += "1";
                dataList.add(result);
                dataList = sortArrayList(dataList);
                adapter.setSmartAndSwitchList(dataList);
                adapter.notifyDataSetChanged();
            }
            if ("delete".equals(flag)) {
                dataList.remove(position);
                adapter.setSmartAndSwitchList(dataList);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
