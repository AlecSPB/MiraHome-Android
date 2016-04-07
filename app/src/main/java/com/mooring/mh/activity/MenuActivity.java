package com.mooring.mh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mooring.mh.R;
import com.mooring.mh.adapter.DataAdapter;
import com.mooring.mh.model.ImageData;
import com.mooring.mh.utils.MConstants;
import com.mooring.mh.views.CustomImageView.ZoomCircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单Activity
 * <p/>
 * Created by Will on 16/4/7.
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView menu_recyclerView;//横向滑动view
    private ImageView imgView_switch_user;//切换用户按钮
    private View layout_connect_moooring;//重新连接view
    private View layout_connected_device;//一连上设备
    private ZoomCircleView zcView_left; // 左边用户头像
    private ImageView imgView_delete_left;//左边删除用户图标
    private ZoomCircleView zcView_right;//右边用户头像
    private ImageView imgView_delete_right;//右边删除用户图标
    private View layout_device; // device设备连接状态
    private TextView tv_connect_health; // 链接Health kit
    private TextView tv_about_text; // 关于我们
    private TextView tv_suggestions_text;// 建议
    private TextView tv_login_out;//退出登陆


    private RecyclerView.LayoutManager layoutManager;
    private List<ImageData> dataList;
    private DataAdapter<ImageData> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();

        initData();
    }

    private void initView() {
        imgView_switch_user = (ImageView) findViewById(R.id.imgView_switch_user);
        layout_connect_moooring = findViewById(R.id.layout_connect_moooring);
        layout_connected_device = findViewById(R.id.layout_connected_device);
        zcView_left = (ZoomCircleView) findViewById(R.id.zcView_left);
        imgView_delete_left = (ImageView) findViewById(R.id.imgView_delete_left);
        zcView_right = (ZoomCircleView) findViewById(R.id.zcView_left);
        imgView_delete_right = (ImageView) findViewById(R.id.imgView_delete_right);
        layout_device = findViewById(R.id.zcView_left);
        tv_connect_health = (TextView) findViewById(R.id.tv_connect_health);
        tv_about_text = (TextView) findViewById(R.id.tv_about_text);
        tv_suggestions_text = (TextView) findViewById(R.id.tv_suggestions_text);
        tv_login_out = (TextView) findViewById(R.id.tv_login_out);


        menu_recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerView);
        menu_recyclerView.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        menu_recyclerView.setLayoutManager(layoutManager);

        imgView_switch_user.setOnClickListener(this);
        layout_connect_moooring.setOnClickListener(this);
        imgView_delete_left.setOnClickListener(this);
        imgView_delete_right.setOnClickListener(this);
        tv_connect_health.setOnClickListener(this);
        tv_about_text.setOnClickListener(this);
        tv_suggestions_text.setOnClickListener(this);
        tv_login_out.setOnClickListener(this);

    }

    private void initData() {
        dataList = new ArrayList<ImageData>();
        for (int i = 0; i < 3; i++) {
            ImageData d = new ImageData("Alex", Environment.getExternalStorageDirectory().getPath() + "/feidieshuo/Cache/Userface/userheads/11223.jpg");
            dataList.add(d);
        }
        adapter = new DataAdapter<ImageData>(this, dataList, R.layout.horizontal_scroll_item, R.id.imgView_horizontal_item,
                R.id.tv_horizontal_item, getResources());
        adapter.setOnClickListener(new DataAdapter.OnClickListener<ImageData>() {
            @Override
            public void onClick(ImageData data) {
                Toast.makeText(MenuActivity.this, data.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        menu_recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MConstants.ADD_USER_REQUEST && resultCode == MConstants.ADD_USER_RESULT) {
            //此时是添加用户回传

            //执行列表更新
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_switch_user:

                break;
            case R.id.layout_connect_moooring:

                break;
            case R.id.imgView_delete_left:

                break;
            case R.id.imgView_delete_right:

                break;
            case R.id.tv_connect_health:

                break;
            case R.id.tv_about_text:

                break;
            case R.id.tv_suggestions_text:

                break;
            case R.id.tv_login_out:

                break;
        }
    }
}
