package com.rokkhi.broker.Ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.broker.Model.FNotification;
import com.rokkhi.broker.R;


import java.util.ArrayList;
import java.util.List;


public class NoticeFragment extends Fragment {

    RecyclerView recyclerView;
    List<FNotification> fNotificationListlist;
    FirebaseFirestore db;
    NoticeAdapter noticeAdapter;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    Context context;
    ProgressDialog progressDialog;
    ProgressBar spintKit;
    private DocumentSnapshot lastVisible=null;


    boolean shouldscrol=true;


    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(view.getContext());

        spintKit=view.findViewById(R.id.spin_kit);
        context=getActivity();
        Wave wave=new Wave();
        spintKit.setIndeterminateDrawable(wave);

        fNotificationListlist = new ArrayList<>();
        recyclerView = view.findViewById(R.id.notice_recylerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
       // gettingAllNoticeDate();
        getAlltheNotices();

    }


    public void  getAlltheNotices(){

       db.collection(getString(R.string.col_fNotification)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fNotificationListlist.clear();

                    for (DocumentSnapshot snapshot:task.getResult()){
                        FNotification fNotification=snapshot.toObject(FNotification.class);
                        fNotificationListlist.add(fNotification);
                    }
                    noticeAdapter =new NoticeAdapter(fNotificationListlist,context);
                    recyclerView.setAdapter(noticeAdapter);

                    spintKit.setVisibility(View.GONE);


                }else {

                    Toast.makeText(context, "Data loading failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Data Load Failed", Toast.LENGTH_SHORT).show();
              //  progressDialog.dismiss();

                spintKit.setVisibility(View.GONE);
            }
        });
    }


}
