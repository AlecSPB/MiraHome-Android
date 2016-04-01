package com.mooring.mh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mooring.mh.fragment.LoginFragment;
import com.mooring.mh.fragment.SignUpFragment;

/**
 * Created by Will on 16/3/30.
 */
public class LoginAndSignUpPagerAdapter extends FragmentPagerAdapter {
    public LoginAndSignUpPagerAdapter(FragmentManager fm) {
        super(fm);
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
                return "Login";
            case 1:
                return "Sign Up";
        }
        return null;
    }
}
