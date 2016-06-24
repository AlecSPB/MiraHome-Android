package com.test.testdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.test.testdemo.CircleProgress.CircleProgress;

/**
 * Created by Will on 16/6/24.
 */
public class TestActivity3 extends AppCompatActivity implements View.OnClickListener{

    private CircleProgress mProgressView;
    private View mStartBtn;
    private View mStopBtn;
    private View mResetBtn;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_3);

        mProgressView = (CircleProgress) findViewById(R.id.progress);
        mProgressView.startAnim();
        mStartBtn = findViewById(R.id.start_btn);
        mStartBtn.setOnClickListener(this);
        mStopBtn = findViewById(R.id.stop_btn);
        mStopBtn.setOnClickListener(this);
        mResetBtn = findViewById(R.id.reset_btn);
        mResetBtn.setOnClickListener(this);

        SeekBar mSeekBar = (SeekBar) findViewById(R.id.out_seek);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float factor = seekBar.getProgress() / 100f;
                mProgressView.setRadius(factor);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mStartBtn) {
            mProgressView.startAnim();
        } else if (v == mStopBtn) {
            mProgressView.stopAnim();
        } else if (v == mResetBtn) {
            mProgressView.reset();
        }
    }
}
