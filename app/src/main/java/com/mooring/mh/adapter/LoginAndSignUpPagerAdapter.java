package com.mooring.mh.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mooring.mh.R;
import com.mooring.mh.fragment.LoginFragment;
import com.mooring.mh.fragment.SignUpFragment;

/**
 * Created by Will on 16/3/30.
 */
public class LoginAndSignUpPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public LoginAndSignUpPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                return new LoginFragment();
            }
            case 1: {
                return new SignUpFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.title_login);
            case 1:
                return context.getResources().getString(R.string.title_sign_up);
        }
        return null;
    }
}
