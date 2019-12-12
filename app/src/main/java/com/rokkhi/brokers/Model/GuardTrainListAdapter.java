package com.rokkhi.brokers.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Utils.Normalfunc;
import com.shashank.sony.fancytoastlib.FancyToast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mumayank.com.airlocationlibrary.AirLocation;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GuardTrainListAdapter extends RecyclerView.Adapter<GuardTrainListAdapter.GuardTrainViewHolder> {

    Context context;
    List<FGuardTrack> fGuardTrackList;
    View v;
    FirebaseFirestore firebaseFirestore;
    FGuardTrack fGuardTrack;
    FirebaseAuth mAuth;
    Normalfunc normalfunc;

    FusedLocationProviderClient client;

    Double endlate,endlang;

    private AirLocation airLocation;


    public GuardTrainListAdapter(Context context, List<FGuardTrack> fGuardTrackList) {
        this.context = context;
        this.fGuardTrackList = fGuardTrackList;
    }

    @NonNull
    @Override
    public GuardTrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guardtrack_list,parent,false);
        GuardTrainViewHolder bv=new GuardTrainViewHolder(v);
        firebaseFirestore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        client = LocationServices.getFusedLocationProviderClient(context);


        airLocation = new AirLocation((Activity) context, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {
                //  lat = location.getLatitude();
                // lng = location.getLongitude();
            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });

        return bv;
    }

    @Override
    public void onBindViewHolder(@NonNull GuardTrainViewHolder holder, int position) {

        String user_id=mAuth.getCurrentUser().getUid();

        FGuardTrack fGuardTrack=fGuardTrackList.get(position);

        Date date1=fGuardTrack.getTimeStart();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);

        String myFormat = "MMM d , hh:mm a "; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        holder.startButton.setText(sdf.format(cal.getTime()));


        /*Date date2=fGuardTrack.getTimeEnd();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);*/

        String myFormat2 = "MMM d , hh:mm a "; //In which you need put here
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);

       // holder.endButton.setText(sdf2.format(cal2.getTime()));


        firebaseFirestore.collection(context.getString(R.string.col_guards)).document(fGuardTrack.getGuard_id()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot=task.getResult();
                            if (documentSnapshot!=null && documentSnapshot.exists()){
                                String name=documentSnapshot.getString("g_name");
                                String imageUrl=documentSnapshot.getString("g_pic");
                                holder.gName.setText(name);
                                Glide.with(context).load(imageUrl).error(R.drawable.error_icon).into(holder.guardImage);

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        firebaseFirestore.collection(context.getString(R.string.col_GuardTrainerTrack)).document(fGuardTrack.getDoc_id()).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();

                    if (documentSnapshot!=null && documentSnapshot.exists()){


                        if (documentSnapshot.getData().get("timeEnd")!=null){

                            Timestamp timestamp = (Timestamp) documentSnapshot.getData().get("timeEnd");
                            Date date=timestamp.toDate();
                            holder.endButton.setText(normalfunc.getDateMMMddhhmma(date));

                        }else {
                           // Toast.makeText(context, "No field found", Toast.LENGTH_SHORT).show();
                        }



                    }

                }
            }
        });


        firebaseFirestore.collection(context.getString(R.string.col_buildings)).document(fGuardTrack.getBuild_id()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot=task.getResult();
                            if (documentSnapshot!=null && documentSnapshot.exists()){
                                String b_name=documentSnapshot.getString("b_name");

                                holder.gHouse.setText(b_name);

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return fGuardTrackList.size();
    }

    public class GuardTrainViewHolder extends RecyclerView.ViewHolder {

        ImageView guardImage;
        TextView gName,gHouse;
        Button startButton,endButton;

        public GuardTrainViewHolder(@NonNull View itemView) {
            super(itemView);
            normalfunc = new Normalfunc();
            guardImage=itemView.findViewById(R.id.propic);
            gName=itemView.findViewById(R.id.gname);
            startButton=itemView.findViewById(R.id.gtrain_start);
            endButton=itemView.findViewById(R.id.gtrain_end);
            gHouse=itemView.findViewById(R.id.gHouse);

            endButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEndPermissionDialog(endButton);
                }
            });
//            itemView.setOnClickListener(this);


        }

    }

    private void showEndPermissionDialog(Button endButton) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.end_training_layout, null);

        Button btn_yes = view.findViewById(R.id.btn_yes);
        Button btn_no=view.findViewById(R.id.btn_no);
        alert.setView(view);



        final AlertDialog alertDialog1 = alert.create();
        alertDialog1.setCanceledOnTouchOutside(true);
        alertDialog1.show();

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTheLatLang();

                FirebaseUser firebaseUser=mAuth.getCurrentUser();
                String userId=firebaseUser.getUid();

                firebaseFirestore.collection(context.getString(R.string.col_GuardTrainerTrack)).whereEqualTo("user_id",userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            String id=" ";

                            for (DocumentSnapshot doc:task.getResult()){
                                FGuardTrack fGuardTrack=doc.toObject(FGuardTrack.class);

                                fGuardTrack.setDoc_id(doc.getId());
                                id=fGuardTrack.getDoc_id();



                                Date date;
                                date = Calendar.getInstance().getTime();

                                String myFormat2 = "MMM d , hh:mm a "; //In which you need put here
                                SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
//                                holder.endButton.setText(sdf2.format(cal2.getTime()));

                                endButton.setText(sdf2.format(date));


                                DocumentReference docRef=firebaseFirestore.collection(context.getString(R.string.col_GuardTrainerTrack)).document(id);


                                Map<String,Object> map=new HashMap<>();
                                map.put("timeEnd",date);
                                map.put("endlatitude",endlate);
                                map.put("endlongitude",endlang);

                                docRef.set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                           // Toast.makeText(context, "Your training period is End!!", Toast.LENGTH_SHORT).show();

                                            FancyToast.makeText(context,"Your training period is End",FancyToast.LENGTH_LONG, FancyToast.DEFAULT,false).show();

                                        }
                                    }
                                });

                            }

                            alertDialog1.dismiss();
                        }
                    }
                });
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
    }

    public void getTheLatLang(){
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    endlate = location.getLatitude();
                    endlang = location.getLongitude();


                }

            }
        });
    }
}
