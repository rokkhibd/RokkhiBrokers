package com.rokkhi.broker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.mf.library.OnCallBack;
import com.mf.library.UpdateChecker;
import com.rokkhi.broker.Model.FWorkers;
import com.rokkhi.broker.Model.Users;
import com.rokkhi.broker.Ui.EditProfileActivity;
import com.rokkhi.broker.Ui.FworkerProfileActivity;
import com.rokkhi.broker.Ui.GuardTrackListActivity;
import com.rokkhi.broker.Ui.MyHomeActivity;
import com.rokkhi.broker.Ui.NoticeActivity;
import com.rokkhi.broker.Ui.PaymentHistoryActivity;
import com.rokkhi.broker.Ui.ProfileActivity;
import com.rokkhi.broker.Utils.Normalfunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout buldng_rel,payment_rel,notice_rel,profile_rel,guard_rel;
    ImageView logout,edit,userPic;

    Context context;
    TextView userName,userPhone;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String userId;

    boolean signoutstate=false;
    String utoken="";

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    Normalfunc normalfunc;

    RelativeLayout mrootview;
    private static final String TAG = "xxx";

    FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 12773;
    AuthUI.IdpConfig phoneConfigWithDefaultNumber;

    Animation mainPageAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();

        //update Checker start


        new UpdateChecker(this)
                .setUpdateLabel("Update") // optional - this to edit update button (default is "Update NOW")
                .setRemindLabel("Remind me") // optional - this to edit remind button (default is "Remind me later")
                .setRemindDays(2) //optional app remind user every 2 days (default is everyday)
                .setForceCloseOnSkip(true) //optional user will choose update or close app
                .setOnCallBack(new OnCallBack() {     //optional Callback to implement your own custom logic it
                    @Override
                    public boolean Done(boolean success, boolean isUpdateAvailable, String new_version) {
                        System.out.println("is success=" + success + " is update available=" + isUpdateAvailable + " new version is" + new_version);

                        //return true will show default library dialog if new version available
                        // and false will hide the dialog

                        return true;
                    }
                }).checkUpdate();

        //start app update checker

        UpdateChecker.clearReminder(this); // if you setRemindDays and want to clear cache to appear everyday again



        //update Checker End

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        normalfunc=new Normalfunc(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPref.edit();


        buldng_rel=findViewById(R.id.buildngs_relative);
        payment_rel=findViewById(R.id.payment_relative);
        notice_rel=findViewById(R.id.notice_relative);
        profile_rel=findViewById(R.id.profile_realtive);
        guard_rel=findViewById(R.id.guard_relative);
        userName=findViewById(R.id.username);
        userPhone=findViewById(R.id.userphone);

        logout=findViewById(R.id.logout_image);
        edit=findViewById(R.id.edit_image);
        userPic=findViewById(R.id.propic);

        buldng_rel.setOnClickListener(this);
        logout.setOnClickListener(this);
        edit.setOnClickListener(this);
        profile_rel.setOnClickListener(this);
        payment_rel.setOnClickListener(this);
        notice_rel.setOnClickListener(this);
        guard_rel.setOnClickListener(this);

        //Set animation
        mainPageAnim= AnimationUtils.loadAnimation(this,R.anim.main_page);
        buldng_rel.startAnimation(mainPageAnim);
        payment_rel.startAnimation(mainPageAnim);
        notice_rel.startAnimation(mainPageAnim);
        profile_rel.startAnimation(mainPageAnim);
        guard_rel.startAnimation(mainPageAnim);

        //gotoFworkerActivty();

        checkuserExistence();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Logout")
                        .setIcon(R.drawable.logout_image)
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                normalfunc.removeTokenId();
                                mAuth.signOut();

                            }

                        }).create().show();
            }
        });
    }

    public void checkuserExistence(){
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser=firebaseAuth.getCurrentUser();

                if (firebaseUser==null){
                    gotoLogIN();
                }else {

                    userId=firebaseUser.getUid();
                    getUsersData();
                    String user_Id= FirebaseAuth.getInstance().getUid();
                    db.collection(getString(R.string.col_fWorkers)).document(user_Id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {


                            if (documentSnapshot!=null && documentSnapshot.exists()){
//

                                FWorkers fworkers=documentSnapshot.toObject(FWorkers.class);


                                final List< String > usertoken = fworkers.getAtoken();

                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                        utoken = instanceIdResult.getToken();
                                        editor.putString("token", utoken);
                                        editor.apply();
                                        Log.d(TAG, "onSuccess: tttt7 "+signoutstate);
                                        //signoutstate=true;

                                        /*if (context!=null){

                                            if (usertoken != null && !usertoken.contains(utoken)  ) {
                                                String logID= db.collection(getString(R.string.col_loginsession)).document().getId();
                                                LogSession logSession= new LogSession(logID,userId,utoken,"FieldWork", Calendar.getInstance().getTime());
                                                db.collection(getString(R.string.col_loginsession)).document(logID).set(logSession)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(MainActivity.this,"Welcome!",Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                            }
                                       }*/


                                    }
                                });


                            }else {
                                gotoFworkerActivty();
                            }
                        }
                    });

                }
            }
        };

    }




    public void getUsersData(){

        String user_Id= FirebaseAuth.getInstance().getUid();
        db.collection(getString(R.string.col_users)).document(user_Id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (documentSnapshot!=null && documentSnapshot.exists()){

                    Users users= documentSnapshot.toObject(Users.class);

                    userName.setText(users.getName());
                    userPhone.setText(normalfunc.makephone11(users.getPhone()));

                    if (users.getThumb()!=null){
                        if(!users.getThumb().isEmpty() && !users.getThumb().equals("none")){

                            Glide.with(getApplicationContext()).load(users.getThumb()).fitCenter()
                                    .error(R.drawable.error_icon).into(userPic);
                            //userPic.setRotation(90f);

                        }
                    }

                }/*else {
                    Intent intent=new Intent(MainActivity.this, FworkerProfileActivity.class);
                    startActivity(intent);
                }*/
            }
        });
    }

    public void gotoFworkerActivty(){
        Intent intent=new Intent(MainActivity.this, FworkerProfileActivity.class);
        startActivity(intent);
    }


    public void gotoLogIN(){
        List<String> whitelistedCountries = new ArrayList<String>();
        whitelistedCountries.add("bd");
        phoneConfigWithDefaultNumber = new AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultCountryIso("bd")
                .setWhitelistedCountries(whitelistedCountries)
                .build();

        signInPhone(mrootview);

    }

    public void signInPhone(View view) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(phoneConfigWithDefaultNumber))
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit from the app?")
                .setIcon(R.drawable.exit)
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity.super.onBackPressed();

                    }

                }).create().show();
    }


    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.buildngs_relative){

            Intent intent=new Intent(MainActivity.this, MyHomeActivity.class);
            startActivity(intent);

        }else if (v.getId()==R.id.payment_relative){

            Intent intent=new Intent(MainActivity.this, PaymentHistoryActivity.class);
            startActivity(intent);

        }else if (v.getId()==R.id.notice_relative){

            Intent intent=new Intent(MainActivity.this, NoticeActivity.class);
            startActivity(intent);

        }else if (v.getId()==R.id.profile_realtive){

            Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);

        }else if (v.getId()==R.id.guard_relative){

            Intent intent=new Intent(MainActivity.this, GuardTrackListActivity.class);
            startActivity(intent);

        }/*else if (v.getId()==R.id.logout_image){
            normalfunc.removeTokenId();
            mAuth.signOut();

        }*/else if (v.getId()==R.id.edit_image){

            Intent intent=new Intent(MainActivity.this, EditProfileActivity.class);
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (resultCode == RESULT_OK) {
            Log.d(TAG, "handleSignInResponse: checkhere ");
            Log.d(TAG, "handleSignInResponse: jjj " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        } else {
            if (response == null) {
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }
            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

    }

    private void showSnackbar(int errorMessageRes) {
        if (mrootview!=null){

            Snackbar.make(mrootview, errorMessageRes, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
