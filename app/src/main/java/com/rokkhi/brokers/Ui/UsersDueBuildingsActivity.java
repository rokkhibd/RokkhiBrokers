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

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.brokers.Model.PaymentBuildingShow;
import com.rokkhi.brokers.Model.ShowDueBuildingListAdapter;
import com.rokkhi.brokers.R;
import java.util.ArrayList;
import java.util.List;

public class UsersDueBuildingsActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userId;
    RecyclerView recyclerView;
    Context context;
    ProgressBar spinkitBar;
    String buildId;
    String amount;

    List<PaymentBuildingShow> paymentBuildingShowsList;
    ShowDueBuildingListAdapter showDueBuildingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_due_buildings);

        context=getApplicationContext();

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        recyclerView=findViewById(R.id.due_bldng_recycler);
        spinkitBar=findViewById(R.id.spin_kit);

        Wave wave=new Wave();
        spinkitBar.setIndeterminateDrawable(wave);

        paymentBuildingShowsList=new ArrayList<>();

        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);

        showTheBuildingList();

    }


    private void showTheBuildingList(){

        db.collection(context.getString(R.string.col_paymentHistory)).whereEqualTo("f_uid",userId).whereEqualTo("payment_status","pending")
                .whereEqualTo("payment_type","w8t5NYlrk68ebdllxdsC").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    paymentBuildingShowsList.clear();

                    for (DocumentSnapshot doc:task.getResult()){
                        PaymentBuildingShow ph=doc.toObject(PaymentBuildingShow.class);
                        paymentBuildingShowsList.add(ph);

                    }

                    Log.d("TAG", "onComplete: ttt "+ paymentBuildingShowsList.size());

                    showDueBuildingListAdapter=new ShowDueBuildingListAdapter(context,paymentBuildingShowsList);
                    showDueBuildingListAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(showDueBuildingListAdapter);

                    spinkitBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
