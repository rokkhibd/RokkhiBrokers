package com.rokkhi.brokers.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.rokkhi.brokers.BaseActivity;
import com.rokkhi.brokers.MainActivity;
import com.rokkhi.brokers.Model.ViewPagerAdapter;
import com.rokkhi.brokers.R;

public class MyHomeActivity extends BaseActivity {

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_home);
        viewPager=(ViewPager)findViewById(R.id.home_viewpager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MyhomeFragment()," ");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_home;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.my_home;
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(MyHomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
