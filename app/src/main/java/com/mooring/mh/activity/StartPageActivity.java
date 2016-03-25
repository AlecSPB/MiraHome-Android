package com.mooring.mh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mooring.mh.R;
import com.mooring.mh.app.InitApplicationHelper;

/**
 * Created by Will on 16/3/24.
 */
public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_startpage);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    Intent intent = new Intent();
                    Log.e("111",InitApplicationHelper.sp.getBoolean("appFirstStart", true)+"");
                    if (InitApplicationHelper.sp.getBoolean("appFirstStart", true)) {
                        // 引导页
                        intent.setClass(StartPageActivity.this, GuidePageActivity.class);
                    } else {
                        // 启动首页
                        intent.setClass(StartPageActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    StartPageActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 300);
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
