package com.rokkhi.brokers.Ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.brokers.Model.PaymentHistory;
import com.rokkhi.brokers.Model.PaymentListAdapter;
import com.rokkhi.brokers.R;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentHistoryFragment extends Fragment {

    RecyclerView recyclerView;

    FirebaseFirestore firebaseFirestore;
    List<PaymentHistory> paymentHistoryList;
    ProgressDialog progressDialog;
    PaymentListAdapter paymentListAdapter;
    Context context;

    public PaymentHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.paymentHistoryRecyclerViewID);
        firebaseFirestore = FirebaseFirestore.getInstance();
        paymentHistoryList = new ArrayList<>();
        progressDialog = new ProgressDialog(view.getContext());
        context = view.getContext();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //Load User Data
        loadPaymentData();


    }

    private void loadPaymentData() {

        progressDialog.setMessage("Executing Action...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        firebaseFirestore.collection(getString(R.string.col_fpaymentHistory)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    paymentHistoryList.clear();

                    for (DocumentSnapshot snapshot : task.getResult()) {
                        PaymentHistory paymentHistory = snapshot.toObject(PaymentHistory.class);
                        paymentHistoryList.add(paymentHistory);
                    }

                    paymentListAdapter = new PaymentListAdapter(paymentHistoryList, context);
                    recyclerView.setAdapter(paymentListAdapter);

                    progressDialog.dismiss();

                } else {
                    Toast.makeText(context, "Data Load Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Data Load Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
