package com.rokkhi.brokers.Ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.brokers.Model.FBuildings;
import com.rokkhi.brokers.Model.FWorkerBuilding;
import com.rokkhi.brokers.Model.TotalMeetingListAdapter;
import com.rokkhi.brokers.R;

import java.util.ArrayList;
import java.util.List;

public class TotalMeetingActivity extends AppCompatActivity {

    protected RecyclerView dueBldngRecycler;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userId;
    RecyclerView recyclerView;
    Context context;
    ProgressBar spinkitBar;
    String buildId;
    String amount;

    List<FWorkerBuilding> fBuildingsList;
    TotalMeetingListAdapter totalMeetingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_total_meeting);
        initView();

    }


    private void initView() {
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        context=getApplicationContext();

        dueBldngRecycler = (RecyclerView) findViewById(R.id.due_bldng_recycler);
        spinkitBar = (ProgressBar) findViewById(R.id.spin_kit);

        Wave wave=new Wave();
        spinkitBar.setIndeterminateDrawable(wave);

        fBuildingsList=new ArrayList<>();

        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        dueBldngRecycler.setLayoutManager(linearLayout);

        showTheBuildingList();

    }

    private void showTheBuildingList() {


        db.collection(context.getString(R.string.col_fWorkerBuilding)).whereEqualTo("f_uid",userId).whereEqualTo("status_id","rUyWv6FLEgZ0EIB6aNNP")
                .whereEqualTo("status_id","cQ7jmazM2pAoMWtL213L").whereEqualTo("status_id","lACNetniNe4gjp6nBvWP").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fBuildingsList.clear();

                    for (DocumentSnapshot doc:task.getResult()){
                        FWorkerBuilding fb=doc.toObject(FWorkerBuilding.class);
                        fBuildingsList.add(fb);

                    }

                    Log.e("TAG","size:"+fBuildingsList.size());

                    totalMeetingListAdapter=new TotalMeetingListAdapter(fBuildingsList,context);
                    totalMeetingListAdapter.setHasStableIds(true);
                    dueBldngRecycler.setAdapter(totalMeetingListAdapter);


                    spinkitBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
