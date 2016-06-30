package com.mooring.mh.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.machtalk.sdk.connect.MachtalkSDK;
import com.machtalk.sdk.connect.MachtalkSDKListener;
import com.machtalk.sdk.domain.DeviceStatus;
import com.machtalk.sdk.domain.DvidStatus;
import com.machtalk.sdk.domain.ReceivedDeviceMessage;
import com.machtalk.sdk.domain.Result;
import com.mooring.mh.R;
import com.mooring.mh.activity.AlarmEditActivity;
import com.mooring.mh.adapter.AlarmClockAdapter;
import com.mooring.mh.adapter.OnRecyclerItemClickListener;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.utils.MUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 闹钟fragment
 * <p>
 * Created by Will on 16/3/24.
 */
public class TimingFragment extends BaseFragment implements OnRecyclerItemClickListener,
        SwitchUserObserver, SwipeRefreshLayout.OnRefreshListener, AlarmClockAdapter.OnToggleBtnChange {

    private SwipeRefreshLayout swipe_refresh;//下拉刷新
    private RecyclerView param_recyclerView;//闹钟列表
    private View layout_alarm_none;//无闹钟界面
    private RecyclerView.LayoutManager layoutManager;//RecycleView对应的布局管理器
    private AlarmClockAdapter adapter;
    private List<String> dataList;//闹钟数据
    private TimingSDKListener timingSDKListener;
    private int currLocation;//当前位置
    private String propertyId;//左右属性ID--对应125:126
    private boolean isOperate = false;//是否是手动操作

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timing;
    }

    @Override
    protected void initFragment() {
        dataList = new ArrayList<>();
        timingSDKListener = new TimingSDKListener();
        currLocation = sp.getInt(MConstants.CURR_USER_LOCATION, MConstants.LEFT_USER);
        propertyId = (currLocation == MConstants.LEFT_USER) ?
                MConstants.ATTR_ALARM_LEFT : MConstants.ATTR_ALARM_RIGHT;
    }

    @Override
    protected void initView() {

        swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        layout_alarm_none = rootView.findViewById(R.id.layout_alarm_none);
        param_recyclerView = (RecyclerView) rootView.findViewById(R.id.param_recyclerView);

        swipe_refresh.setColorSchemeResources(R.color.colorWhite50);
        swipe_refresh.setProgressBackgroundColorSchemeResource(R.color.colorPurple);
        swipe_refresh.setOnRefreshListener(this);
        param_recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(context);
        param_recyclerView.setLayoutManager(layoutManager);
        param_recyclerView.setHasFixedSize(true);
        param_recyclerView.addItemDecoration(new AlarmClockAdapter.SpaceItemDecoration(
                MUtils.dp2px(getContext(), 5)));

        swipe_refresh.setProgressViewOffset(true, 0, MUtils.dp2px(context, 30));
        swipe_refresh.setRefreshing(true);
        MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
    }

    /**
     * 解析闹钟数据
     *
     * @param alarms
     */
    private void parsingAlarmString(String alarms) {
        if (!TextUtils.isEmpty(alarms) || !"mirahome".equals(alarms)) {
            dataList.clear();
            String[] alarmArr = alarms.split(";");
            for (int i = 0; i < alarmArr.length - 1; i++) {
                dataList.add(alarmArr[i]);
            }
            swipe_refresh.setVisibility(View.VISIBLE);
            layout_alarm_none.setVisibility(View.GONE);
            adapter = new AlarmClockAdapter(dataList);
            adapter.setItemClickListener(this);
            adapter.setOnToggleBtnChange(this);
            param_recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            swipe_refresh.setVisibility(View.INVISIBLE);
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
     * @param dataList
     */
    private void sendAlarmString(List<String> dataList) {
        String temp = "";
        for (int i = 0; i < dataList.size(); i++) {
            temp += dataList.get(i) + ";";
        }
        if (!TextUtils.isEmpty(temp)) {
            temp += "^";
            MachtalkSDK.getInstance().operateDevice(deviceId,
                    new String[]{propertyId},
                    new String[]{temp});
            isOperate = true;
        }
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
            LogUtil.e("onQueryDeviceStatus  " + success);
            if (success == Result.SUCCESS && ds != null && deviceId.equals(ds.getDeviceId())) {
                List<DvidStatus> list = ds.getDeviceDvidStatuslist();
                if (list != null) {
                    for (DvidStatus DS : list) {
                        if (propertyId.equals(DS.getDvid())) {
                            LogUtil.w("所有闹钟:   " + DS.getValue());
                            parsingAlarmString(DS.getValue());
                        }
                    }
                }
            } else {
                MUtils.showToast(context, context.getString(R.string.device_not_online));
            }
            swipe_refresh.setRefreshing(false);
        }

        @Override
        public void onReceiveDeviceMessage(Result result, ReceivedDeviceMessage rdm) {
            super.onReceiveDeviceMessage(result, rdm);
            if (!isOperate) {
                return;
            }
            int success = Result.FAILED;
            if (result != null) {
                success = result.getSuccess();
            }
            LogUtil.e("onReceiveDeviceMessage  " + success);
            if (success == Result.SUCCESS && rdm != null && deviceId.equals(rdm.getDeviceId())) {
                if (rdm.getDvidStatusList() != null &&
                        propertyId.equals(rdm.getDvidStatusList().get(0).getDvid())) {
                    LogUtil.w("  操作成功   ");
                }
            } else {
                LogUtil.w("  操作失败   ");
            }
            isOperate = false;
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
            //判断执行切换
            if (currLocation != sp.getInt(MConstants.CURR_USER_LOCATION, MConstants.LEFT_USER)) {
                currLocation = sp.getInt(MConstants.CURR_USER_LOCATION, MConstants.LEFT_USER);
                propertyId = (currLocation == MConstants.LEFT_USER) ? MConstants.ATTR_ALARM_LEFT
                        : MConstants.ATTR_ALARM_RIGHT;
                MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
            }
        }
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
                MUtils.showToast(context, getResources().getString(R.string.clock_edit_success));
            }
            if ("add".equals(flag)) {
                result += data.getStringExtra("time");
                result += paringArrayList(data.getStringArrayListExtra("repeat"));
                result += data.getBooleanExtra("smart", false) ? "1" : "0";
                result += "1";
                dataList.add(result);
                dataList = sortArrayList(dataList);
                adapter.notifyDataSetChanged();
                MUtils.showToast(context, getResources().getString(R.string.clock_add_success));
            }
            if ("delete".equals(flag) && position != -1) {
                dataList.remove(position);
                adapter.notifyDataSetChanged();
                MUtils.showToast(context, getResources().getString(R.string.clock_delete_success));
                if (dataList.size() <= 0) {
                    swipe_refresh.setVisibility(View.INVISIBLE);
                    layout_alarm_none.setVisibility(View.VISIBLE);
                }
            }

            sendAlarmString(dataList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MachtalkSDK.getInstance().setContext(context);
        MachtalkSDK.getInstance().setSdkListener(timingSDKListener);
        MobclickAgent.onPageStart("Timing");
    }

    @Override
    public void onPause() {
        super.onPause();
        MachtalkSDK.getInstance().removeSdkListener(timingSDKListener);
        MobclickAgent.onPageEnd("Timing");
    }

    @Override
    public void onSwitch(String userId, int location, String fTag) {
        if (fTag.equals(this.getTag())) {
            currLocation = location;
            propertyId = (currLocation == MConstants.LEFT_USER) ? MConstants.ATTR_ALARM_LEFT
                    : MConstants.ATTR_ALARM_RIGHT;
            MachtalkSDK.getInstance().queryDeviceStatus(deviceId);
        }
    }
}
