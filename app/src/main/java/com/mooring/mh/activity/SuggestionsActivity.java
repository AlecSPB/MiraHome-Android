package com.mooring.mh.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mooring.mh.R;
import com.mooring.mh.utils.MUtils;

/**
 * 提交建议
 * <p/>
 * Created by Will on 16/4/14.
 */
public class SuggestionsActivity extends BaseActivity {

    private EditText EdTv_suggestion;
    private TextView tv_send;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_suggestions;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.title_suggestions);
    }

    @Override
    protected void initActivity() {
    }

    @Override
    protected void initView() {
        EdTv_suggestion = (EditText) findViewById(R.id.EdTv_suggestion);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
    }

    @Override
    protected void OnClick(View v) {
        String text = EdTv_suggestion.getText().toString().trim();
        if (v.getId() == R.id.tv_send) {
            if (!TextUtils.isEmpty(text)) {
                //发送建议
            } else {
                MUtils.showToast(context, getString(R.string.tip_suggest_empty));
            }
        }
    }
}
