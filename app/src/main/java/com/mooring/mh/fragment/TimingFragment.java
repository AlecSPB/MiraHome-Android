package com.mooring.mh.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.AidStatus;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.activity.AlarmEditActivity;
import com.mooring.mh.activity.SetWifiActivity;
import com.mooring.mh.adapter.AlarmClockAdapter;
import com.mooring.mh.adapter.OnRecyclerItemClickListener;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 闹钟fragment
 * <p/>
 * Created by Will on 16/3/24.
 */
public class TimingFragment extends BaseFragment implements AlarmClockAdapter.OnToggleBtnChange,
        SwitchUserObserver, SwipeRefreshLayout.OnRefreshListener, OnRecyclerItemClickListener {

    private View layout_timing;//闹钟界面
    private View layout_no_device;//无设备界面

    private SwipeRefreshLayout timing_swipeRefresh;//下拉刷新
    private RecyclerView timing_recyclerView;//闹钟列表
    private View layout_alarm_none;//无闹钟界面
    private RecyclerView.LayoutManager layoutManager;//RecycleView对应的布局管理器
    private AlarmClockAdapter adapter;
    private List<String> dataList;//闹钟数据
    private TimingSDKListener timingSDKListener;
    private int currLocation;//当前位置
    private String propertyId;//左右属性ID--对应125:126
    private int isDeviceExist = 0;//设备是否存在,0:默认,1:存在,2:不存在
    private String resultFlag;//onActivityResult回调

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing;
    }

    @Override
    protected void initFragment() {
        dataList = new ArrayList<>();
        timingSDKListener = new TimingSDKListener();
        currLocation = MUtils.getCurrUserLocation();
        propertyId = (currLocation == MConstants.LEFT_USER) ?
                MConstants.ATTR_ALARM_LEFT : MConstants.ATTR_ALARM_RIGHT;
    }

    @Override
    protected void initView() {

        layout_timing = rootView.findViewById(R.id.layout_timing);
        timing_swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.timing_swipeRefresh);
        layout_alarm_none = rootView.findViewById(R.id.layout_alarm_none);
        timing_recyclerView = (RecyclerView) rootView.findViewById(R.id.timing_recyclerView);

        timing_swipeRefresh.setColorSchemeResources(R.color.colorWhite50);
        timing_swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.colorPurple);
        timing_swipeRefresh.setOnRefreshListener(this);
        timing_recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(context);
        timing_recyclerView.setLayoutManager(layoutManager);
        timing_recyclerView.setHasFixedSize(true);
        timing_recyclerView.addItemDecoration(new AlarmClockAdapter.SpaceItemDecoration(
                MUtils.dp2px(getContext(), 5)));

        adapter = new AlarmClockAdapter(dataList);
        adapter.setItemClickListener(this);
        adapter.setOnToggleBtnChange(this);
        timing_recyclerView.setAdapter(adapter);
    }

    /**
     * 判断设备是否在线
     */
    private boolean judgeDeviceIsOnline() {
        if (MUtils.isCurrDeviceOnline()) {
            hideNoDeviceView();
            isDeviceExist = 1;
            return true;
        } else {
            showNoDeviceView();
            isDeviceExist = 2;
            return false;
        }
    }

    /**
     * 显示无设备去连接界面
     */
    private void showNoDeviceView() {
        if (isDeviceExist == 2) return;
        layout_timing.setVisibility(View.GONE);
        if (layout_no_device == null) {
            ViewStub viewStub = (ViewStub) rootView.findViewById(R.id.VStub_no_device);
            layout_no_device = viewStub.inflate();
        } else {
            layout_no_device.setVisibility(View.VISIBLE);
        }
        View view = rootView.findViewById(R.id.no_device_to_conn);
        View imgView_device_connect = view.findViewById(R.id.imgView_device_connect);
        imgView_device_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //连接设备
                context.startActivity(new Intent(context, SetWifiActivity.class));
            }
        });
    }

    /**
     * 隐藏无设备去连接界面
     */
    private void hideNoDeviceView() {
        if (isDeviceExist == 1) return;
        layout_timing.setVisibility(View.VISIBLE);
        if (layout_no_device != null) {
            layout_no_device.setVisibility(View.GONE);
        }

        timing_swipeRefresh.setProgressViewOffset(true, 0, MUtils.dp2px(context, 30));
        timing_swipeRefresh.setRefreshing(true);
        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
    }

    /**
     * 解析闹钟数据
     *
     * @param alarms 1120111111101,^
     */
    private void parsingAlarmString(String alarms) {
        LogUtil.w("所有闹钟___" + alarms + "___中间是什么");
        if (!TextUtils.isEmpty(alarms) && !getString(R.string.tv_empty_clock).equals(alarms)) {
            dataList.clear();
            String[] alarmArr = alarms.split(";");

            dataList.addAll(Arrays.asList(alarmArr));
            dataList.remove(dataList.size() - 1);
            adapter.notifyDataSetChanged();
            timing_swipeRefresh.setVisibility(View.VISIBLE);
            layout_alarm_none.setVisibility(View.GONE);
        } else {
            timing_swipeRefresh.setVisibility(View.INVISIBLE);
            layout_alarm_none.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ArrayList  to  String
     * 闹钟重复list->String
     *
     * @param list
     * @return
     */
    private String paringArrayList(ArrayList<String> list) {
        String result = "";
        if (list != null) {
            for (String s : list) {
                result += s;
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
        int temp;
        String str;
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

    /**
     * 发送改变后的闹钟
     *
     * @param dataList 闹钟列表
     */
    private void sendAlarmString(List<String> dataList) {
        String temp = "";
        if (dataList.size() <= 0) {
            temp = getString(R.string.tv_empty_clock);
        } else {
            for (String data : dataList) {
                temp += data + ";";
            }
            temp += "^";
        }
        MachtalkSDK.getInstance().operateDevice(deviceId,
                new String[]{propertyId},
                new String[]{temp});
        MUtils.showLoadingDialog(context, null);
    }

    @Override
    public void onRefresh() {
        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
    }

    @Override
    public void onChanged(CompoundButton cb, boolean isChecked, int position) {
        String temp = dataList.get(position);
        switch (cb.getId()) {
            case R.id.tglBtn_wake_up:
                temp = temp.substring(0, 11) + (isChecked ? "1" : "0") + temp.substring(12);
                break;
            case R.id.tglBtn_clock_set:
                temp = temp.substring(0, 12) + (isChecked ? "1" : "0");
                break;
        }
        dataList.set(position, temp);
        sendAlarmString(dataList);
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
     * 自定义回调监听
     */
    class TimingSDKListener extends MachtalkSDKListener {
        @Override
        public void onQueryDeviceStatus(Result result, DeviceStatus ds) {
            super.onQueryDeviceStatus(result, ds);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && ds != null && deviceId.equals(ds.getDeviceId())) {
                List<AidStatus> listAid = ds.getDeviceAidStatuslist();
                if (listAid == null) return;
                for (AidStatus as : listAid) {
                    if (propertyId.equals(as.getAid())) {
                        parsingAlarmString(as.getValue());
                        timing_swipeRefresh.setRefreshing(false);
                        return;
                    }
                }
            } else {
                timing_swipeRefresh.setRefreshing(false);
                MUtils.showToast(context, getString(R.string.check_device_online));
            }
        }

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            if (success == Result.SUCCESS && rdm != null && deviceId.equals(rdm.getDeviceId())) {
                if (!rdm.isRespMsg()) return;
                MUtils.hideLoadingDialog();
                List<AidStatus> listAid = rdm.getAidStatusList();
                if (listAid == null) return;
                for (AidStatus as : listAid) {
                    if (propertyId.equals(as.getAid())) {
                        adapter.notifyDataSetChanged();
                        if (resultFlag.equals("edit")) {
                            MUtils.showToast(context, getString(R.string.clock_edit_success));
                        }
                        if (resultFlag.equals("add")) {
                            if (timing_swipeRefresh.getVisibility() == View.INVISIBLE) {
                                timing_swipeRefresh.setVisibility(View.VISIBLE);
                                layout_alarm_none.setVisibility(View.GONE);
                            }
                            MUtils.showToast(context, getString(R.string.clock_add_success));
                        }
                        if (resultFlag.equals("delete")) {
                            if (dataList.size() <= 0) {
                                timing_swipeRefresh.setVisibility(View.INVISIBLE);
                                layout_alarm_none.setVisibility(View.VISIBLE);
                            }
                            MUtils.showToast(context, getString(R.string.clock_delete_success));
                        }
                        resultFlag = "";
                    } else {
                        MUtils.showToast(context, getString(R.string.operate_failed));
                    }
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            MachtalkSDK.getInstance().removeSdkListener(timingSDKListener);
        } else {
            MachtalkSDK.getInstance().setContext(context);
            MachtalkSDK.getInstance().setSdkListener(timingSDKListener);

            //判断设备是否在线
            if (!judgeDeviceIsOnline()) return;

            //判断执行切换
            if (currLocation != MUtils.getCurrUserLocation()) {
                currLocation = MUtils.getCurrUserLocation();
                propertyId = (currLocation == MConstants.LEFT_USER) ? MConstants.ATTR_ALARM_LEFT
                        : MConstants.ATTR_ALARM_RIGHT;
                MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
                return;
            }

            //查询无结果时,再次执行查询
            if (layout_alarm_none.getVisibility() == View.VISIBLE) {
                MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
            }
        }
    }

    @Override
    public void onSwitch(String userId, int location, String fTag) {
        if (!isVisible()) return;
        if (!TextUtils.isEmpty(userId)) {//切换头像时
            if (!MUtils.isCurrDeviceOnline()) return;
            currLocation = location;
            propertyId = (currLocation == MConstants.LEFT_USER) ? MConstants.ATTR_ALARM_LEFT
                    : MConstants.ATTR_ALARM_RIGHT;
            MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
            MUtils.showLoadingDialog(context, null);
        } else {
            if (location == MConstants.OBSERVER_DEVICE_STATUS) {
                if (fTag.equals(MConstants.DEVICE_ONLINE + "")) {
                    hideNoDeviceView();
                    isDeviceExist = 1;
                } else if (fTag.equals(MConstants.DEVICE_OFFLINE + "")) {
                    showNoDeviceView();
                    isDeviceExist = 2;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.ALARM_EDIT_REQUEST && resultCode == MConstants.ALARM_EDIT_RESULT) {
            resultFlag = data.getStringExtra("flag");
            int position = data.getIntExtra("position", -1);
            String result = "";
            if ("edit".equals(resultFlag) && position != -1) {
                result += data.getStringExtra("time");
                result += paringArrayList(data.getStringArrayListExtra("repeat"));
                result += data.getBooleanExtra("smart", false) ? "1" : "0";
                result += data.getBooleanExtra("set", false) ? "1" : "0";
                dataList.set(position, result);
                if (dataList.size() > 1) {
                    dataList = sortArrayList(dataList);
                }
            } else if ("add".equals(resultFlag)) {
                result += data.getStringExtra("time");
                result += paringArrayList(data.getStringArrayListExtra("repeat"));
                result += data.getBooleanExtra("smart", false) ? "1" : "0";
                result += "1";
                dataList.add(result);
                if (dataList.size() > 1) {
                    dataList = sortArrayList(dataList);
                }
                for (String da : dataList) {
                    LogUtil.i("____闹钟_____" + da + "_____" + dataList.size());
                }
            } else if ("delete".equals(resultFlag) && position != -1) {
                dataList.remove(position);
                adapter.notifyDataSetChanged();
            }

            sendAlarmString(dataList);
        }
    }

    @Override
    protected void OnResume() {
        //判断设备是否在线
        judgeDeviceIsOnline();

        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(timingSDKListener);
        MobclickAgent.onPageStart("Timing");
    }

    @Override
    protected void OnPause() {
        MachtalkSDK.getInstance().removeSdkListener(timingSDKListener);
        MobclickAgent.onPageEnd("Timing");
    }
}
