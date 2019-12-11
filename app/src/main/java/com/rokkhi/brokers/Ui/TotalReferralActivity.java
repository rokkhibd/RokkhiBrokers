package com.rokkhi.brokers.Ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.rokkhi.brokers.Model.FPayments;
import com.rokkhi.brokers.Model.TotalRefferalListAdapter;
import com.rokkhi.brokers.Model.Users;
import com.rokkhi.brokers.R;


import java.util.ArrayList;
import java.util.List;

public class TotalReferralActivity extends AppCompatActivity {

    RecyclerView dueBldngRecycler;
    protected ProgressBar spinKit;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String currentUser;
    String user_phone;
    List<Users> usersList;
    TotalRefferalListAdapter totalRefferalListAdapter;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_total_referral);
        initView();
    }

    private void initView() {

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user_phone=mAuth.getCurrentUser().getPhoneNumber();

        Log.e("TAG","Number"+user_phone);
        usersList=new ArrayList<>();

        dueBldngRecycler = (RecyclerView) findViewById(R.id.due_bldng_recycler);
        spinKit = (ProgressBar) findViewById(R.id.spin_kit);
        txt=findViewById(R.id.notdatatxt);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        dueBldngRecycler.setLayoutManager(linearLayoutManager);

        showTotalRef();
    }

    private void showTotalRef() {

        db.collection(getString(R.string.col_fPayment)).whereEqualTo("ref_id",user_phone).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    usersList.clear();

                    for (DocumentSnapshot doc:task.getResult()){

                        FPayments fPayments=doc.toObject(FPayments.class);

                        currentUser =fPayments.getUser_id();

                        Log.e("TAG","id:"+currentUser);

                        db.collection(getString(R.string.col_users)).whereEqualTo("user_id",currentUser).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){

                                            //usersList.clear();

                                            for (DocumentSnapshot doc:task.getResult()){
                                                Users users=doc.toObject(Users.class);
                                                usersList.add(users);


                                            }

                                            Log.e("TAG","size:"+usersList.size());

                                            totalRefferalListAdapter=new TotalRefferalListAdapter(usersList,TotalReferralActivity.this);
                                            totalRefferalListAdapter.setHasStableIds(true);
                                            dueBldngRecycler.setAdapter(totalRefferalListAdapter);

                                            spinKit.setVisibility(View.GONE);

                                        }
                                    }
                                });

                    }


                }


            }
        });

    }
}
