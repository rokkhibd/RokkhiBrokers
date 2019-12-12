package com.rokkhi.brokers.Ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.brokers.Model.FWorkerBuilding;
import com.rokkhi.brokers.Model.LiveBuildingListAdapter;
import com.rokkhi.brokers.R;


import java.util.ArrayList;
import java.util.List;

public class UsersLiveBuildingActivity extends AppCompatActivity {

    ProgressBar spinKit;
    RecyclerView liveBuildingRecyclerview;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userId;
    List<FWorkerBuilding> fWorkerBuildingList;
    LiveBuildingListAdapter liveBuildingListAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_users_live_building);
        initView();


    }

    private void initView() {

        context = getApplicationContext();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        liveBuildingRecyclerview = (RecyclerView) findViewById(R.id.live_building_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        liveBuildingRecyclerview.setLayoutManager(linearLayoutManager);

        fWorkerBuildingList=new ArrayList<>();

        showLiveBuildingList();
        spinKit = (ProgressBar) findViewById(R.id.spin_kit);

        Wave wave=new Wave();
        spinKit.setIndeterminateDrawable(wave);
    }

    private void showLiveBuildingList() {

        // db.collection(getString(R.string.col_paymentHistory)).whereEqualTo("f_id",userId)

        db.collection(getString(R.string.col_fWorkerBuilding)).whereEqualTo("f_uid", userId).whereEqualTo("status_id", "lACNetniNe4gjp6nBvWP").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            fWorkerBuildingList.clear();

                            for (DocumentSnapshot doc : task.getResult()) {
                                FWorkerBuilding fWorkerBuilding = doc.toObject(FWorkerBuilding.class);
                                fWorkerBuildingList.add(fWorkerBuilding);
                            }

                            liveBuildingListAdapter = new LiveBuildingListAdapter(fWorkerBuildingList, context);
                            liveBuildingListAdapter.setHasStableIds(true);
                            liveBuildingRecyclerview.setAdapter(liveBuildingListAdapter);

                            spinKit.setVisibility(View.GONE);
                        }
                    }
                });

    }
}
