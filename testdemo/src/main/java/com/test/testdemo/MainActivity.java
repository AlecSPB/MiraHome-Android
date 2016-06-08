package com.test.testdemo;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    int backgroundColor;
    private View main_layout;
    private Button btn_color_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_layout = findViewById(R.id.main_layout);
        btn_color_anim = (Button) findViewById(R.id.btn_color_anim);

        btn_color_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator colorAnim = ObjectAnimator.ofInt(main_layout, "backgroundColor", 0xFFFF8080, 0xFF8080FF);
                colorAnim.setDuration(3000);
                colorAnim.setEvaluator(new ArgbEvaluator());
//                colorAnim.setRepeatCount(1);
//                colorAnim.setRepeatMode(ValueAnimator.REVERSE);
                colorAnim.start();
                colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Log.e("setBackgroundColor",animation.getCurrentPlayTime()+"______");
                    }
                });
            }
        });
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        main_layout.setBackgroundColor(backgroundColor);
//        Log.e("setBackgroundColor",backgroundColor+"______");

    }

}
