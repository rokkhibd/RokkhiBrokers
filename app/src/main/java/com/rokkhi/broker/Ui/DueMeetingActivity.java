package com.rokkhi.broker.Ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.broker.Model.DueMeetingListAdapter;
import com.rokkhi.broker.Model.FWorkerBuilding;
import com.rokkhi.broker.R;

import java.util.ArrayList;
import java.util.List;

public class DueMeetingActivity extends AppCompatActivity {

    RecyclerView dueMeetingRecycler;
    ProgressBar spinKit;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Context context;
    String userID;

    List<FWorkerBuilding> fBuildingsList;
    DueMeetingListAdapter dueMeetingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_due_meeting);
        initView();
    }

    private void initView() {

        context = getApplicationContext();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        dueMeetingRecycler = (RecyclerView) findViewById(R.id.due_meeting_recycler);
        spinKit = (ProgressBar) findViewById(R.id.spin_kit);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dueMeetingRecycler.setLayoutManager(linearLayoutManager);

        fBuildingsList = new ArrayList<>();

        showDueMeetingList();

    }

    private void showDueMeetingList() {

        db.collection(context.getString(R.string.col_fWorkerBuilding)).whereEqualTo("f_uid", userID)
                .whereEqualTo("status_id", "rUyWv6FLEgZ0EIB6aNNP")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    fBuildingsList.clear();

                    for (DocumentSnapshot doc : task.getResult()) {
                        FWorkerBuilding fb = doc.toObject(FWorkerBuilding.class);
                        fBuildingsList.add(fb);

                    }


                    dueMeetingListAdapter = new DueMeetingListAdapter(fBuildingsList, context);
                    dueMeetingListAdapter.setHasStableIds(true);
                    dueMeetingRecycler.setAdapter(dueMeetingListAdapter);


                    spinKit.setVisibility(View.GONE);
                }
            }
        });
    }
}
