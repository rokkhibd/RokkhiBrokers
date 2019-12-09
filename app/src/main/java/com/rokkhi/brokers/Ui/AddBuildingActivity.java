package com.rokkhi.brokers.Ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.brokers.Model.AllStringValues;
import com.rokkhi.brokers.Model.FBPeople;
import com.rokkhi.brokers.Model.FBuildings;
import com.rokkhi.brokers.Model.FWorkerBuilding;
import com.rokkhi.brokers.Model.FWorkers;
import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Utils.Normalfunc;
import com.rokkhi.brokers.Utils.StringAdapter;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import mumayank.com.airlocationlibrary.AirLocation;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AddBuildingActivity extends AppCompatActivity {

    AllStringValues allStringValues;
    ArrayAdapter<String> adapter;
    FusedLocationProviderClient client;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    StorageReference addbldngRef;
    String currentUserID;
    ListView roadNumberList, blockList, houseNoList, areaListView, peopleWeTalkList, districtListView;
    EditText roadNumberEdit, blockEdit, houseNoEdit, areaEdit, districtEdit;
    Bitmap bitmap;
    Uri pickedImageUri;
    List<String> areaList = new ArrayList<>();
    List<String> districtList = new ArrayList<>();
    DocumentReference docref_FBuildings;
    DocumentReference docref_FWorkersBuildings;
    Normalfunc normalfunc;
    CircleImageView circleImageView;
    EditText b_status, b_name, b_totalfloor, b_floorperflat, b_totalguard, areaNameET, roadName, roadNumberET, houseNumberET,
            b_visit, b_follwing, b_code, b_peoplesName, b_peopleNumber, people_we_talk, districtNameET;
    Button tapCode, addInfoButton, checkHouseBtn, saveNumberBtn;
    String roadListCode, blockListCode, houseListCode, housefrmntListCode, totalHouseCode, districtValue, downloadImageUri, totalCode;
    String wholeAddress, currentDate, status_id;
    Double lat, lan;
    ImageView visitCal, followpCal, statusMenu, flatfrmtMenu, /*district_Menu*/
            designationMenu;
    EditText b_flatfrmt;
    int areaCodePos;
    int districtCodePos;
    int statusCodePos;
    List<Long> areaCodeList;
    List<String> statusIdList;
    List<Long> districtCodeList;
    FBPeople fbPeople;
    FBuildings fBuildings;
    Date date;
    ArrayList<String> types;
    Context context;
    Query buildingsQuery;
    private AirLocation airLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        areaCodeList = new ArrayList<>();
        districtCodeList = new ArrayList<>();
        statusIdList = new ArrayList<>();
        normalfunc = new Normalfunc();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        docref_FBuildings = firebaseFirestore.collection(getString(R.string.col_fBuildings)).document();

        docref_FWorkersBuildings = firebaseFirestore.collection(getString(R.string.col_fWorkerBuilding)).document();

        addbldngRef = FirebaseStorage.getInstance().getReference()
                .child("fBuildings/" + currentUserID + "/pic");

        client = LocationServices.getFusedLocationProviderClient(this);
        getTheLatLang();

        fbPeople = new FBPeople();
        fBuildings = new FBuildings();

        date = Calendar.getInstance().getTime();
        context = AddBuildingActivity.this;
        progressDialog = new ProgressDialog(this);

        relativeLayout = findViewById(R.id.housecheck_layout);
        progressBar = findViewById(R.id.progressbar);

        b_peoplesName = findViewById(R.id.bldng_edit_theirnames);
        b_peopleNumber = findViewById(R.id.bldng_edit_theirnumbers);

        districtNameET = findViewById(R.id.district);
        areaNameET = findViewById(R.id.area);
        roadNumberET = findViewById(R.id.roadNumber);
        roadName = findViewById(R.id.road_Name);
        houseNumberET = findViewById(R.id.houseNumber);


        b_floorperflat = findViewById(R.id.bldng_edit_totalflt);
        b_totalfloor = findViewById(R.id.bldng_edit_totalfloor);
        b_totalguard = findViewById(R.id.bldng_edit_totalguard);
        b_flatfrmt = findViewById(R.id.bldng_edit_flatformat);
        b_visit = findViewById(R.id.bldng_edit_visitdate);
        b_follwing = findViewById(R.id.bldng_edit_followingdate);
        b_code = findViewById(R.id.bldng_edit_bcode);
        b_status = findViewById(R.id.bldng_edit_status);
        b_name = findViewById(R.id.bldng_edit_husename);
       /* b_lat = findViewById(R.id.bldng_edit_lat);
        b_long = findViewById(R.id.bldng_edit_long);*/
        people_we_talk = findViewById(R.id.bldng_edit_buildingspeoples);

        tapCode = findViewById(R.id.tap_bcode);
        circleImageView = findViewById(R.id.building_photo);
        addInfoButton = findViewById(R.id.addblgnInfoBtn);
        checkHouseBtn = findViewById(R.id.check_button);
        saveNumberBtn = findViewById(R.id.bldng_edit_numberSaves);

        visitCal = findViewById(R.id.visitcalimg);
        followpCal = findViewById(R.id.followingcalimg);
        statusMenu = findViewById(R.id.statusMenu);
        flatfrmtMenu = findViewById(R.id.flatformatMenu);
//        district_Menu = findViewById(R.id.districtMenu);
//        designationMenu = findViewById(R.id.choosePeopleMenu);

        allStringValues = new AllStringValues();

        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {
                // lat = location.getLatitude();
                //lng = location.getLongitude();
            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allStringValues.status);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allStringValues.flatformat);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allStringValues.district);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allStringValues.designation);


        //TODO: get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());

        addInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar.setVisibility(View.VISIBLE);

                if (pickedImageUri == null) {


                    FancyToast.makeText(context, "Capture a Building Image", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    //saveBuildingDataInDB();
                    progressBar.setVisibility(View.GONE);

                } else {
                    saveImageToStorage();
                }


            }
        });

        saveNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNumberBtn.setVisibility(View.INVISIBLE);

                createBuildingsContactInfo();

            }
        });

        b_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        b_follwing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AllStringValues.showCalendar(AddBuildingActivity.this, b_follwing);

            }
        });


        people_we_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //people_we_talk.showDropDown();
                //Toast.makeText(AddBuildingActivity.this, "ok", Toast.LENGTH_SHORT).show();
                showTypeofPeopleInbuilding();

            }
        });

        b_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuildingStatus();
            }
        });

        b_flatfrmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //b_flatfrmt.showDropDown();
                // flatformat = b_flatfrmt.getText().toString();
                addAllTypes();
            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickSetup setup = new PickSetup().setWidth(100).setHeight(100)
                        .setTitle("Choose Photo")
                        .setBackgroundColor(Color.WHITE)
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setGalleryButtonText("Gallery")
                        .setCameraIcon(R.mipmap.camera_colored)
                        .setGalleryIcon(R.mipmap.gallery_colored);

                PickImageDialog.build(setup, new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        if (r.getError() == null) {

                            pickedImageUri = r.getUri();
                            bitmap = r.getBitmap();
                            circleImageView.setImageBitmap(r.getBitmap());
                            //saveImageToStorage();
                        } else {
                            Toast.makeText(AddBuildingActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }).show(AddBuildingActivity.this);

            }
        });

        //get Area Data List
        firebaseFirestore.collection("area").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                areaList.clear();

                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        String area_eng = documentSnapshot.getString("english");
                        String area_ban = documentSnapshot.getString("bangla");
                        Long area_code = documentSnapshot.getLong("code");
                        areaCodeList.add(area_code);

                        areaList.add(area_eng + "(" + area_ban + ")");

                    }
                }

                areaNameET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAllAreasAlert(areaList);


                    }
                });

            }
        });
        //Get District Data
        firebaseFirestore.collection(getString(R.string.col_district)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                districtList.clear();

                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        String area_district = documentSnapshot.getString("english");
                        //String area_ban = documentSnapshot.getString("bangla");
                        Long district_code = documentSnapshot.getLong("code");
                        districtCodeList.add(district_code);

                        Log.e("xxxx", area_district);
                        Log.e("xxxx", district_code.toString());
                        districtList.add(area_district);


                    }
                }

                districtNameET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDistrict(districtList);
                    }
                });
            }
        });


      /*  roadNumberET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableRoads();

            }
        });*/

   /*     b_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableBlock();
                //showAddressAlert(Arrays.asList(allStringValues.block_numbers),b_block);
            }
        });*/
/*
        houseNumberET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoAvailableHouseNumner();

                //showAddressAlert(Arrays.asList(allStringValues.road_no),houseNumberET);
            }
        });*/

   /*     b_housefrmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAvailableHouseFormat();
                //showAddressAlert(Arrays.asList(allStringValues.block_numbers),b_housefrmt);
            }
        });*/

        checkHouseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection(getString(R.string.col_fWorkers)).document(currentUserID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {

                                    FWorkers fWorkers = task.getResult().toObject(FWorkers.class);

                                    boolean train = fWorkers.isTrained();

                                    //Check if the guard is trained or not while adding building
                                    if (train == false) {
                                        checkTrainOrNot();
                                    } else {
                                        if (districtNameET.getText().toString().isEmpty()) {
                                            districtNameET.setError("Insert the District number");
                                            districtNameET.requestFocus();
                                            return;

                                        } else if (areaNameET.getText().toString().isEmpty()) {
                                            areaNameET.setError("Insert the area name");
                                            areaNameET.requestFocus();

                                            return;

                                        } else if (roadNumberET.getText().toString().isEmpty()) {
                                            roadNumberET.setError("Insert the road number");
                                            areaNameET.requestFocus();
                                            return;

                                        } else if (houseNumberET.getText().toString().isEmpty()) {
                                            houseNumberET.setError("Insert the House number");
                                            houseNumberET.requestFocus();
                                            return;

                                        }

                                        String roadNumberST = roadNumberET.getText().toString().replaceAll("\\s+", "");
                                        String areaCode = areaCodeList.get(areaCodePos).toString();
                                        String houseNumberST = houseNumberET.getText().toString().replaceAll("\\s+", "");


                                        Log.e("TAG", "onComplete: district code = " + districtCodeList.get(districtCodePos));
                                        Log.e("TAG", "onComplete: area code = " + areaCode);
                                        Log.e("TAG", "onComplete: roadNumberST code = " + roadNumberST);
                                        Log.e("TAG", "onComplete: houseNumberST code = " + houseNumberST);


                                        CollectionReference buildref = firebaseFirestore.collection(getString(R.string.col_fBuildings));


                                        buildingsQuery = buildref.whereEqualTo("b_area", areaCode)
                                                .whereEqualTo("b_roadno", roadNumberST)
                                                .whereEqualTo("b_houseno", houseNumberST);

                                        buildingsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                                if (task.isSuccessful()) {

                                                    Toast.makeText(AddBuildingActivity.this, "" + task.getResult().size(), Toast.LENGTH_SHORT).show();

                                                    //if get this Building
                                                    if (task.getResult().size() > 0) {

                                                        for (DocumentSnapshot snapshot : task.getResult()) {

                                                            fBuildings = snapshot.toObject(FBuildings.class);
                                                            Log.e("TAG", "onComplete: " + fBuildings.getB_code());

                                                            String status = fBuildings.getStatus_id();
                                                            Log.e("TAG", "onComplete: Building status = " + status);

                                                            Intent intent = new Intent(AddBuildingActivity.this, MapActivity.class);
                                                            intent.putExtra("fbuildings", fBuildings);

/*
                                                            startActivity(new Intent(AddBuildingActivity.this,MapActivity.class)
                                                            .putExtra("fbuildings",fBuildings));*/

                                                            startActivity(intent);




                                                          /*  //Building Active
                                                            if (status.equalsIgnoreCase("lACNetniNe4gjp6nBvWP")) {
                                                                shoeAlertforPendingHouse();
                                                            } //meeting pending
                                                            else if (status.equalsIgnoreCase("rUyWv6FLEgZ0EIB6aNNP")) {
                                                                shoeAlertforPendingHouse();
                                                            }//meeting canceled
                                                            else if (status.equalsIgnoreCase("MWI1MTIe8Xv3Ls8Asa2X")) {
                                                                shoeAlertforPendingHouse();
                                                            }//flow up
                                                            else if (status.equalsIgnoreCase("zD0cviZ6Zab3GbWYu7tA")) {
                                                                shoeAlertforPendingHouse();
                                                            } //meeting done
                                                            else if (status.equalsIgnoreCase("qj3AJDoo5e58TYzZZ9lE")) {
                                                                shoeAlertforPendingHouse();
                                                            }//meeting rejected
                                                            else if (status.equalsIgnoreCase("cQ7jmazM2pAoMWtL213L")) {
                                                                shoeAlertforPendingHouse();
                                                            }*/
                                                        }

                                                        if (task.getResult().isEmpty()) {

                                                            //if Buildings Not Found in f_buildings
                                                            Toast.makeText(context, "No Building Found", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);

                                                            shoeAlertforhouseNotfound();
                                                        }


                                                    } else {

                                                        //if Buildings Not Found in f_buildings
//                        Toast.makeText(context, "No Building Found", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        shoeAlertforhouseNotfound();
                                                    }

                                                } else {

                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });


                                    }


                                }
                            }
                        });


            }
        });


    }


    public void checkTheHouseAvailability(String s) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Executing action...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        CollectionReference buildRef;
        buildRef = firebaseFirestore.collection(getString(R.string.col_fBuildings));

        Query buildingsQuery = buildRef.whereEqualTo("b_code", s);
        buildingsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            FBuildings fBuildings = documentSnapshot.toObject(FBuildings.class);
                            String status = fBuildings.getStatus_id();

                            if (status.equalsIgnoreCase("lACNetniNe4gjp6nBvWP")) {
                                shoeAlertforPendingHouse();
                            } else if (status.equalsIgnoreCase("rUyWv6FLEgZ0EIB6aNNP")) {
                                shoeAlertforPendingHouse();
                            } else if (status.equalsIgnoreCase("MWI1MTIe8Xv3Ls8Asa2X")) {
                                shoeAlertforPendingHouse();
                            } else if (status.equalsIgnoreCase("zD0cviZ6Zab3GbWYu7tA")) {
                                shoeAlertforPendingHouse();
                            } else if (status.equalsIgnoreCase("qj3AJDoo5e58TYzZZ9lE")) {
                                shoeAlertforPendingHouse();
                            } else if (status.equalsIgnoreCase("cQ7jmazM2pAoMWtL213L")) {
                                shoeAlertforPendingHouse();
                            }/*else {
                               // shoeAlertforhouseNotfound();
                               // progressDialog.dismiss();
                            }*/
                            progressDialog.dismiss();
                        }
                    } else {
                        //Toast.makeText(AddBuildingActivity.this, "No building found", Toast.LENGTH_SHORT).show();
                        shoeAlertforhouseNotfound();
                        progressDialog.dismiss();

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    public void showAllAreasAlert(List<String> areaList) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        areaListView = rowList.findViewById(R.id.listview);
        areaEdit = rowList.findViewById(R.id.search_edit);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, areaList);
        adapter.notifyDataSetChanged();
        areaListView.setAdapter(adapter);

        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        areaListView.setDivider(color);
        areaListView.setDividerHeight(1);


        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        areaEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddBuildingActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        areaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String areatxt = String.valueOf(parent.getItemAtPosition(position));
                areaCodePos = position;

                areaNameET.setText(areatxt);
                dialog.dismiss();


            }
        });
    }

    private void showAddressAlert(List<String> list, final EditText editText) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        ListView houseNoList = rowList.findViewById(R.id.listview);
        EditText houseNoEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        houseNoList.setAdapter(adapter);
        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        houseNoList.setDivider(color);
        houseNoList.setDividerHeight(2);
        houseNoList.setSelector(R.color.lightorange);
        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        houseNoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddBuildingActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        houseNoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String houseno = String.valueOf(parent.getItemAtPosition(position));
                editText.setText(houseno);
                dialog.dismiss();
            }
        });
    }

    public void showAvailableRoads() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        roadNumberList = rowList.findViewById(R.id.listview);
        roadNumberEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.road_no);
        //customListAdapter=new CustomListAdapter(this,allStringValues.road_no);
        roadNumberList.setAdapter(adapter);

        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        roadNumberList.setDivider(color);
        roadNumberList.setDividerHeight(2);
        roadNumberList.setSelector(R.color.lightorange);

        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        roadNumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                AddBuildingActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roadNumberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String roadno = String.valueOf(parent.getItemAtPosition(position));

                if (roadno.equals("None")) {
                    roadNumberET.setText("0");
                    roadListCode = "0";
                    //  Toast.makeText(AddBuildingActivity.this, roadListCode, Toast.LENGTH_SHORT).show();
                } else {
                    roadListCode = roadno;
                    roadNumberET.setText(roadno);
                    // Toast.makeText(AddBuildingActivity.this, roadListCode, Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

    }

    public void showAvailableBlock() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        blockList = rowList.findViewById(R.id.listview);
        blockEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.block_numbers);
        blockList.setAdapter(adapter);
        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        blockList.setDivider(color);
        blockList.setDividerHeight(2);
        blockList.setSelector(R.color.lightorange);

        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        blockEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddBuildingActivity.this.adapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        blockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String blockno = String.valueOf(parent.getItemAtPosition(position));

                if (blockno.equals("None")) {
//                    b_block.setText("0");
                    blockListCode = "0";
                    //  Toast.makeText(AddBuildingActivity.this, blockListCode, Toast.LENGTH_SHORT).show();
                } else {

//                    b_block.setText(blockno);
                    blockListCode = String.valueOf(position);
                    // Toast.makeText(AddBuildingActivity.this, blockListCode, Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

    }

    public void shoAvailableHouseNumner() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        houseNoList = rowList.findViewById(R.id.listview);
        houseNoEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.road_no);
        houseNoList.setAdapter(adapter);
        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        houseNoList.setDivider(color);
        houseNoList.setDividerHeight(2);
        houseNoList.setSelector(R.color.lightorange);

        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        houseNoEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                AddBuildingActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        houseNoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String houseno = String.valueOf(parent.getItemAtPosition(position));


                if (houseno.equals("None")) {
                    houseNumberET.setText("0");
                    houseListCode = "0";

                } else {
                    houseListCode = houseno;
                    houseNumberET.setText(houseno);
                }

                dialog.dismiss();
            }
        });
    }

    public void showAvailableHouseFormat() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        blockList = rowList.findViewById(R.id.listview);
        blockEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.block_numbers);
        blockList.setAdapter(adapter);
        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        blockList.setDivider(color);
        blockList.setDividerHeight(2);
        blockList.setSelector(R.color.lightorange);

        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        blockEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AddBuildingActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
/*
        blockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String blockno = String.valueOf(parent.getItemAtPosition(position));
                //housefrmntListCode = String.valueOf(position + 1);
                //b_housefrmt.setText(blockno);

                if (blockno.equals("None")) {
                    b_housefrmt.setText("0");
                    housefrmntListCode = "0";
                } else {

                    b_housefrmt.setText(blockno);
                    housefrmntListCode = String.valueOf(position);
                }

                dialog.dismiss();

            }
        });*/
    }

    public void saveImageToStorage() {
        final UploadTask uploadTask = addbldngRef.putFile(pickedImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddBuildingActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUri = addbldngRef.getDownloadUrl().toString();

                        return addbldngRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUri = task.getResult().toString();
                            try {
                                saveBuildingDataInDB();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

    }

    public void saveBuildingDataInDB() throws ParseException {

        if (b_totalfloor.getText().toString().isEmpty()) {
            b_totalfloor.setError("Insert total floor");
            b_totalfloor.requestFocus();
        }
        if (b_floorperflat.getText().toString().isEmpty()) {
            b_floorperflat.setError("Insert number of floor per flat");
            b_floorperflat.requestFocus();
        }
        if (b_totalguard.getText().toString().isEmpty()) {
            b_totalguard.setError("Insert the number of guards");
            b_totalguard.requestFocus();
        }
        if (b_flatfrmt.getText().toString().isEmpty()) {
            b_flatfrmt.setError("Insert the flat format");
            b_flatfrmt.requestFocus();
        }
        if (b_name.getText().toString().isEmpty()) {
            b_name.setError("Insert the house name");
            b_name.requestFocus();
        } else {
            String area = areaNameET.getText().toString();
            String road = roadNumberET.getText().toString();
            String roadNameSt=roadName.getText().toString();
//            String block = b_block.getText().toString();
            String houseNmbr = houseNumberET.getText().toString();
//            String housefrmt = b_housefrmt.getText().toString();
            String flatformat = b_flatfrmt.getText().toString();
            //status = b_status.getText().toString();
            status_id = statusIdList.get(statusCodePos);


            String theWholeAddress = area + " " + road + " " + houseNmbr + " " + districtCodeList.get(districtCodePos).toString();

            wholeAddress = theWholeAddress;

            districtValue = districtCodeList.get(districtCodePos).toString();

            totalHouseCode = areaCodeList.get(areaCodePos) + "" + roadListCode + "" + blockListCode + "" + houseListCode + "" + housefrmntListCode + "" + districtValue;

            String housename = b_name.getText().toString();
            String flatperfloor = b_floorperflat.getText().toString();
            String followupdate = b_follwing.getText().toString();


            String guards = b_totalguard.getText().toString();
            String totalfloor = b_totalfloor.getText().toString();

            int flatperFloor = Integer.parseInt(flatperfloor);
            int totlflr = Integer.parseInt(totalfloor);


            Normalfunc normalfunc = new Normalfunc();
            ArrayList<String> code_array = new ArrayList<>(normalfunc.splitchar(area));
            code_array.add(road);
            code_array.add(houseNmbr);
            code_array.add(totalHouseCode);


            totalCode = districtCodeList.get(districtCodePos).toString() + "*" + areaCodeList.get(areaCodePos) + "*" + roadNumberET.getText().toString().trim().replaceAll("\\s+", "") + "*" + houseNumberET.getText().toString().trim().replaceAll("\\s+", "");

            ArrayList<String> imageurl = new ArrayList<String>();
            imageurl.add(downloadImageUri);

            String build_id = docref_FBuildings.getId();

            String fWorkersDocID = docref_FWorkersBuildings.getId();

            Log.e("TAG", "saveBuildingDataInDB: build_id 1  = " + build_id);
            Log.e("TAG", "saveBuildingDataInDB: fWorkersDocID doc_ID 1  = " + fWorkersDocID);
            Log.e("TAG", "saveBuildingDataInDB: currentUserID f_UID 1  = " + currentUserID);
            Log.e("TAG", "saveBuildingDataInDB: status_id 1  = " + status_id);
            Log.e("TAG", "saveBuildingDataInDB: created date 1  = " + date);
            Log.e("TAG", "saveBuildingDataInDB: update date 1  = " + date);
            Log.e("TAG", "saveBuildingDataInDB: totalCode b_code 1  = " + totalCode);

            FWorkerBuilding fWorkerBuilding = new FWorkerBuilding(build_id,
                    fWorkersDocID,
                    currentUserID,
                    status_id, date,
                    date,
                    totalCode);

//            normalfunc.getTimestampFromDate(date);
            String date1 = b_follwing.getText().toString();

            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
            Log.d("TAG", "saveBuildingDataInDB: " + date2);


            Log.e("TAG", "saveBuildingDataInDB: build_id = " + build_id);
            Log.e("TAG", "saveBuildingDataInDB: wholeAddress =  " + wholeAddress);
            Log.e("TAG", "saveBuildingDataInDB: totalCode =  " + totalCode);
            Log.e("TAG", "saveBuildingDataInDB: houseNmbr =  " + houseNmbr);
            Log.e("TAG", "saveBuildingDataInDB: road Number =  " + road);
            Log.e("TAG", "saveBuildingDataInDB: districtValue =  " + districtValue);
            Log.e("TAG", "saveBuildingDataInDB: area =  " + areaCodeList.get(areaCodePos).toString());
            Log.e("TAG", "saveBuildingDataInDB: flatformat =  " + flatformat);
            Log.e("TAG", "saveBuildingDataInDB: flatperFloor =  " + flatperFloor);
            Log.e("TAG", "saveBuildingDataInDB: date2 =  " + date2);
            Log.e("TAG", "saveBuildingDataInDB: housename =  " + housename);
            Log.e("TAG", "saveBuildingDataInDB: totlflr =  " + totlflr);
            Log.e("TAG", "saveBuildingDataInDB: right time =  " + Calendar.getInstance().getTime());
            Log.e("TAG", "saveBuildingDataInDB: status_id =  " + status_id);
            Log.e("TAG", "saveBuildingDataInDB: imageurl =  " + imageurl);
            Log.e("TAG", "saveBuildingDataInDB: code_array =  " + code_array);
            Log.e("TAG", "saveBuildingDataInDB: lat =  " + lat);
            Log.e("TAG", "saveBuildingDataInDB: lan =  " + lan);

            fBuildings = new FBuildings(build_id,
                    wholeAddress, totalCode,
                    houseNmbr, road, districtValue,
                    areaCodeList.get(areaCodePos).toString(),
                    flatformat,
                    flatperFloor, date2, housename,
                    totlflr, Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime(),
                    status_id,
                    "Pending", imageurl, code_array,
                    lat, lan,roadNameSt);

            WriteBatch batch = firebaseFirestore.batch();


            DocumentReference fbuildingsReferance = firebaseFirestore.collection(getString(R.string.col_fBuildings)).document(build_id);
            batch.set(fbuildingsReferance, fBuildings);

            DocumentReference fWorkersBuildingsReferance = firebaseFirestore.collection(getString(R.string.col_fWorkerBuilding)).document(fWorkersDocID);
            batch.set(fWorkersBuildingsReferance, fWorkerBuilding);
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    FancyToast.makeText(context, "Data Saved Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                    progressBar.setVisibility(View.GONE);


                }
            });


        }


    }

    private void gotoMyHomeActvity() {
        Intent intent = new Intent(AddBuildingActivity.this, MyHomeActivity.class);
        startActivity(intent);
        finish();

    }

    public String add88withNumb(String s) {
        String number = "+88" + s;
        return number;
    }


    public void createBuildingsContactInfo() {

        if (people_we_talk.length() == 0) {
            people_we_talk.setError("Insert the People your are talking");
            people_we_talk.requestFocus();
        }
        if (b_peoplesName.length() == 0) {
            b_peoplesName.setError("Insert the name");
            b_peoplesName.requestFocus();
        }
        if (b_peopleNumber.getText().toString().length() <= 10) {
            b_peopleNumber.setError("Insert the valid mobile number");
            b_peopleNumber.requestFocus();
        } else {
            String design_type = people_we_talk.getText().toString();
            String design_name = b_peoplesName.getText().toString();
            String design_number = b_peopleNumber.getText().toString();
            String numbers = add88withNumb(design_number);

            totalCode = areaCodeList.get(areaCodePos) + "*" + roadListCode + "*" + blockListCode + "*" + houseListCode + "*" + housefrmntListCode + "*" + districtValue;
            totalHouseCode = areaCodeList.get(areaCodePos) + "" + roadListCode + "" + blockListCode + "" + houseListCode + "" + housefrmntListCode + "" + districtValue;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String strDate = mdformat.format(calendar.getTime());

            String doc_id = design_number + totalHouseCode;

            fbPeople = new FBPeople(totalCode, design_type, doc_id, design_name, numbers);

            firebaseFirestore.collection(getString(R.string.col_fBbuildingContacts)).document(design_number + totalHouseCode)
                    .set(fbPeople).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        //Toast.makeText(AddBuildingActivity.this, "number saved successfully", Toast.LENGTH_SHORT).show();

                        FancyToast.makeText(AddBuildingActivity.this, "number saved successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                        saveNumberBtn.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    //house not Visited yet
    public void shoeAlertforhouseNotfound() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AddBuildingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.house_not_found, null);

        Button btn = view.findViewById(R.id.houseVisitedAlertBtn);
        alert.setView(view);

        final AlertDialog alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(true);
        alertDialog1.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });


    }

    //house already Visited
    public void shoeAlertforPendingHouse() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AddBuildingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.house_status_pending, null);

        Button houseVisitedAlertBtn = view.findViewById(R.id.houseVisitedAlertBtn);
        TextView txt = view.findViewById(R.id.txt);
        alert.setView(view);

        final AlertDialog alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(true);
        alertDialog1.show();

        houseVisitedAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
                //relativeLayout.setVisibility(View.GO);
            }
        });
    }

    public void showTypeofPeopleInbuilding() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        peopleWeTalkList = rowList.findViewById(R.id.listview);
        areaEdit = rowList.findViewById(R.id.search_edit);

        firebaseFirestore.collection(getString(R.string.col_fPeopleType)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                areaList.clear();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    String area_eng = documentSnapshot.getString("type");
                    areaList.add(area_eng);

                }

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, areaList);
                adapter.notifyDataSetChanged();
                peopleWeTalkList.setAdapter(adapter);

            }
        });

        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        peopleWeTalkList.setDivider(color);
        peopleWeTalkList.setDividerHeight(1);
        peopleWeTalkList.setSelector(R.color.lightorange);
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        peopleWeTalkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String peoples = String.valueOf(parent.getItemAtPosition(position));
                people_we_talk.setText(peoples);
                dialog.dismiss();
            }
        });


    }

    public void showDistrict(List<String> districtList) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        districtListView = rowList.findViewById(R.id.listview);
        districtEdit = rowList.findViewById(R.id.search_edit);

        /*firebaseFirestore.collection(getString(R.string.col_district)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                districtList.clear();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    String area_district = documentSnapshot.getString("english");
                    //String area_ban = documentSnapshot.getString("bangla");
                    Long district_code = documentSnapshot.getLong("code");
                    districtCodeList.add(district_code);

                    Log.e("xxxx", area_district);
                    Log.e("xxxx", district_code.toString());
                    districtList.add(area_district);


                }

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, districtList);
                //customListAdapter=new CustomListAdapter(AddBuildingActivity.this,areaList);
                adapter.notifyDataSetChanged();
                districtListView.setAdapter(adapter);


            }
        });*/

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, districtList);
        //customListAdapter=new CustomListAdapter(AddBuildingActivity.this,areaList);
        adapter.notifyDataSetChanged();
        districtListView.setAdapter(adapter);

        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        districtListView.setDivider(color);
        districtListView.setDividerHeight(1);


        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        districtListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String distrct_code = String.valueOf(parent.getItemAtPosition(position));

                districtCodePos = position;
                districtNameET.setText(distrct_code);
                dialog.dismiss();
            }
        });

    }

    public void showBuildingStatus() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        ListView statusListView = rowList.findViewById(R.id.listview);
        EditText statusEdit = rowList.findViewById(R.id.search_edit);

        ArrayList<String> statusList = new ArrayList<>();

        firebaseFirestore.collection(getString(R.string.col_buildingstatus)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                statusList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {


                    String status = documentSnapshot.getString("status_type");
                    String bstatus_id = documentSnapshot.getString("status_id");


                    //statusList.add(status);
                    //Ignore Done status
                    if (!status.equalsIgnoreCase("Building Active") && !status.equalsIgnoreCase("Meeting Done")
                            && !status.equalsIgnoreCase("Meeting Rejected")) {

                        statusList.add(status);
                        statusIdList.add(bstatus_id);

                    }


                }

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, statusList);


                adapter.notifyDataSetChanged();
                statusListView.setAdapter(adapter);

            }
        });

        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        statusListView.setDivider(color);
        statusListView.setDividerHeight(1);


        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        statusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bstatus = String.valueOf(parent.getItemAtPosition(position));

                statusCodePos = position;

                b_status.setText(bstatus);
                dialog.dismiss();
            }
        });
    }

    public void addAllTypes() {
        types = new ArrayList<>();
        types.add("A1");
        types.add("1A");
        types.add("101");


        final StringAdapter stringAdapter = new StringAdapter(types, context);
        // adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, types);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();

        View convertView = (View) inflater.inflate(R.layout.custom_list, null);
        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);

        alertDialog.setView(convertView);
        alertDialog.setCancelable(false);

        lv.setAdapter(stringAdapter);
        alertDialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String typeselected = (String) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());
                b_flatfrmt.setText(typeselected);
                alertDialog.dismiss();
            }

        });
    }

    public void checkTrainOrNot() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AddBuildingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.trained_untrained_layout, null);

        Button btn = view.findViewById(R.id.houseVisitedAlertBtn);
        TextView txt = view.findViewById(R.id.txt);
        alert.setView(view);

        final AlertDialog alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
                Intent intent = new Intent(AddBuildingActivity.this, MyHomeActivity.class);
                startActivity(intent);
                //relativeLayout.setVisibility(View.GO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getTheLatLang() {

        if (ActivityCompat.checkSelfPermission(AddBuildingActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    lat = location.getLatitude();
                    lan = location.getLongitude();


                    Log.e("TAG", "lat:" + lat);
                    Log.e("TAG", "lat:" + lan);

                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        gotoMyHomeActvity();
    }

    public void noNumberFound(View view) {

        roadNumberET.setText("0");
    }
}
