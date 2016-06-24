package com.test.testdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.test.testdemo.waitingdots.DotsTextView;

import java.util.Locale;

/**
 * Created by Will on 16/6/17.
 */
public class NoLayoutActivity extends AppCompatActivity {
    FrameLayout layout;
    ImageView view2, view1;
    AlphaAnimation a1, a2;
    ViewSwitcher viewSwitcher;
    TextView tv_ceshi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        layout = new FrameLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView view1 = new ImageView(this);
        view1.setImageResource(R.mipmap.splash_1_71080);
        view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view1.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(view1);
        view2 = new ImageView(this);
        view2.setImageResource(R.mipmap.splash_2_1080);
        view2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view2.setScaleType(ImageView.ScaleType.FIT_XY);
//        layout.addView(view2);

        a1 = new AlphaAnimation(1.0f, 0.05f);
        a1.setDuration(100);
        a1.setRepeatCount(0);
//        a1.start();


        a2 = new AlphaAnimation(0.05f, 1.0f);
        a2.setDuration(1000);
        a2.setRepeatCount(0);
//        a2.start();


        view1.setAnimation(a1);
        view2.setAnimation(a2);

        setContentView(layout);


        new Handler().postDelayed(new Runnable() {
            public void run() {

                a1.start();
                a2.start();
                layout.addView(view2);
            }
        }, 500);*/

//        viewSwitcher = new ViewSwitcher(this);
//        viewSwitcher.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        view1 = new ImageView(this);
//        view1.setImageResource(R.mipmap.splash_1_71080);
//        view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        view1.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        view2 = new ImageView(this);
//        view2.setImageResource(R.mipmap.splash_2_1080);
//        view2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        view2.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewSwitcher.addView(view1);
//        viewSwitcher.addView(view2);
//
//        setContentView(viewSwitcher);
//

        setContentView(R.layout.activity_no_layout);

//        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);

        a1 = new AlphaAnimation(1.0f, 0.05f);
        a1.setDuration(500);
        a1.setRepeatCount(0);

        a2 = new AlphaAnimation(0.05f, 1.0f);
        a2.setDuration(1800);
        a2.setRepeatCount(0);

//        viewSwitcher.setInAnimation(a2);
//        viewSwitcher.setOutAnimation(a1);

//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                viewSwitcher.showNext();
//            }
//        }, 1000);

        a2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view1 = (ImageView) findViewById(R.id.img_zhanshi);
        view1.startAnimation(a2);

//        new Handler().postDelayed(new Runnable() {
//            public void run() {
////                viewSwitcher.showNext();
//
//              view1.startAnimation(a2);
//            }
//        }, 1000);


        int num = 121;
        String str = String.format(Locale.getDefault(), "%02d", num);
        Log.e("asdasdasd", "str    " + str);

        tv_ceshi = (TextView) findViewById(R.id.tv_ceshi);
        tv_ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(tv_ceshi)) {
                    Log.e("equals", "equalsvequalsequalsequals");
                }
                if (v == tv_ceshi) {
                    Log.e("==", "== == == == == ");
                }
            }
        });


        dotsTextView = (DotsTextView) findViewById(R.id.dots);
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonHide = (Button) findViewById(R.id.buttonHide);
        buttonHideAndStop = (Button) findViewById(R.id.buttonHideAndStop);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dotsTextView.isPlaying()) {
                    dotsTextView.stop();
                } else {
                    dotsTextView.start();
                }
            }
        });

        buttonHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dotsTextView.isHide()) {
                    dotsTextView.show();
                } else {
                    dotsTextView.hide();
                }
            }
        });

        buttonHideAndStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dotsTextView.isHide()) {
                    dotsTextView.showAndPlay();
                } else {
                    dotsTextView.hideAndStop();
                }
            }
        });

    }


    DotsTextView dotsTextView;
    Button buttonPlay;
    Button buttonHide;
    Button buttonHideAndStop;

}
