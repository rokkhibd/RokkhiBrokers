package com.rokkhi.brokers.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.rokkhi.brokers.BaseActivity;
import com.rokkhi.brokers.Model.ViewPagerAdapter;
import com.rokkhi.brokers.R;

public class NoticeActivity extends BaseActivity {

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notice);

        viewPager=(ViewPager)findViewById(R.id.notice_viewpager);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new NoticeFragment()," ");

        viewPager.setAdapter(viewPagerAdapter);
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_notice;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.notice;
    }
}
