package com.rokkhi.broker.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.broker.Model.BuildingsListAdapter;
import com.rokkhi.broker.Model.FWorkerBuilding;
import com.rokkhi.broker.R;

import java.util.ArrayList;
import java.util.List;

public class UserTotalBuildingsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar spinKit;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String currentUserID;
    Context context;
    List<FWorkerBuilding> fBuildingsList;
    BuildingsListAdapter buildingsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_total_buildings);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();

        context=getApplicationContext();

        recyclerView=findViewById(R.id.totalbuild_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(UserTotalBuildingsActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        fBuildingsList=new ArrayList<>();

        spinKit=findViewById(R.id.spin_kit);

        Wave wave=new Wave();
        spinKit.setIndeterminateDrawable(wave);

        getTotalBuildings();

    }

    private void getTotalBuildings() {
        spinKit.setVisibility(View.VISIBLE);


        db.collection(getString(R.string.col_fWorkerBuilding)).whereEqualTo("f_uid",currentUserID)
                .orderBy("updated_at", Query.Direction.DESCENDING).limit(10)
                .get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fBuildingsList.clear();

                            for(DocumentSnapshot d:task.getResult()){

                                FWorkerBuilding fb=d.toObject(FWorkerBuilding.class);

                                fBuildingsList.add(fb);
                            }
                            Log.d("TAG", "onComplete: ttt "+ fBuildingsList.size());

                            spinKit.setVisibility(View.GONE);
                            buildingsListAdapter=new BuildingsListAdapter(fBuildingsList,context);
                            buildingsListAdapter.setHasStableIds(true);
                            recyclerView.setAdapter(buildingsListAdapter);
                            recyclerView.setAdapter(buildingsListAdapter);

                            //int xx=task.getResult().size();
                            //if(xx>0)lastVisible = task.getResult().getDocuments().get(xx - 1);
                            //loadmoredata();
                        }
                    }
                });
    }
}
