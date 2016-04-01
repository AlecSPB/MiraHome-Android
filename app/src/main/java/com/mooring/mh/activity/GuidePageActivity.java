package com.mooring.mh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.adapter.GuideViewPagerAdapter;
import com.mooring.mh.app.InitApplicationHelper;
import com.mooring.mh.utils.CommonUtils;
import com.mooring.mh.utils.MConstants;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by Will on 16/3/24.
 */
public class GuidePageActivity extends AppCompatActivity {
    // 定义ViewPager对象
    private ViewPager viewPager;

    // 定义ViewPager适配器
    private GuideViewPagerAdapter vpAdapter;

    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    // 定义底部小点图片
    private ImageView pointImage1, pointImage2, pointImage3, pointImage4;

    // 定义各个界面View对象
    private View view1, view2, view3, view4;

    // 定义开始按钮对象
    private TextView startBt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guidepage);

        if ("".equals(CommonUtils.getSP("token"))) {
            getToken();
        }

        initView();

        initData();
    }

    private void getToken() {
        RequestParams params = new RequestParams(MConstants.SERVICE_URL + MConstants.TOKEN);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result != null) {
                    JSONObject data = result.optJSONObject("data");
                    String token = data.optString("token");
                    CommonUtils.addSP("token", token);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(ex.getMessage(), ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(cex.getMessage(), cex);
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
            }
        });
    }

    private void initView() {
        // 实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.layout_guide_page_one, null);
        view2 = mLi.inflate(R.layout.layout_guide_page_two, null);
        view3 = mLi.inflate(R.layout.layout_guide_page_three, null);
        view4 = mLi.inflate(R.layout.layout_guide_page_four, null);

        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.guide_viewpager);

        // 实例化底部小点图片对象
        pointImage1 = (ImageView) findViewById(R.id.guide_one);
        pointImage2 = (ImageView) findViewById(R.id.guide_two);
        pointImage3 = (ImageView) findViewById(R.id.guide_three);
        pointImage4 = (ImageView) findViewById(R.id.guide_four);

        // 实例化开始按钮
        startBt = (TextView) view4.findViewById(R.id.guide_btn_open);
    }

    private void initData() {
        // 设置监听
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        // 实例化ViewPager适配器
        vpAdapter = new GuideViewPagerAdapter(views);
        // 设置适配器数据
        viewPager.setAdapter(vpAdapter);

        // 给开始按钮设置监听
        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton();
            }
        });
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    pointImage1.setImageResource(R.drawable.bg_circle_indicator_select);
                    pointImage2.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
                case 1:
                    pointImage2.setImageResource(R.drawable.bg_circle_indicator_select);
                    pointImage1.setImageResource(R.drawable.bg_circle_indicator_normal);
                    pointImage3.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
                case 2:
                    pointImage3.setImageResource(R.drawable.bg_circle_indicator_select);
                    pointImage2.setImageResource(R.drawable.bg_circle_indicator_normal);
                    pointImage4.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
                case 3:
                    pointImage4.setImageResource(R.drawable.bg_circle_indicator_select);
                    pointImage3.setImageResource(R.drawable.bg_circle_indicator_normal);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /**
     * 相应按钮点击事件
     */
    private void startButton() {
        SharedPreferences.Editor edit = InitApplicationHelper.sp.edit();
        edit.putBoolean("appFirstStart", false);
        edit.commit();
        Intent intent = new Intent();
        intent.setClass(GuidePageActivity.this, LoginAndSignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
