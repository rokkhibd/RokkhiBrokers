package com.rokkhi.broker.Ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.rokkhi.broker.Model.FPayments;
import com.rokkhi.broker.Model.FWorkers;
import com.rokkhi.broker.Model.Users;
import com.rokkhi.broker.R;
import com.rokkhi.broker.Utils.Normalfunc;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    ImageView logout,edit;
    CircleImageView propic;
    TextView name,mailid,joiningDate;
    Button bkashno,nidno;
    TextView tearning,dearning,tref,dref,tmeeting,dmeeting,tbuilding,dbuilding,abuilding,bonusearning,dbonusearing;
    FirebaseFirestore firebaseFirestore;

    Context context;
    ProgressBar pro;
    Normalfunc normalfunc;
    String userid="none";
    String bkashnumber="none";
    String nid="none";
    FWorkers fWorkers;

    private long mLastClickTime = 0;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseFirestore= FirebaseFirestore.getInstance();
        context=getActivity();
        pro=view.findViewById(R.id.pro);
        pro.setVisibility(View.VISIBLE);

        logout=view.findViewById(R.id.logout);
        edit=view.findViewById(R.id.edit);
        propic=view.findViewById(R.id.propic);
        name=view.findViewById(R.id.username);
        mailid=view.findViewById(R.id.mail);
        joiningDate=view.findViewById(R.id.joinDate);

        bkashno=view.findViewById(R.id.bkash_no);
        nidno=view.findViewById(R.id.nid_no);

        tearning=view.findViewById(R.id.total_earning);
        dearning=view.findViewById(R.id.due_earning);
        tref=view.findViewById(R.id.total_referal);
        dref=view.findViewById(R.id.due_referal);
        tmeeting=view.findViewById(R.id.total_meeting);
        dmeeting=view.findViewById(R.id.due_meeting);
        tbuilding=view.findViewById(R.id.total_building);
        dbuilding=view.findViewById(R.id.due_building);
        abuilding=view.findViewById(R.id.active_building);
        bonusearning=view.findViewById(R.id.bonus_earning);
        dbonusearing=view.findViewById(R.id.bonus_due_earning);
        userid= FirebaseAuth.getInstance().getUid();


        normalfunc= new Normalfunc(context);
        showCurrentUserInfo();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,EditProfileActivity.class);
                startActivity(intent);
            }
        });

        bkashno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setCancelable(false);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.confirm_bkash, null);
                EditText input= convertView.findViewById(R.id.input);
                Button done = convertView.findViewById(R.id.done);
                Button cancel = convertView.findViewById(R.id.cancel);
                ProgressBar progressBar= convertView.findViewById(R.id.pro);

                if(!bkashnumber.equals("none"))input.setText(normalfunc.makephone11(bkashnumber));

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        input.setError(null);
                        // Store values at the time of the login attempt.
                        final String bkashtext = input.getText().toString();
                        boolean cancel = false;
                        View focusView = null;


                        if (TextUtils.isEmpty(bkashtext)) {
                            input.setError(getString(R.string.error_field_required));
                            focusView = input;
                            cancel = true;

                        }

                        if (!normalfunc.isvalidphone11(bkashtext)) {
                            input.setError(getString(R.string.fui_invalid_phone_number));
                            focusView = input;
                            cancel = true;

                        }

                        if (cancel) {
                            Log.d(TAG, "onClick: yyy1 ");
                            // There was an error; don't attempt login and focus the first
                            // form field with an error.
                            focusView.requestFocus();
                            //progressBar.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "onClick: yyy2");
                            String bkashtext2=normalfunc.makephone14(bkashtext);

                            Date date;
                            date = Calendar.getInstance().getTime();

                            progressBar.setVisibility(View.VISIBLE);
                            Map<String,Object>mm=new HashMap<>();
                            mm.put("bkash_no",bkashtext2);
                            mm.put("updated_at",date);
                            firebaseFirestore.collection(getString(R.string.col_fPayment))
                                    .document(userid).set(mm, SetOptions.merge())
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "onClick: yyy3 ");
                                            progressBar.setVisibility(View.GONE);
                                            alertDialog.dismiss();
                                        }
                                    });
                        }
                    }
                });

                alertDialog.setView(convertView);
                alertDialog.show();
            }
        });

        nidno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setCancelable(false);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.confirm_nid, null);
                EditText input= convertView.findViewById(R.id.nid_input);
                Button done = convertView.findViewById(R.id.nid_done);
                Button cancel = convertView.findViewById(R.id.nid_cancel);
                ProgressBar progressBar= convertView.findViewById(R.id.nid_pro);

                if(!nid.equals("none"))input.setText(nid);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        input.setError(null);
                        // Store values at the time of the login attempt.
                        final String nidtext = input.getText().toString();
                        boolean cancel = false;
                        View focusView = null;


                        if (TextUtils.isEmpty(nidtext)) {
                            input.setError(getString(R.string.error_field_required));
                            focusView = input;
                            cancel = true;

                        }

                        if (cancel) {
                            Log.d(TAG, "onClick: yyy1 ");
                            // There was an error; don't attempt login and focus the first
                            // form field with an error.
                            focusView.requestFocus();
                            //progressBar.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "onClick: yyy4");
                            //String bkashtext2=normalfunc.makephone14(bkashtext);
                            progressBar.setVisibility(View.VISIBLE);

                            Date date;
                            date = Calendar.getInstance().getTime();

                            Map<String,Object>mm=new HashMap<>();
                            mm.put("fw_nid",nidtext);
                            mm.put("updated_at",date);
                            firebaseFirestore.collection(getString(R.string.col_fWorkers))
                                    .document(userid).set(mm, SetOptions.merge())
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "onClick: yyy3 ");
                                            progressBar.setVisibility(View.GONE);
                                            alertDialog.dismiss();
                                        }
                                    });
                        }
                    }
                });

                alertDialog.setView(convertView);
                alertDialog.show();

            }
        });

        RelativeLayout totalBuildingRef,dueBuildingRef,totalMeeting,dueMeeting,totalref,dueref,liveBuilding;

        totalBuildingRef=view.findViewById(R.id.totalbuildingrel);
        dueBuildingRef=view.findViewById(R.id.due_buildingrel);
        totalMeeting=view.findViewById(R.id.meetingrel);
        dueMeeting=view.findViewById(R.id.reldue_meeting);
        totalref=view.findViewById(R.id.total_referal_rel);
        dueref=view.findViewById(R.id.reldue_referal);
        liveBuilding=view.findViewById(R.id.active_buildingrel);

        totalBuildingRef.setOnClickListener(this);
        dueBuildingRef.setOnClickListener(this);
        totalMeeting.setOnClickListener(this);
        dueMeeting.setOnClickListener(this);
        totalref.setOnClickListener(this);
        dueref.setOnClickListener(this);
        liveBuilding.setOnClickListener(this);

    }

    private static final String TAG = "ProfileFragment";


    private void showCurrentUserInfo() {

        String userId= FirebaseAuth.getInstance().getUid();

        firebaseFirestore.collection(getString(R.string.col_users)).document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if(documentSnapshot!=null && documentSnapshot.exists()){
                            Users users= documentSnapshot.toObject(Users.class);
                            name.setText(users.getName());
                            if (users.getThumb()!=null){
                                if(!users.getThumb().isEmpty() && !users.getThumb().equals("none")){

                                    Glide.with(getContext()).load(users.getThumb()).fitCenter().error(R.drawable.error_icon).into(propic);

                                }


                            }
                            mailid.setText(users.getMail());
                            joiningDate.setText(normalfunc.getDateMMMdyyyy(users.getJoindate()));
                        }


                    }
                });

        firebaseFirestore.collection(getString(R.string.col_fPayment)).document(userId)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        pro.setVisibility(View.GONE);

//                            TextView tearning,dearning,tref,dref,tmeeting,dmeeting,tbuilding,dbuilding,abuilding;
                        FPayments fPayments= documentSnapshot.toObject(FPayments.class);
                        bkashnumber=fPayments.getBkash_no();
                        bkashno.setText("Bkash ->  "+normalfunc.makephone11(bkashnumber));

                        tearning.setText(String.valueOf(fPayments.getTotal_earning()));
                        dearning.setText(String.valueOf(fPayments.getDue_earning()));
                        bonusearning.setText(String.valueOf(fPayments.getBonus_earning()));
                        dbonusearing.setText(String.valueOf(fPayments.getDue_bonus_earning()));
                        tref.setText(String.valueOf(fPayments.getTotal_referral()));
                        dref.setText(String.valueOf(fPayments.getDue_referral()));
                        tmeeting.setText(String.valueOf(fPayments.getTotal_meeting()));
                        dmeeting.setText(String.valueOf(fPayments.getDue_meeting()));
                        tbuilding.setText(String.valueOf(fPayments.getTotal_buildings()));
                        dbuilding.setText(String.valueOf(fPayments.getDue_buildings()));
                        abuilding.setText(String.valueOf(fPayments.getActive_buildings()));
                    }


                });

        firebaseFirestore.collection(getString(R.string.col_fWorkers)).document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot!=null && documentSnapshot.exists()){
                    FWorkers fWorkers= documentSnapshot.toObject(FWorkers.class);

                    nid=fWorkers.getFw_nid();

                    nidno.setText("NID ->  "+nid);

                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.totalbuildingrel){
            showusersAllBuildings();
        }else if (v.getId()==R.id.due_buildingrel){
            showUserDueBuilding();
        }else if (v.getId()==R.id.active_buildingrel){
            showUsersLiveBuilding();
        }else if (v.getId()==R.id.meetingrel){
            showUserTotalMeeeting();
        }else if (v.getId()==R.id.reldue_meeting){
            showuserDueMeeting();
        }else if (v.getId()==R.id.total_referal_rel){
            showTotalrefBuilding();
        }else if (v.getId()==R.id.reldue_referal){
            showDueRefBuildings();
        }
    }

    private void showDueRefBuildings() {
        Intent intent=new Intent(getContext(),DuerefferalActivity.class);
        startActivity(intent);
    }

    private void showTotalrefBuilding() {
        Intent intent=new Intent(getContext(),TotalReferralActivity.class);
        startActivity(intent);
    }

    private void showUserDueBuilding() {
        Intent intent=new Intent(getContext(),UsersDueBuildingsActivity.class);
        startActivity(intent);
    }

    private void showusersAllBuildings() {
        Intent intent=new Intent(getContext(),UserTotalBuildingsActivity.class);
        startActivity(intent);
    }
    private void showUsersLiveBuilding() {
        Intent intent=new Intent(getContext(),UsersLiveBuildingActivity.class);
        startActivity(intent);
    }

    private void showUserTotalMeeeting(){
        Intent intent=new Intent(getContext(),TotalMeetingActivity.class);
        startActivity(intent);
    }

    private void showuserDueMeeting(){
        Intent intent=new Intent(getContext(),DueMeetingActivity.class);
        startActivity(intent);
    }
}
