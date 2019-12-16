package com.rokkhi.broker.Ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rokkhi.broker.Model.BuildingsListAdapter;
import com.rokkhi.broker.Model.FWorkerBuilding;
import com.rokkhi.broker.Model.FWorkers;
import com.rokkhi.broker.Model.LogSession;
import com.rokkhi.broker.R;
import com.rokkhi.broker.Utils.Normalfunc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyhomeFragment extends Fragment {

    Context context;
    FloatingActionButton flotbtn;
    RecyclerView recyclerView;
    List<FWorkerBuilding> fBuildingsList;
    private boolean isLastItemReached = false;
    FirebaseFirestore db;
    private DocumentSnapshot lastVisible=null;
    BuildingsListAdapter buildingsListAdapter;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String userID;
    String utoken="";
    CircleImageView profileImage;
    ImageView logout;
    Normalfunc normalfunc;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    boolean signoutstate=false;
    boolean shouldscrol=true;


    TextView f_name;
    ProgressBar profile_progressBar,spinKitProgress;
     FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "xxx";
    RelativeLayout mrootview;
    private static final int RC_SIGN_IN = 12773;
    AuthUI.IdpConfig phoneConfigWithDefaultNumber;



    public MyhomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myhome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        context= getActivity();
        normalfunc=new Normalfunc(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPref.edit();

        spinKitProgress=view.findViewById(R.id.spin_kit);
        logout=view.findViewById(R.id.logout_image);

        Wave wave=new Wave();
        spinKitProgress.setIndeterminateDrawable(wave);


        progressBar=view.findViewById(R.id.progressbar);
        recyclerView=view.findViewById(R.id.myhome_frag_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        fBuildingsList=new ArrayList<>();

        //spinKitProgress.setVisibility(View.VISIBLE);

        getfirstdata();

        //spinKitProgress.setVisibility(View.INVISIBLE);

//      shoWorkerDetails();

        flotbtn = view.findViewById(R.id.floating_btn);

        flotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddBuildingActivity.class);
                startActivity(intent);

            }
        });

    }

    private void checkUserExistence(FragmentActivity activity) {

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser=firebaseAuth.getCurrentUser();

                if(firebaseUser==null){
                    gotoLogIN();
                }
                else{
                    userID=firebaseUser.getUid();
                    getfirstdata();
                    final String user_id=firebaseAuth.getCurrentUser().getUid();

                    db.collection(getString(R.string.col_fWorkers)).document(user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {


                            if (documentSnapshot!=null && documentSnapshot.exists()){
//

                                FWorkers fworkers=documentSnapshot.toObject(FWorkers.class);


                                final List< String > usertoken = fworkers.getAtoken();

                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity, new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                        utoken = instanceIdResult.getToken();
                                        editor.putString("token", utoken);
                                        editor.apply();
                                        // Log.d(TAG, "onSuccess: tokenxx "+ useertoken +"xx"+ utoken);
                                        Log.d(TAG, "onSuccess: tttt7 "+signoutstate);
                                        //signoutstate=true;

                                        if (getActivity()!=null){

                                            if (usertoken != null && !usertoken.contains(utoken)  ) {
                                                String logID= db.collection(getString(R.string.col_loginsession)).document().getId();
                                                LogSession logSession= new LogSession(logID,userID,utoken,"FieldWork", Calendar.getInstance().getTime());
                                                db.collection(getString(R.string.col_loginsession)).document(logID).set(logSession)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(context,"Welcome!",Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                            }
                                        }


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


    private void gotoFworkerActivty() {

        Intent intent=new Intent(getContext(),FworkerProfileActivity.class);
        startActivity(intent);
        if(getActivity()!=null)getActivity().finish();

    }


    public void getfirstdata(){

        spinKitProgress.setVisibility(View.VISIBLE);


        userID=firebaseUser.getUid();
        Log.d(TAG, "getfirstdata: ttt2 "+userID);
        db.collection(getString(R.string.col_fWorkerBuilding)).whereEqualTo("f_uid",userID)
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
                            Log.d(TAG, "onComplete: ttt "+ fBuildingsList.size());

                            spinKitProgress.setVisibility(View.GONE);
                            buildingsListAdapter=new BuildingsListAdapter(fBuildingsList,context);
                            buildingsListAdapter.setHasStableIds(true);
                            recyclerView.setAdapter(buildingsListAdapter);
                            recyclerView.setAdapter(buildingsListAdapter);

                            int xx=task.getResult().size();
                            if(xx>0)lastVisible = task.getResult().getDocuments().get(xx - 1);
                            loadmoredata();
                        }
                    }
                });
    }

    public void loadmoredata(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

                Log.d(TAG, "onScrollChange: item dekhi "+ firstVisibleItemPosition +" "+ visibleItemCount+" "+totalItemCount);


                if ((firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached && shouldscrol
                        && lastVisible!=null) {
                    progressBar.setVisibility(View.VISIBLE);
                    shouldscrol=false;
                    Log.d(TAG, "onScrolled: mmmmll dhukse");
                    Query nextQuery;
                    nextQuery= db.collection(getString(R.string.col_fWorkerBuilding)).whereEqualTo("f_uid",userID)
                            .orderBy("updated_at", Query.Direction.DESCENDING).startAfter(lastVisible).limit(10);
                    nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete( Task<QuerySnapshot> t) {
                            if (t.isSuccessful()) {
                                // list.clear();

                                for (DocumentSnapshot d : t.getResult()) {
                                    FWorkerBuilding fWorkerBuilding = d.toObject(FWorkerBuilding.class);
                                    fBuildingsList.add(fWorkerBuilding);
                                }
                                shouldscrol=true;
                                progressBar.setVisibility(View.GONE);
                                buildingsListAdapter.notifyDataSetChanged();
                                int xx=t.getResult().size();
                                if(xx>0)lastVisible = t.getResult().getDocuments().get(xx - 1);

                                if (t.getResult().size() < 10) {
                                    isLastItemReached = true;
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }
                else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });





    }

    public void gettingAllHouseData(){
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






}

