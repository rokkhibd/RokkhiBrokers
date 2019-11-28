package com.rokkhi.brokers.Ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Utils.Normalfunc;
import com.rokkhi.brokers.Utils.StringAdapter;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddBuildingActivity extends AppCompatActivity {

    AllStringValues allStringValues;
    ArrayAdapter<String> adapter;

    ProgressDialog progressDialog;
    ProgressBar progressBar;


    RelativeLayout relativeLayout;

    FirebaseFirestore db;
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

    Normalfunc normalfunc = new Normalfunc();

    CircleImageView circleImageView;
    EditText b_status, b_name, b_totalfloor, b_floorperflat, b_totalguard, b_lat, b_long, b_area, b_roadnumber, b_block, b_housenmbr, b_housefrmt,
            b_visit, b_follwing, b_code, b_peoplesName, b_peopleNumber, people_we_talk, b_district;

    Button saveBtn, tapCode, addInfoButton, checkHouseBtn, saveNumberBtn;

    String areaListCode, roadListCode, blockListCode, houseListCode, housefrmntListCode, totalHouseCode, status, flatformat, districtValue, downloadImageUri, totalCode;

    String wholeAddress, currentDate;

    ImageView visitCal, followpCal, statusMenu, flatfrmtMenu, district_Menu, designationMenu;

    DatePickerDialog datePickerDialog;

    EditText b_flatfrmt;

    int areaCodePos;
    int districtCodePos;
    List<Long> areaCodeList;

    List<Long> districtCodeList;

    FBPeople fbPeople;
    FBuildings fBuildings;

    String doc_id = "";
    Date date;
    ArrayList<String> types;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);
        areaCodeList = new ArrayList<>();
        districtCodeList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        docref_FBuildings = db.collection(getString(R.string.col_fBuildings)).document();

        docref_FWorkersBuildings = db.collection(getString(R.string.col_fWorkerBuilding)).document();

        //addbldngRef = FirebaseStorage.getInstance().getReference().child("fbldngs_photo");
        addbldngRef = FirebaseStorage.getInstance().getReference()
                .child("fBuildings/" + currentUserID + "/pic");

        fbPeople = new FBPeople();
        fBuildings = new FBuildings();

        date = Calendar.getInstance().getTime();
        context= AddBuildingActivity.this;
        progressDialog = new ProgressDialog(this);

        relativeLayout = findViewById(R.id.housecheck_layout);
        progressBar = findViewById(R.id.progressbar);

        b_peoplesName = findViewById(R.id.bldng_edit_theirname);
        b_peopleNumber = findViewById(R.id.bldng_edit_theirnumber);
        b_area = findViewById(R.id.bldng_edit_area);
        b_roadnumber = findViewById(R.id.bldng_edit_road);
        b_block = findViewById(R.id.bldng_edit_block);
        b_housenmbr = findViewById(R.id.bldng_edit_house);
        b_housefrmt = findViewById(R.id.bldng_edit_houseformat);
        b_district = findViewById(R.id.bldng_edit_disctrict);
        b_name = findViewById(R.id.bldng_edit_husename);
        b_floorperflat = findViewById(R.id.bldng_edit_totalflt);
        b_totalfloor = findViewById(R.id.bldng_edit_totalfloor);
        b_totalguard = findViewById(R.id.bldng_edit_totalguard);
        b_flatfrmt = findViewById(R.id.bldng_edit_flatformat);
        b_visit = findViewById(R.id.bldng_edit_visitdate);
        b_follwing = findViewById(R.id.bldng_edit_followingdate);
        b_code = findViewById(R.id.bldng_edit_bcode);
        b_status = findViewById(R.id.bldng_edit_status);
        b_lat = findViewById(R.id.bldng_edit_lat);
        b_long = findViewById(R.id.bldng_edit_long);
        people_we_talk = findViewById(R.id.bldng_edit_buildingspeople);

        tapCode = findViewById(R.id.tap_bcode);
        circleImageView = findViewById(R.id.building_photo);
        addInfoButton = findViewById(R.id.addblgnInfoBtn);
        checkHouseBtn = findViewById(R.id.bldng_button_housecheck);
        saveNumberBtn = findViewById(R.id.bldng_edit_numberSave);

        visitCal = findViewById(R.id.visitcalimg);
        followpCal = findViewById(R.id.followingcalimg);
        statusMenu = findViewById(R.id.statusMenu);
        flatfrmtMenu = findViewById(R.id.flatformatMenu);
        district_Menu = findViewById(R.id.districtMenu);
        designationMenu = findViewById(R.id.choosePeopleMenu);

        allStringValues = new AllStringValues();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allStringValues.status);
        //b_status.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allStringValues.flatformat);
        //b_flatfrmt.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allStringValues.district);
        //b_district.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allStringValues.designation);


        //TODO: get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());
        //Toast.makeText(AddBuildingActivity.this, currentDate, Toast.LENGTH_SHORT).show();

        addInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (pickedImageUri == null) {
                   // Toast.makeText(AddBuildingActivity.this, "No Image", Toast.LENGTH_SHORT).show();
                    saveBuildingDataInDB();
                } else {
                  //  Toast.makeText(AddBuildingActivity.this, "Image", Toast.LENGTH_SHORT).show();

                    saveImageToStorage();

                }

            }
        });

        saveNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        b_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDistrict();
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

        district_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //b_district.showDropDown();


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
        db.collection("area").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                areaList.clear();

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    String area_eng = documentSnapshot.getString("english");
                    String area_ban = documentSnapshot.getString("bangla");
                    Long area_code = documentSnapshot.getLong("code");
                    areaCodeList.add(area_code);

                    areaList.add(area_eng + "(" + area_ban + ")");

                }
               // Toast.makeText(AddBuildingActivity.this, "Loaded Area", Toast.LENGTH_SHORT).show();
                b_area.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAllAreasAlert(areaList);


                    }
                });

            }
        });
        //Get Area Data End


        b_roadnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableRoads();

            }
        });

        b_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableBlock();
                //showAddressAlert(Arrays.asList(allStringValues.block_numbers),b_block);
            }
        });

        b_housenmbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoAvailableHouseNumner();

                //showAddressAlert(Arrays.asList(allStringValues.road_no),b_housenmbr);
            }
        });

        b_housefrmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAvailableHouseFormat();
                //showAddressAlert(Arrays.asList(allStringValues.block_numbers),b_housefrmt);
            }
        });

        checkHouseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (b_area.length() == 0) {
                    b_area.setError("Insert the area name");
                } else if (b_roadnumber.length() == 0) {
                    b_roadnumber.setError("Insert the road number");
                } else if (b_block.length() == 0) {
                    b_block.setError("Insert the block number");
                } else if (b_district.length() == 0) {
                    b_block.setError("Insert the District number");
                } else {

                    String area = b_area.getText().toString();
                    String road = b_roadnumber.getText().toString();
                    String block = b_block.getText().toString();
                    String houseNmbr = b_housenmbr.getText().toString();
                    String housefrmt = b_housefrmt.getText().toString();
                    districtValue = b_district.getText().toString();


                    String theWholeAddress = area + " " + road + block + " " + houseNmbr + housefrmt + " " + districtValue;

                    wholeAddress = theWholeAddress;

                    districtValue = String.valueOf(1);

                    //Toast.makeText(AddBuildingActivity.this, districtValue, Toast.LENGTH_SHORT).show();

                    totalCode = areaCodeList.get(areaCodePos) + "*" + roadListCode + "*" + blockListCode + "*" + houseListCode + "*" + housefrmntListCode + "*" + districtCodeList.get(districtCodePos);

                   // Toast.makeText(AddBuildingActivity.this, "" + totalCode, Toast.LENGTH_SHORT).show();


                    totalHouseCode = areaListCode + "" + roadListCode + "" + blockListCode + "" + houseListCode + "" + housefrmntListCode + "" + districtValue;

                    //b_code.setText(totalHouseCode);
                    checkTheHouseAvailability(totalCode);
                }

            }
        });



    }


    public void checkTheHouseAvailability(String s) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Executing action...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        CollectionReference buildRef;
        buildRef = db.collection(getString(R.string.col_fBuildings));

        Query buildingsQuery = buildRef.whereEqualTo("b_code", s);
        buildingsQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            FBuildings fBuildings = documentSnapshot.toObject(FBuildings.class);
                            String status = fBuildings.getStatus();

                            if (status.equalsIgnoreCase("Done")) {
                                shoeAlertforPendingHouse();
                            } if (status.equalsIgnoreCase("Meeting Pending")) {
                                shoeAlertforPendingHouse();
                            } if (status.equalsIgnoreCase("Cancelled")){
                                shoeAlertforhouseNotfound();
                            } if (status.equalsIgnoreCase("Followup")){
                                shoeAlertforPendingHouse();
                            } if (status.equalsIgnoreCase("Meeting Done")){
                                shoeAlertforPendingHouse();
                            }if (status.equalsIgnoreCase("Meeting Rejected")){
                                shoeAlertforPendingHouse();
                            }
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

                b_area.setText(areatxt);
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
                    b_roadnumber.setText("0");
                    roadListCode = "0";
                    //  Toast.makeText(AddBuildingActivity.this, roadListCode, Toast.LENGTH_SHORT).show();
                } else {
                    roadListCode = roadno;
                    b_roadnumber.setText(roadno);
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
                    b_block.setText("0");
                    blockListCode = "0";
                    //  Toast.makeText(AddBuildingActivity.this, blockListCode, Toast.LENGTH_SHORT).show();
                } else {

                    b_block.setText(blockno);
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
                    b_housenmbr.setText("0");
                    houseListCode = "0";

                } else {

                    houseListCode = houseno;
                    b_housenmbr.setText(houseno);
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
        });
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
                            saveBuildingDataInDB();
                        }
                    }
                });
            }
        });

    }

    public void saveBuildingDataInDB() {

        if (b_totalfloor.length() == 0) {
            b_totalfloor.setError("Insert total floor");
            b_totalfloor.requestFocus();
        } if (b_floorperflat.length() == 0) {
            b_floorperflat.setError("Insert number of floor per flat");
            b_floorperflat.requestFocus();
        }  if (b_totalguard.length() == 0) {
            b_totalguard.setError("Insert the number of guards");
            b_totalguard.requestFocus();
        }  if (b_flatfrmt.length() == 0) {
            b_flatfrmt.setError("Insert the flat format");
            b_flatfrmt.requestFocus();
        }  if (b_name.length() == 0) {
            b_name.setError("Insert the house name");
            b_name.requestFocus();
        } else {
            String area = b_area.getText().toString();
            String road = b_roadnumber.getText().toString();
            String block = b_block.getText().toString();
            String houseNmbr = b_housenmbr.getText().toString();
            String housefrmt = b_housefrmt.getText().toString();
            String flatformat = b_flatfrmt.getText().toString();
            districtValue = b_district.getText().toString();
            status = b_status.getText().toString();

            String theWholeAddress = area + " " + road + block + " " + houseNmbr + housefrmt + " " + districtValue;

            wholeAddress = theWholeAddress;

            districtValue = String.valueOf(1);
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


            ArrayList<String> imageurl = new ArrayList<String>();
            imageurl.add(downloadImageUri);

            String build_id = docref_FBuildings.getId();

            String fWorkersDocID = docref_FWorkersBuildings.getId();

            FWorkerBuilding fWorkerBuilding=new FWorkerBuilding(build_id,fWorkersDocID, currentUserID,status,date,date,totalCode);

            fBuildings = new FBuildings(build_id, wholeAddress, totalCode, houseNmbr, road, districtValue, area, flatformat,
                    flatperFloor, date, housename, totlflr, date, date, status, "Pending", imageurl, code_array, 0, 0);

            WriteBatch batch=db.batch();


            DocumentReference fbuildingsReferance= db.collection(getString(R.string.col_fBuildings)).document(build_id);
            batch.set(fbuildingsReferance,fBuildings);

            DocumentReference fWorkersBuildingsReferance=db.collection(getString(R.string.col_fWorkerBuilding)).document(fWorkersDocID);
            batch.set(fWorkersBuildingsReferance,fWorkerBuilding);
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    gotoMyHomeActvity();
                    progressBar.setVisibility(View.GONE);

                }
            });


           /* db.collection(getString(R.string.col_fBuildings)).document(build_id).set(fBuildings).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        gotoMyHomeActvity();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddBuildingActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });*/
        }


    }

    private void gotoMyHomeActvity() {
        Intent intent = new Intent(AddBuildingActivity.this, MyHomeActivity.class);
        startActivity(intent);

    }

    public String add88withNumb(String s) {
        String number = "+88" + s;
        return number;
    }


    public void createBuildingsContactInfo() {

        if(people_we_talk.length()==0){
            people_we_talk.setError("Insert the People your are talking");
            people_we_talk.requestFocus();
        }
        if(b_peoplesName.length()==0){
            b_peoplesName.setError("Insert the name");
            b_peoplesName.requestFocus();
        }
        if (b_peopleNumber.length()==0){
            b_peopleNumber.setError("Insert the mobile number");
            b_peopleNumber.requestFocus();
        }else {
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

            db.collection(getString(R.string.col_fBbuildingContacts)).document(design_number + totalHouseCode)
                    .set(fbPeople).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(AddBuildingActivity.this, "number saved successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void shoeAlertforhouseNotfound() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AddBuildingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.house_not_found, null);

        Button btn = view.findViewById(R.id.btn);
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

    public void shoeAlertforPendingHouse() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AddBuildingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.house_status_pending, null);

        Button btn = view.findViewById(R.id.btn);
        TextView txt = view.findViewById(R.id.txt);
        alert.setView(view);

        final AlertDialog alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(true);
        alertDialog1.show();

        btn.setOnClickListener(new View.OnClickListener() {
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

        db.collection(getString(R.string.col_fPeopleType)).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void showDistrict() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        districtListView = rowList.findViewById(R.id.listview);
        districtEdit = rowList.findViewById(R.id.search_edit);

        db.collection(getString(R.string.col_district)).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        });

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
                b_district.setText(distrct_code);
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

        db.collection(getString(R.string.col_buildingstatus)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                statusList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String status = documentSnapshot.getString("status_type");
                    statusList.add(status);
                    //Ignore Done status
                    /*if (!status.equalsIgnoreCase("Done")){
                        statusList.add(status);
                    }*/

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


        final StringAdapter stringAdapter=new StringAdapter(types,context);
       // adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, types);
        final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
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
}
