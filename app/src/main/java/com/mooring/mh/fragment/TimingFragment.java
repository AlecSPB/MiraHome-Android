package com.mooring.mh.fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ToggleButton;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKConstant;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.DvidStatus;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.activity.AlarmEditActivity;
import com.mooring.mh.adapter.AlarmClockAdapter;
import com.mooring.mh.adapter.OnRecyclerItemClickListener;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 闹钟fragment
 * <p>
 * Created by Will on 16/3/24.
 */
public class TimingFragment extends BaseFragment implements OnRecyclerItemClickListener {

    private RecyclerView param_recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AlarmClockAdapter adapter;
    private List<String> dataList;
    private BaseListener listener;
    private String deviceId;
    private String PropertyId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing;
    }


    @Override
    protected void initView() {

        listener = new BaseListener();
        deviceId = sp.getString(MConstants.DEVICE_ID, "");
        /*int location = ((MainActivity) context).getCurrentUser().get_location();
        if (location == 1) {
            PropertyId = "125";
        } else if (location == 2) {
            PropertyId = "126";
        }*/

        PropertyId = "125";

        param_recyclerView = (RecyclerView) rootView.findViewById(R.id.param_recyclerView);
        param_recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(context);
        param_recyclerView.setLayoutManager(layoutManager);
        param_recyclerView.setHasFixedSize(true);
        param_recyclerView.addItemDecoration(new AlarmClockAdapter.SpaceItemDecoration(
                CommonUtils.dp2px(getContext(), 5)));

        initData();
    }


    private void initData() {
        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
    }


    private void parsingAlarmString(String alarms) {
        LogUtil.i("此时最新的闹钟数据:    " + alarms);
        if (!TextUtils.isEmpty(alarms)) {
            String[] alarmArr = alarms.split(",");
            dataList = new ArrayList<>();
            for (int i = 0; i < alarmArr.length - 1; i++) {
                dataList.add(alarmArr[i]);
            }
        } else {
            dataList = new ArrayList<>();
            dataList.add("1122123456711");
            dataList.add("1530120056010");
        }
        adapter = new AlarmClockAdapter(dataList);
        adapter.setItemClickListener(this);
        param_recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected MachtalkSDKListener setSDKListener() {
        return listener;
    }


    @Override
    public void onItemClick(View view, int position) {
        ToggleButton smart = (ToggleButton) view.findViewById(R.id.tglBtn_wake_up);
        ToggleButton set = (ToggleButton) view.findViewById(R.id.tglBtn_clock_set);
        Intent it = new Intent();
        it.putExtra("flag", "edit");
        it.putExtra("time", dataList.get(position).substring(0, 4));
        it.putExtra("repeat", dataList.get(position).substring(4, 11));
        it.putExtra("smart", smart.isChecked());
        it.putExtra("set", set.isChecked());
        it.putExtra("position", position);
        it.setClass(context, AlarmEditActivity.class);
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
            if ("edit".equals(flag) && position != -1) {
                result += data.getStringExtra("time");
                result += paringArrayList(data.getStringArrayListExtra("repeat"));
                result += data.getBooleanExtra("smart", false) ? "1" : "0";
                result += data.getBooleanExtra("set", false) ? "1" : "0";
                dataList.set(position, result);
                dataList = sortArrayList(dataList);
                adapter.notifyDataSetChanged();
                CommonUtils.showToast(context, getResources().getString(R.string.clock_edit_success));
            }
            if ("add".equals(flag)) {
                result += data.getStringExtra("time");
                result += paringArrayList(data.getStringArrayListExtra("repeat"));
                result += data.getBooleanExtra("smart", false) ? "1" : "0";
                result += "1";
                dataList.add(result);
                dataList = sortArrayList(dataList);
                adapter.notifyDataSetChanged();
                CommonUtils.showToast(context, getResources().getString(R.string.clock_add_success));
            }
            if ("delete".equals(flag) && position != -1) {
                dataList.remove(position);
                adapter.notifyDataSetChanged();
                CommonUtils.showToast(context, getResources().getString(R.string.clock_delete_success));
            }

            sendAlarmString(dataList);
        }
    }

    /**
     * 发送改变后的闹钟
     *
     * @param dataList
     */
    private void sendAlarmString(List<String> dataList) {
        String temp = "";
        for (int i = 0; i < dataList.size(); i++) {
            temp += dataList.get(i) + ",";
        }
        if (!TextUtils.isEmpty(temp)) {
            temp += "^";
            MachtalkSDK.getInstance().operateDevice(deviceId,
                    new String[]{PropertyId},
                    new String[]{temp});
        }

    }

    /**
     * 自定义回调监听
     */
    class BaseListener extends MachtalkSDKListener {
        @Override
        public void onServerConnectStatusChanged(MachtalkSDKConstant.ServerConnStatus serverConnStatus) {
            super.onServerConnectStatusChanged(serverConnStatus);
            if (serverConnStatus == MachtalkSDKConstant.ServerConnStatus.LOGOUT_KICKOFF) {
                context.finish();
                return;
            }
        }

        @Override
        public void onQueryDeviceStatus(Result result, DeviceStatus deviceStatus) {
            super.onQueryDeviceStatus(result, deviceStatus);
            LogUtil.e("onQueryDeviceStatus  " + result.getSuccess());
            if (deviceStatus != null) {
                LogUtil.e("onQueryDeviceStatus  " + deviceStatus.getDeviceId());
                List<DvidStatus> list = deviceStatus.getDeviceDvidStatuslist();
                if (list != null) {
                    for (DvidStatus ds : list) {
                        LogUtil.e("DvidStatus  " + ds.getDvid() + "  value  " + ds.getValue());
                        if (PropertyId.equals(ds.getDvid())) {
                            parsingAlarmString(ds.getValue());
                        }
                    }
                }
            }
        }
    }
}
