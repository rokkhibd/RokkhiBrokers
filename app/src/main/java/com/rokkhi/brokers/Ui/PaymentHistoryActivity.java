package com.rokkhi.brokers.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.rokkhi.brokers.BaseActivity;
import com.rokkhi.brokers.Model.ViewPagerAdapter;
import com.rokkhi.brokers.R;

public class PaymentHistoryActivity extends BaseActivity {

    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_payment_history);

        viewPager=(ViewPager)findViewById(R.id.payment_viewpager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PaymentHistoryFragment()," ");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_payment_history;
    }

    @Override
    protected int getNavigationMenuItemId() {
        return R.id.payment;
    }
}
