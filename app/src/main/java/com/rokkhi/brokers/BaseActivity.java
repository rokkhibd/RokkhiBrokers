package com.rokkhi.brokers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rokkhi.brokers.Ui.MyHomeActivity;
import com.rokkhi.brokers.Ui.NoticeActivity;
import com.rokkhi.brokers.Ui.PaymentHistoryActivity;
import com.rokkhi.brokers.Ui.ProfileActivity;


import javax.annotation.Nullable;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.postDelayed(()-> {
            int itemId = item.getItemId();
            if (itemId == R.id.my_home) {
                startActivity(new Intent(this, MyHomeActivity.class));
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }else if(itemId==R.id.payment){
                startActivity(new Intent(this, PaymentHistoryActivity.class));
            }else if (itemId==R.id.notice){
                startActivity(new Intent(this, NoticeActivity.class));
            }
            finish();
        }, 300);
        return true;
    }




    private void updateNavigationBarState() {

        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);

    }

    //protected abstract void selectBottomNavigationBarItem(int actionId);

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = bottomNavigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
     protected abstract int getContentViewId();

    protected abstract int getNavigationMenuItemId();
}
