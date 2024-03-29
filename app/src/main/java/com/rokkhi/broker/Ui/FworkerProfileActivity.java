package com.rokkhi.broker.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.broker.MainActivity;
import com.rokkhi.broker.Model.AllStringValues;
import com.rokkhi.broker.Model.CustomListAdapter;
import com.rokkhi.broker.Model.FPayments;
import com.rokkhi.broker.Model.FWorkers;
import com.rokkhi.broker.Model.Users;
import com.rokkhi.broker.R;
import com.rokkhi.broker.Utils.Normalfunc;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class FworkerProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText f_gender;
    EditText f_area;

    AllStringValues allStringValues;

    Button saveData;

    String userPhoneNumber, currentDate;
    Date date;

    DatePickerDialog.OnDateSetListener datedialog;
    Calendar myCalendar;

    CustomListAdapter customListAdapter;

    ImageView areaMenu, genderMenu, dobCal, joinDateCal;
    EditText f_name, f_road, f_block, f_houseno, f_roadletter, f_phone, f_nid, f_dob, f_uni, f_mail, f_joindate, f_bkash, f_refId;
    TextView knowMore, mblNumberget, bkashNumberget;

    //TODO: Creates String variable


    ArrayAdapter<String> adapter;

    Normalfunc normalfunc;

    List<String> areaList = new ArrayList<>();

    ListView roadNumberList, blockList, houseNoList, areaListView, genderList;
    EditText roadNumberEdit, blockEdit, houseNoEdit, areaEdit, genderEdit;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;
    FirebaseUser currentUser;
    String userId, downloadImageUri = "";

    DatePickerDialog datePickerDialog;
    CircleImageView circleImageView;

    Bitmap bitmap;
    Uri pickedImageUri;


    ProgressBar progressBar, spinKitProgressBar;

    Users users;
    FWorkers fWorkers;
    FPayments fPayments;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fworker_profile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        context = FworkerProfileActivity.this;
        myCalendar=Calendar.getInstance();
        normalfunc = new Normalfunc(context);
        // storageRef = FirebaseStorage.getInstance().getReference().child("fworkers_photo");


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            userId = currentUser.getUid();
        }


        storageRef = FirebaseStorage.getInstance().getReference()
                .child("users/" + userId + "/pic");


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = sdf.format(new Date());


        users = new Users();
        fWorkers = new FWorkers();
        fPayments = new FPayments();

        date = Calendar.getInstance().getTime();

        saveData = findViewById(R.id.fworker_data_btn);
        f_area = findViewById(R.id.fworker_address_area);
        f_gender = findViewById(R.id.fworker_gender_edit);
        f_name = findViewById(R.id.fworker_name_edit);
        f_road = findViewById(R.id.fworker_address_road);
        f_block = findViewById(R.id.fworker_address_block);
        f_houseno = findViewById(R.id.fworker_address_housenmbr);
        f_roadletter = findViewById(R.id.fworker_address_roadletter);
        f_phone = findViewById(R.id.fworker_phone_edit);
        f_nid = findViewById(R.id.fworker_nid_edit);
        f_dob = findViewById(R.id.fworker_dob_edit);
        f_uni = findViewById(R.id.fworker_uni_edit);
        f_mail = findViewById(R.id.fworker_mail_edit);
        f_joindate = findViewById(R.id.fworker_joining_edit);
        f_bkash = findViewById(R.id.fworker_bkash_edit);
        f_refId = findViewById(R.id.fworker_refcode_edit);
        knowMore = findViewById(R.id.ref_knowMore_txt);
        mblNumberget = findViewById(R.id.getnumber_txt);
        bkashNumberget = findViewById(R.id.getbkashnumber_txt);

        mblNumberget.setOnClickListener(this);
        bkashNumberget.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);

        spinKitProgressBar = findViewById(R.id.spin_kit);
        Wave wave = new Wave();
        spinKitProgressBar.setIndeterminateDrawable(wave);

        allStringValues = new AllStringValues();

        //TODO: ImageViews ID
        genderMenu = findViewById(R.id.gender_menu);
        dobCal = findViewById(R.id.calendar);
        joinDateCal = findViewById(R.id.calendar_joining);
        circleImageView = findViewById(R.id.fworker_photo);

        //check the valid phone Number
        f_refId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("TAG", "afterTextChanged: "+s );
                Log.e("TAG", "afterTextChanged: "+normalfunc.makephone14(s.toString()) );

                if (s.length()==11){

                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

                    firebaseFirestore.collection(getString(R.string.col_fWorkers))
                            .whereEqualTo("fw_phone",normalfunc.makephone14(s.toString()))
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().isEmpty()){

                                f_refId.setError("Enter Valid Number");
                                f_refId.requestFocus();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }

                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Log.e("TAG", document.getId() + " => " + document.getString("fw_phone"));

                                }
                            }else {
                                f_refId.setError("Enter Valid Number");
                                f_refId.requestFocus();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            f_refId.setError("Enter Valid Number");
                            f_refId.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    });



                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allStringValues.gender);


        db.collection(getString(R.string.col_area)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                areaList.clear();

                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        String area_eng = documentSnapshot.getString("english");
                        String area_ban = documentSnapshot.getString("bangla");

                        areaList.add(area_eng + "(" + area_ban + ")");


                    }


                }

            }
        });


        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                spinKitProgressBar.setVisibility(View.VISIBLE);


                if (pickedImageUri == null) {

                    //saveAllDataToFirestore();
                    //Toast.makeText(context, "Select Your Profile picture...", Toast.LENGTH_SHORT).show();

                    FancyToast.makeText(context,"Select Your Profile picture",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();

                    spinKitProgressBar.setVisibility(View.GONE);

                }  else {

                    saveImageToStorage();

                }


            }
        });

        knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(FworkerProfileActivity.this);
                View view = getLayoutInflater().inflate(R.layout.referral_show_dialog, null);
                alert.setView(view);

                final AlertDialog alertDialog1 = alert.create();
                alertDialog1.setCanceledOnTouchOutside(true);
                alertDialog1.show();


            }
        });

        f_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //f_area.showDropDown();
                showallAreas();
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


                        } else {
                            Toast.makeText(FworkerProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }).show(FworkerProfileActivity.this);

            }
        });

        f_roadletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableRoadLetter();
            }
        });

        f_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableRoads();
            }
        });

        f_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dateBirthday = Calendar.getInstance().getTime();

                AllStringValues.showCalendar(FworkerProfileActivity.this, f_dob);

            }
        });

        f_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableBlock();
            }
        });
        f_houseno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvailableHouseno();
            }
        });

        f_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGender();
            }
        });

        dobCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllStringValues.showCalendar(FworkerProfileActivity.this, f_dob);
            }
        });

        joinDateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllStringValues.showCalendar(FworkerProfileActivity.this, f_joindate);

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.getnumber_txt) {
            getTheCurrentUserPhoneNumber(f_phone);
        } else if (v.getId() == R.id.getbkashnumber_txt) {
            getTheCurrentUserPhoneNumber(f_bkash);
        }
    }

    private void showallAreas() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        areaListView = rowList.findViewById(R.id.listview);
        areaEdit = rowList.findViewById(R.id.search_edit);


        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, areaList);
        adapter.notifyDataSetChanged();
        areaListView.setAdapter(adapter);

        //areListView.setAdapter(adapter);
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
                FworkerProfileActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        areaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String areatxt = String.valueOf(parent.getItemAtPosition(position));
                f_area.setText(areatxt);
                dialog.dismiss();
            }
        });


    }

    private void showAvailableHouseno() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        houseNoList = rowList.findViewById(R.id.listview);
        houseNoEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.road_no);
        //customListAdapter=new CustomListAdapter(this,allStringValues.road_no);
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

                FworkerProfileActivity.this.adapter.getFilter().filter(s);
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
                    f_houseno.setText("0");
                } else {

                    f_houseno.setText(houseno);
                }


                dialog.dismiss();
            }
        });


    }

    private void showAvailableBlock() {

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
                FworkerProfileActivity.this.adapter.getFilter().filter(s);

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
                    f_block.setText("0");
                } else {

                    f_block.setText(blockno);

                }
                dialog.dismiss();

            }
        });


    }

    private void showAvailableRoadLetter() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        roadNumberList = rowList.findViewById(R.id.listview);
        roadNumberEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.block_numbers);
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

                FworkerProfileActivity.this.adapter.getFilter().filter(s);
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
                    f_roadletter.setText("0");
                } else {

                    f_roadletter.setText(roadno);
                }

                dialog.dismiss();
            }
        });

    }

    private void showAvailableRoads() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        roadNumberList = rowList.findViewById(R.id.listview);
        roadNumberEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.road_no);
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

                FworkerProfileActivity.this.adapter.getFilter().filter(s);
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
                    f_road.setText("0");
                } else {

                    f_road.setText(roadno);
                }

                dialog.dismiss();
            }
        });


    }

    public void saveAllDataToFirestore() {


        if (f_name.getText().length() == 0) {
            f_name.setError("Insert your name");
            f_name.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_area.length() == 0) {
            f_area.setError("Insert your area name");
            f_area.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_road.length() == 0) {
            f_road.setError("Insert your Road Number");
            f_road.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_block.length() == 0) {
            f_block.setError("Insert your Block Number");
            f_block.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_houseno.length() == 0) {
            f_houseno.setError("Insert your House Number");
            f_houseno.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_roadletter.length() == 0) {
            f_roadletter.setError("Insert your House Block");
            f_roadletter.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_phone.length() == 0) {
            f_phone.setError("Insert your mobile number");
            f_phone.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }

        if (f_refId.getText().toString().isEmpty()) {
            f_refId.requestFocus();
            f_refId.setError("Enter Referral Number");

            spinKitProgressBar.setVisibility(View.GONE);
            return;

        }

        if (!normalfunc.isvalidphone(f_refId.getText().toString())) {
            f_refId.requestFocus();
            f_refId.setError("Enter Valid Number");
            spinKitProgressBar.setVisibility(View.GONE);
            return;

        }

        if (TextUtils.isEmpty(f_nid.getText().toString())) {
            f_nid.setError("Insert Your NID Number");
            f_nid.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(f_bkash.getText().toString())) {
            f_bkash.setError("Insert Your Bkash Number");
            f_bkash.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }

        if (f_mail.getText().toString().isEmpty() || !normalfunc.isValidEmail(f_mail.getText().toString())) {
            f_mail.setError("Insert Valid E-mail");
            f_mail.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }

        String fw_name=f_name.getText().toString();
        String fw_area = f_area.getText().toString();
        String fw_road = f_road.getText().toString();
        String fw_block = f_block.getText().toString();
        String fw_housenmbr = f_houseno.getText().toString();
        String fw_houseletter = f_roadletter.getText().toString();
        String fphone = normalfunc.makephone14(f_phone.getText().toString());

        List<String> u_array = normalfunc.splitchar(fphone);

        String fw_nid = f_nid.getText().toString();
        String fw_uni = f_uni.getText().toString();
        String fw_address = fw_area + " " + fw_road + fw_block + " " + fw_housenmbr + fw_houseletter;


        List<String> atoken = fWorkers.getAtoken();
        List<String> itoken = fWorkers.getItoken();



        fWorkers = new FWorkers(userId,fw_name, fw_nid, fphone, fw_uni, fw_address, date, date, false, u_array, atoken, itoken);

        db.collection("fWorkers").document(userId)
                .set(fWorkers)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            spinKitProgressBar.setVisibility(View.GONE);
                            Toast.makeText(FworkerProfileActivity.this, "Data saved!!", Toast.LENGTH_SHORT).show();
                            try {
                                saveDataToUserCollection();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FworkerProfileActivity.this, "Error!!" + e, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void saveDataToUserCollection() throws ParseException {
        String fname = f_name.getText().toString();
        String fphone = f_phone.getText().toString();
        String fw_gender = f_gender.getText().toString();
        String fw_mail = f_mail.getText().toString();
        String phone=normalfunc.makephone14(fphone);


        List<String> fw_name = normalfunc.splitstring(fname);

        String totalString = fw_mail + "," + fphone + "," + fw_name;
        String[] tagArray = totalString.split("\\s*,\\s*");

        List<String> u_array = Arrays.asList(tagArray);


        String date1 = f_dob.getText().toString();

        Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(date1);


        users = new Users(fname, downloadImageUri, downloadImageUri, userId, date2, date, fw_gender, fw_mail, phone, u_array);

        db.collection(getString(R.string.col_users)).document(userId).set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    spinKitProgressBar.setVisibility(View.GONE);
                    Toast.makeText(FworkerProfileActivity.this, "Saving Data...Wait", Toast.LENGTH_SHORT).show();

                    savePaymentData();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                spinKitProgressBar.setVisibility(View.GONE);
                Toast.makeText(FworkerProfileActivity.this, "Error!!" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void savePaymentData() {

        String ref_id = f_refId.getText().toString();
        String fw_phone = normalfunc.makephone14(f_phone.getText().toString());
        String fw_bkash = normalfunc.makephone14(f_bkash.getText().toString());
        String fw_nogod = "";

        fPayments = new FPayments(userId, normalfunc.makephone14(ref_id), fw_phone, 0, 0, 0, 0, 0, 0, 0, fw_bkash, fw_nogod, date, date, date, 0, 0, 0, 0);

        db.collection(getString(R.string.col_fPayment)).document(userId).set(fPayments).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    stayAtMainActvity();

                    //Toast.makeText(FworkerProfileActivity.this, "payment data saved", Toast.LENGTH_SHORT).show();

                    FancyToast.makeText(FworkerProfileActivity.this,"payment data saved",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(FworkerProfileActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveImageToStorage() {


        if (f_name.getText().toString().isEmpty()) {
            f_name.setError("Insert your name");
            f_name.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_area.getText().toString().isEmpty()) {
            f_area.setError("Insert your area name");
            f_area.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_road.getText().toString().isEmpty()) {
            f_road.setError("Insert your Road Number");
            f_road.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_block.getText().toString().isEmpty()) {
            f_block.setError("Insert your Block Number");
            f_block.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_houseno.getText().toString().isEmpty()) {
            f_houseno.setError("Insert your House Number");
            f_houseno.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_roadletter.getText().toString().isEmpty()) {
            f_roadletter.setError("Insert your House Block");
            f_roadletter.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (f_phone.getText().toString().isEmpty())   {
            f_phone.setError("Insert your mobile number");
            f_phone.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (!normalfunc.isvalidphone(f_phone.getText().toString()))   {
            f_phone.setError("Insert valid mobile number");
            f_phone.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }


        if (f_refId.getText().toString().isEmpty()) {
            f_refId.requestFocus();
            f_refId.setError("Enter Referral Number");

            spinKitProgressBar.setVisibility(View.GONE);
            return;

        }

        if (!normalfunc.isvalidphone(f_refId.getText().toString())) {
            f_refId.requestFocus();
            f_refId.setError("Enter Valid Number");
            spinKitProgressBar.setVisibility(View.GONE);
            return;

        }

        if (TextUtils.isEmpty(f_nid.getText().toString())) {
            f_nid.setError("Insert Your NID Number");
            f_nid.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(f_bkash.getText().toString())) {
            f_bkash.setError("Insert Your Bkash Number");
            f_bkash.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }

        if (!normalfunc.isvalidphone(f_bkash.getText().toString()))   {
            f_bkash.setError("Insert valid bkash number");
            f_bkash.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }

        if (f_mail.getText().toString().isEmpty() || !normalfunc.isValidEmail(f_mail.getText().toString())) {
            f_mail.setError("Insert Valid E-mail");
            f_mail.requestFocus();
            spinKitProgressBar.setVisibility(View.GONE);
            return;
        }


        final UploadTask uploadTask = storageRef.putFile(pickedImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(FworkerProfileActivity.this, "Error Image Upload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(FworkerProfileActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                FancyToast.makeText(context,"Image Uploaded Successfully",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,false).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUri = storageRef.getDownloadUrl().toString();
                        return storageRef.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            //get the image download Link
                            downloadImageUri = task.getResult().toString();

                            saveData.setVisibility(View.GONE);
                            saveAllDataToFirestore();
                        }
                    }
                });
            }
        });

    }

    private void stayAtMainActvity() {
        Intent intent = new Intent(FworkerProfileActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }


    public void getTheCurrentUserPhoneNumber(EditText s) {

        currentUser = mAuth.getCurrentUser();
        userPhoneNumber = currentUser.getPhoneNumber();

        s.setText(Normalfunc.getNumberWithoutCountryCode(userPhoneNumber));

    }

    public void showGender() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        genderList = rowList.findViewById(R.id.listview);
        genderEdit = rowList.findViewById(R.id.search_edit);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allStringValues.gender);
        genderList.setAdapter(adapter);
        ColorDrawable color = new ColorDrawable(this.getResources().getColor(R.color.lightorange));
        genderList.setDivider(color);
        genderList.setDividerHeight(2);
        genderList.setSelector(R.color.lightorange);

        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        genderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gender = String.valueOf(parent.getItemAtPosition(position));
                f_gender.setText(gender);

                dialog.dismiss();
            }
        });

    }
}
