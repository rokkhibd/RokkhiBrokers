package com.rokkhi.brokers.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rokkhi.brokers.Model.FBuildings;
import com.rokkhi.brokers.Model.Reports;
import com.rokkhi.brokers.R;

public class ReportActivity extends AppCompatActivity {

    protected TextView roadNumbeReportTV;
    protected TextView houseNumberReportTV;
    protected TextView houseNameReportTV;
    protected EditText titleReportET;
    protected EditText detailsReportET;
    FBuildings fBuildings;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    DocumentReference documentReference;

    String TAG="ReportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_report);
        initView();

        fBuildings = (FBuildings) getIntent().getSerializableExtra("fbuildings");

        roadNumbeReportTV.setText(fBuildings.getB_roadno());
        houseNumberReportTV.setText(fBuildings.getB_houseno());
        houseNameReportTV.setText(fBuildings.getHousename());




    }

    public void submitReport(View view) {

        String title = titleReportET.getText().toString();
        String details = detailsReportET.getText().toString();


        if (title.isEmpty()){
            titleReportET.setError("Fill the Field");
            titleReportET.requestFocus();
            return;
        }
        if (details.isEmpty()){
            detailsReportET.setError("Fill the Field");
            detailsReportET.requestFocus();
            return;
        }


        firebaseFirestore=FirebaseFirestore.getInstance();

        documentReference=firebaseFirestore.collection(getString(R.string.col_fReport)).document();
        String docID = documentReference.getId();
        String buildID = fBuildings.getBuild_id();
        String userID = firebaseAuth.getCurrentUser().getUid();
        String phoneNumber = firebaseAuth.getCurrentUser().getPhoneNumber();

        Log.e(TAG, "submitReport: docID "+docID );
        Log.e(TAG, "submitReport: buildID "+buildID );
        Log.e(TAG, "submitReport: userID "+userID);
        Log.e(TAG, "submitReport: phoneNumber "+phoneNumber);

        Reports reports=new Reports(docID,buildID,userID,details,title,phoneNumber);


        firebaseFirestore.collection(getString(R.string.col_fReport))
                .document(docID)
                .set(reports).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                startActivity(new Intent(ReportActivity.this,AddBuildingActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReportActivity.this, "Failed to Submit Data", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void initView() {
        roadNumbeReportTV = (TextView) findViewById(R.id.roadNumbeReport);
        houseNumberReportTV = (TextView) findViewById(R.id.houseNumberReport);
        houseNameReportTV = (TextView) findViewById(R.id.houseNameReport);
        titleReportET = (EditText) findViewById(R.id.titleReport);
        detailsReportET = (EditText) findViewById(R.id.detailsReport);
        firebaseAuth = FirebaseAuth.getInstance();


    }
}
