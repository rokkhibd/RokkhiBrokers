package com.rokkhi.brokers.Ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rokkhi.brokers.Model.Users;
import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Utils.Normalfunc;
import com.rokkhi.brokers.Utils.StringAdapter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements IPickResult {


    FirebaseFirestore firebaseFirestore;

    SharedPreferences sharedPref;
    CircleImageView propic;
    TextView upload;
    EditText name,usermail,gender,bday,nid;
    Button done;
    String picurl="";
    String mFileUri = "";
    private Bitmap bitmap = null;
    ProgressBar progressBar;
    private static final String TAG = "EditProfileActivity";
    private long mLastClickTime = 0;
    StorageReference photoRef;
    Normalfunc normalfunc;
    Context context;
    Users users;
    ImageView datepicker;
    ArrayList<String> types;
    DatePickerDialog.OnDateSetListener datedialog;
    Date mdate = new Date();
    Calendar myCalendar;
    Users users2;

    String flatid = "", buildid = "", commid = "",famid="",userid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        flatid = sharedPref.getString("flatid", "none");
        buildid = sharedPref.getString("buildid", "none");
        commid = sharedPref.getString("commid", "none");
        famid = sharedPref.getString("familyid", "none");
        userid = sharedPref.getString("userid", "none");
        if(userid.equals("none")){
            userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            SharedPreferences.Editor editor= sharedPref.edit();
            editor.putString("userid",userid);
            editor.apply();
        }
        context= EditProfileActivity.this;
        normalfunc= new Normalfunc(context);

        propic = findViewById(R.id.user_photo);
        upload = findViewById(R.id.changeProfilePhoto);
        name = findViewById(R.id.user_name);
        gender = findViewById(R.id.gender);
        bday = findViewById(R.id.bday);

        // building = findViewById(R.id.bda);
        done = findViewById(R.id.done);
        //pass = findViewById(R.id.gatepass);
        usermail = findViewById(R.id.usermail);
        progressBar = findViewById(R.id.progressBar1);
        datepicker= findViewById(R.id.datepicker);
        myCalendar = Calendar.getInstance();

        init();
        buttonclick();
    }

    Dialog mdialog;

    public void initdialog(){
        mdialog=new Dialog(context);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.custom_progress);
        mdialog.getWindow ().setBackgroundDrawableResource (android.R.color.transparent);
    }

    public void showdialog(){
        mdialog.show();
    }
    public void dismissdialog(){
        mdialog.dismiss();
    }



    public void init() {
        firebaseFirestore.collection(getString(R.string.col_users)).document(userid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(e!=null)return;
                        if(documentSnapshot.exists()){
                            users = documentSnapshot.toObject(Users.class);
                            name.setText(users.getName());
                            usermail.setText(users.getMail());
                            picurl = users.getPic();
                            gender.setText(users.getGender());
                            if(users.getBday()!=null && Calendar.getInstance().getTime().compareTo(users.getBday())>0)bday.setText(normalfunc.convertDate(users.getBday()));
                            else bday.setText("");
                            mdate= users.getBday();
                            if(context!=null) {
                                Log.d(TAG, "onEvent:");
                                if(!picurl.equals("none") && !picurl.isEmpty()) Glide.with(getApplicationContext()).load(picurl).into(propic);
                            }
                        }

                    }
                });
    }

    public void addalltypes() {
        types = new ArrayList<>();
        types.add("male");
        types.add("female");
        types.add("other");

        final StringAdapter valueAdapter = new StringAdapter(types, context);
        final AlertDialog alertcompany = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.custom_list, null);
        final ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        alertcompany.setView(convertView);
        alertcompany.setCancelable(false);
        //valueAdapter.notifyDataSetChanged();

        lv.setAdapter(valueAdapter);
        alertcompany.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String typeselected = (String) lv.getItemAtPosition(position);
                //cname.setText(myoffice.getName());
                gender.setText(typeselected);
                alertcompany.dismiss();
            }
        });

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());


        bday.setText(sdf.format(myCalendar.getTime()));
        mdate = myCalendar.getTime();
    }



    public void setPic(PickResult r){
        propic.setImageBitmap(null);

        mFileUri = r.getUri().toString();
        bitmap = r.getBitmap();
        propic.setImageBitmap(r.getBitmap());
    }

    public void buttonclick() {


        datedialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.HOUR, 12);
                myCalendar.set(Calendar.MINUTE, 0);

                updateLabel();
            }
        };

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addalltypes();

            }
        });


        bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, datedialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, datedialog, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickSetup setup = new PickSetup()
                        .setTitle("Choose Photo")
                        .setBackgroundColor(Color.WHITE)
                        .setButtonOrientation(LinearLayout.HORIZONTAL)
                        .setGalleryButtonText("Gallery")
                        .setCameraIcon(R.mipmap.camera_colored)
                        .setGalleryIcon(R.mipmap.gallery_colored)
                        .setCameraToPictures(false)
                        .setMaxSize(400);

                PickImageDialog.build(setup)
                        //.setOnClick(this)
                        .show(EditProfileActivity.this);

            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: yyyy");
                // progressBar.setVisibility(View.VISIBLE);

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                name.setError(null);
                // Store values at the time of the login attempt.
                final String iname = name.getText().toString();
                final String gentertext = gender.getText().toString();
                final String mailtext = usermail.getText().toString();
                final String nidtext=nid.getText().toString();


                boolean cancel = false;
                View focusView = null;


                if (TextUtils.isEmpty(iname)) {
                    name.setError(getString(R.string.error_field_required));
                    focusView = name;
                    cancel = true;

                }
                if (TextUtils.isEmpty(gentertext)) {
                    gender.setError(getString(R.string.error_field_required));
                    focusView = gender;
                    cancel = true;

                }
                if (TextUtils.isEmpty(mailtext)) {
                    usermail.setError(getString(R.string.error_field_required));
                    focusView = usermail;
                    cancel = true;

                }
                if (!normalfunc.isValidEmail(mailtext)) {
                    usermail.setError(getString(R.string.fui_invalid_email_address));
                    focusView = usermail;
                    cancel = true;
                }if (TextUtils.isEmpty(nidtext)) {
                    nid.setError(getString(R.string.error_field_required));
                    focusView = nid;
                    cancel = true;

                }




                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                    //progressBar.setVisibility(View.GONE);
                } else {
                    initdialog();
                    showdialog();
                    uploaddata();
                }


            }


        });

    }

    private void uploaddata() {

        photoRef = FirebaseStorage.getInstance().getReference()
                .child("users/" + userid + "/pic");

        String userphone= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().replace("+88","");

        List<String> ll= normalfunc.splitstring(name.getText().toString());
        ll.add(userphone);
        ll.add(usermail.getText().toString().toLowerCase());

        users2= new Users(name.getText().toString(),picurl,picurl,userid,mdate,users.getJoindate(),gender.getText().toString()
                ,usermail.getText().toString(),users.getPhone(),ll);

        WriteBatch batch = firebaseFirestore.batch();


        DocumentReference setinoffice = firebaseFirestore.collection(getString(R.string.col_users)).document(userid);

        batch.set(setinoffice, users2, SetOptions.merge());

        //TODO cloud function here

        if(!users2.equals(users))batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                //progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    if(bitmap==null){
                        dismissdialog();
                        Toast.makeText(context, "Data Update successful!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "Error connection!!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = photoRef.putBytes(data);
            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload succeeded

                            dismissdialog();
                            Intent intent= new Intent(context,ProfileActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(context, "Picture Update successful!!", Toast.LENGTH_SHORT).show();

                            // [END_EXCLUDE]
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception exception) {
                            // Upload failed
                            Log.w(TAG, "uploadFromUri:onFailure", exception);

                            // [END_EXCLUDE]
                        }
                    });
        }


    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //progressBar.setVisibility(View.VISIBLE);

            propic.setImageBitmap(null);

            mFileUri = r.getUri().toString();
            bitmap = r.getBitmap();
            propic.setImageBitmap(r.getBitmap());

        } else {
            Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_LONG).show();

        }

    }
}
