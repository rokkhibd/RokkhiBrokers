package com.rokkhi.brokers.Ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.brokers.Model.AllStringValues;
import com.rokkhi.brokers.Model.FBPeople;
import com.rokkhi.brokers.Model.FBuildings;
import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Utils.Normalfunc;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateBldngInfoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView houseImage;
    EditText house_name, total_guards, followup_date, total_floor, flat_floor, house_address, flat_format,
            owner_name, owner_number, careataker_name, caretaker_number;
    FBuildings fBuildings;
    AllStringValues allStringValues;
    FBPeople fbPeople;
    Button updateInfo_Button;

    AutoCompleteTextView building_status;
    CircleImageView update_bldngImage;

    Normalfunc normalfunc;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DocumentReference docRef;
    DocumentSnapshot documentSnapshot;
    List<FBPeople> fbPeopleList;
    Date date;
    FirebaseUser currentUser;

    Bitmap bitmap;
    Uri pickedImageUri;

    StorageReference storageRef;
    String userId, downloadImageUri;

    ArrayAdapter<String> adapter;
    ListView statusListView;
    EditText statusEdit;
    ProgressDialog progressDialog;
    List<String> statusList = new ArrayList<>();
    String builidID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bldng_info);
        progressDialog = new ProgressDialog(this);

        fBuildings = new FBuildings();
        builidID = getIntent().getStringExtra("buildingID");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        getBuildingsInformation();

        storageRef = FirebaseStorage.getInstance().getReference()
                .child("fBuildings/" + userId + "/pic");

        fbPeople = new FBPeople();
        fbPeopleList = new ArrayList<>();

        house_name = findViewById(R.id.update_bldng_houseName);
        houseImage = findViewById(R.id.update_bldng_image);
        building_status = findViewById(R.id.update_bldng_bldngStatus);
        total_floor = findViewById(R.id.update_bldng_totalfloor);
        flat_floor = findViewById(R.id.update_bldng_flatpfloor);
        house_address = findViewById(R.id.update_bldng_houseAddress);
        flat_format = findViewById(R.id.update_bldng_flatformat);
        caretaker_number = findViewById(R.id.bldng_edit_caretakernmbr);
        followup_date = findViewById(R.id.update_bldng_followupdate);
        update_bldngImage = findViewById(R.id.update_bldng_image);

        //getThePeoplesInfo();
        normalfunc = new Normalfunc();
        date = Calendar.getInstance().getTime();
        updateInfo_Button = findViewById(R.id.update_bldng_updatebtn);

        allStringValues = new AllStringValues();
        updateInfo_Button.setOnClickListener(this);
        house_address.setOnClickListener(this);
        house_name.setOnClickListener(this);
        building_status.setOnClickListener(this);

        update_bldngImage.setOnClickListener(new View.OnClickListener() {
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
                            update_bldngImage.setImageBitmap(r.getBitmap());
//                            upload Image
//                            saveImageToStorage();

                        } else {
                            Toast.makeText(UpdateBldngInfoActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }).show(UpdateBldngInfoActivity.this);

            }
        });


    }
//get the Buildings Information
    private void getBuildingsInformation() {

        db.collection(getString(R.string.col_fBuildings)).document(builidID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    fBuildings = task.getResult().toObject(FBuildings.class);
                    house_name.setText(fBuildings.getHousename());
                    building_status.setText(fBuildings.getStatus());
                    total_floor.setText(String.valueOf(fBuildings.getTotalfloor()));
                    flat_floor.setText(String.valueOf(fBuildings.getFlatperfloor()));
                    house_address.setText(fBuildings.getB_address());
                    flat_format.setText(fBuildings.getFlatformat());

                    Glide.with(UpdateBldngInfoActivity.this).load(fBuildings.getB_imageUrl()).placeholder(R.drawable.building).into(houseImage);


                    String status=fBuildings.getStatus().toString();

                    if (status.equalsIgnoreCase("Building Active")){
                        updateInfo_Button.setVisibility(View.GONE);
                        //Toast.makeText(UpdateBldngInfoActivity.this, "You can not update the building Info", Toast.LENGTH_SHORT).show();
                        showMessageAlertDialogue();

                    }

                }
            }
        });

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.update_bldng_updatebtn) {

            progressDialog.setMessage("Executing Action...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (pickedImageUri == null) {
                updateBuildingInfo();
            } else {
                saveImageToStorage();
            }
        } else if (v.getId() == R.id.update_bldng_houseAddress) {
            house_address.setFocusableInTouchMode(true);
        } else if (v.getId() == R.id.update_bldng_followupdate) {
            AllStringValues.showCalendar(this, followup_date);
        } else if (v.getId() == R.id.update_bldng_houseName) {
            house_name.setFocusableInTouchMode(true);
        } else if (v.getId() == R.id.update_bldng_bldngStatus) {
            showBuildingStatus();
        }
    }

    private void showBuildingStatus() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        View rowList = getLayoutInflater().inflate(R.layout.adress_list, null);
        statusListView = rowList.findViewById(R.id.listview);
//        progressList=rowList.findViewById(R.id.progress_list);
        statusEdit = rowList.findViewById(R.id.search_edit);

        db.collection(getString(R.string.col_buildingstatus)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                statusList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String status = documentSnapshot.getString("status_type");
                    //statusList.add(status);

                    if (!status.equalsIgnoreCase("Building Active") && !status.equalsIgnoreCase("Meeting Done")){
                        statusList.add(status);
                    }


                    //statusList.add(status);
                }

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, statusList);
                //customListAdapter=new CustomListAdapter(AddBuildingActivity.this,areaList);
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
                String b_status = String.valueOf(parent.getItemAtPosition(position));
                building_status.setText(b_status);
                dialog.dismiss();
            }
        });
    }

    private void updateBuildingInfo() {

        WriteBatch batch = db.batch();
        String update_bstatus = building_status.getText().toString();
        String update_address = house_address.getText().toString();
        String update_houseName = house_name.getText().toString();
        String update_followdate=followup_date.getText().toString();

        ArrayList<String> b_array = new ArrayList<>(normalfunc.splitchar(update_houseName));

        DocumentReference docRef = db.collection(getString(R.string.col_fBuildings)).document(fBuildings.getBuild_id());

        Map<String, Object> map = new HashMap<>();
        map.put("b_address", update_address);
        map.put("housename", update_houseName);
        map.put("b_array", b_array);
        map.put("updated_at", date);
        map.put("status",update_bstatus);

        docRef.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    startActivity(new Intent(UpdateBldngInfoActivity.this, MyHomeActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    Toast.makeText(UpdateBldngInfoActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateBldngInfoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UpdateBldngInfoActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showMessageAlertDialogue(){

        AlertDialog.Builder alert = new AlertDialog.Builder(UpdateBldngInfoActivity.this);
        View view = getLayoutInflater().inflate(R.layout.status_done_layout, null);

        Button btn = view.findViewById(R.id.btn);
        TextView txt = view.findViewById(R.id.txt);
        alert.setView(view);

        final AlertDialog alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
                Intent intent=new Intent(UpdateBldngInfoActivity.this,MyHomeActivity.class);
                startActivity(intent);
                //relativeLayout.setVisibility(View.GO);
            }
        });
    }


    public void saveImageToStorage() {


        final StorageReference filePath = storageRef.child(pickedImageUri.getLastPathSegment() + ".jpg");
        final UploadTask uploadTask = filePath.putFile(pickedImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UpdateBldngInfoActivity.this, "Error Image Upload:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UpdateBldngInfoActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {


                            throw task.getException();
                        }
                        downloadImageUri = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            //get the image download Link
                            downloadImageUri = task.getResult().toString();


                            updateBuildingInfowithImage();
                        }
                    }
                });
            }
        });

    }


    private void updateBuildingInfowithImage() {

        WriteBatch batch = db.batch();

        String update_address = house_address.getText().toString();
        String update_houseName = house_name.getText().toString();
        String update_bstatus = building_status.getText().toString();
        //String update_followdate=followup_date.getText().toString();

        ArrayList<String> b_array = new ArrayList<>(normalfunc.splitchar(update_houseName));

        ArrayList<String> imageurl = new ArrayList<String>();
        imageurl.add(downloadImageUri);

        DocumentReference docRef = db.collection(getString(R.string.col_fBuildings)).document(fBuildings.getBuild_id());

        Map<String, Object> map = new HashMap<>();
        map.put("b_address", update_address);
        map.put("housename", update_houseName);
        map.put("b_array", b_array);
        map.put("status", update_bstatus);
        map.put("updated_at", date);


        map.put("b_imageUrl", imageurl);

        docRef.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    startActivity(new Intent(UpdateBldngInfoActivity.this, MyHomeActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    Toast.makeText(UpdateBldngInfoActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UpdateBldngInfoActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
