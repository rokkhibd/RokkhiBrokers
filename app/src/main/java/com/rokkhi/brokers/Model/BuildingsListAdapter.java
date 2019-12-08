package com.rokkhi.brokers.Model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Ui.AddBuildingActivity;
import com.rokkhi.brokers.Ui.UpdateBldngInfoActivity;
import com.rokkhi.brokers.Utils.Normalfunc;


import java.util.List;

public class BuildingsListAdapter extends RecyclerView.Adapter<BuildingsListAdapter.BuildingViewholder> {

    public Context context;
    public List<FWorkerBuilding> fBuildingsList;
    FirebaseFirestore firebaseFirestore;
    FBPeople fbPeople = new FBPeople();


    public BuildingsListAdapter(List<FWorkerBuilding> fBuildingsList, Context context) {

        this.fBuildingsList = fBuildingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingViewholder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View v;

        firebaseFirestore = FirebaseFirestore.getInstance();

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_layout, parent, false);
        final BuildingViewholder bv = new BuildingViewholder(v);

        //TODO:Dialog initialize

        final Dialog dialog = new Dialog(parent.getContext());
        dialog.setContentView(R.layout.show_buildinginfo_layout);

        return bv;
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewholder holder, int position) {

        FWorkerBuilding fBuildings = fBuildingsList.get(position);
        firebaseFirestore.collection(context.getString(R.string.col_fBuildings))
                .document(fBuildings.getBuild_id()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {

                        FBuildings fb = documentSnapshot.toObject(FBuildings.class);

                        //holder.item_list_id.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_anim));

                        holder.build_address.setText(fb.getB_address());
                        holder.build_name.setText(fb.getHousename());

                        if (fb.getStatus_id().equalsIgnoreCase("zD0cviZ6Zab3GbWYu7tA")){
                            holder.build_status.setText("Followup");
                        }if (fb.getStatus_id().equalsIgnoreCase("rUyWv6FLEgZ0EIB6aNNP")){
                            holder.build_status.setText("Meeting Pending");
                        }if (fb.getStatus_id().equalsIgnoreCase("MWI1MTIe8Xv3Ls8Asa2X")){
                            holder.build_status.setText("Cancelled");
                        }if (fb.getStatus_id().equalsIgnoreCase("cQ7jmazM2pAoMWtL213L")){
                            holder.build_status.setText("Meeting rejected");
                        }if (fb.getStatus_id().equalsIgnoreCase("lACNetniNe4gjp6nBvWP")){
                            holder.build_status.setText("Building Active");
                        }
                        /*if (fb.getStatus_id().equalsIgnoreCase("lACNetniNe4gjp6nBvWP")){
                            holder.build_status.setText("Building Active");
                        }*/

                        //holder.build_status.setText(fb.getStatus_id());


                        holder.build_lastVisit.setText(Normalfunc.convertDate(fb.getCreated_at()));
                    }
                }
            }
        });


    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return fBuildingsList.size();
    }

    public class BuildingViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView build_name, build_address, build_status, build_lastVisit, building_contacts;
        CardView item_list_id;

        public BuildingViewholder(@NonNull final View itemView) {
            super(itemView);

            build_name = itemView.findViewById(R.id.myhome_frag_bldngName);
            build_address = itemView.findViewById(R.id.myhome_frag_bldngAddress);
            build_status = itemView.findViewById(R.id.myhome_frag_bldngstatus);
            build_lastVisit = itemView.findViewById(R.id.myhome_frag_bldngvisitdate);
            building_contacts = itemView.findViewById(R.id.bldng_contacts);

            item_list_id = itemView.findViewById(R.id.item_list_id);

            building_contacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FWorkerBuilding fBuildings = fBuildingsList.get(getAdapterPosition());

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.building_contacts_layout, null);

                    ProgressBar progress=view.findViewById(R.id.progress);
                    LinearLayout lin1=view.findViewById(R.id.lin1);
                    LinearLayout lin2=view.findViewById(R.id.lin2);
                    LinearLayout lin3=view.findViewById(R.id.lin3);
                    LinearLayout lin4=view.findViewById(R.id.lin4);
                    LinearLayout lin5=view.findViewById(R.id.lin5);
                    LinearLayout lin6=view.findViewById(R.id.lin6);


                    TextView nodatatxt=view.findViewById(R.id.notdatatxt);

                    TextView managerName = view.findViewById(R.id.bpeople_name_manager);
                    TextView managerDesig = view.findViewById(R.id.bpeople_desig_manager);
                    TextView managerNumber = view.findViewById(R.id.bpeople_phone_manager);

                    ImageView managerImg = view.findViewById(R.id.manager_call_img);
                    ImageView guardImg = view.findViewById(R.id.guard_call_img);
                    ImageView ownerImg=view.findViewById(R.id.owner_call_img);

                    TextView guardName = view.findViewById(R.id.bpeople_name_guard);
                    TextView guardDesig = view.findViewById(R.id.bpeople_desig_guard);
                    TextView guardNumber = view.findViewById(R.id.bpeople_phone_guard);


                    TextView ownerName = view.findViewById(R.id.bpeople_name_owner);
                    TextView ownerDesig = view.findViewById(R.id.bpeople_desig_owner);
                    TextView ownerNumber = view.findViewById(R.id.bpeople_phone_owner);


                    firebaseFirestore.collection(context.getString(R.string.col_fBbuildingContacts)).whereEqualTo("b_code", fBuildings.getB_code()).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                    if (task.isSuccessful()) {

                                        progress.setVisibility(View.GONE);

                                        for (DocumentSnapshot doc : task.getResult()) {
                                            FBPeople fbPeople = doc.toObject(FBPeople.class);
                                            String designation = fbPeople.getDesignation();

                                            if (designation.equalsIgnoreCase("Manager")) {
                                                managerDesig.setText("Manager");
                                                managerName.setText(fbPeople.getName());
                                                managerNumber.setText(fbPeople.getNumber());

                                                managerImg.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        String number = fbPeople.getNumber();
                                                            /*Intent intent=new Intent(Intent.ACTION_CALL);
                                                            intent.setData(Uri.parse(number));
                                                            context.startActivity(intent);*/


                                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                                                                context.startActivity(intent);
                                                                return;
                                                            }else {
                                                                requestPermission(context);
                                                            }
                                                        }


                                                                                                            }
                                                });
                                            }else if (designation.equalsIgnoreCase("Guard")){
                                                guardDesig.setText("Guard");
                                                guardName.setText(fbPeople.getName());
                                                guardNumber.setText(fbPeople.getNumber());


                                                guardImg.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String number = fbPeople.getNumber();

                                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                                                                context.startActivity(intent);
                                                                return;
                                                            }else {
                                                                requestPermission(context);
                                                            }
                                                        }
                                                    }
                                                });
                                            }else if (designation.equalsIgnoreCase("Owner")){
                                                ownerDesig.setText("Owner");
                                                ownerName.setText(fbPeople.getName());
                                                ownerNumber.setText(fbPeople.getNumber());


                                                ownerImg.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String number = fbPeople.getNumber();

                                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                                                                context.startActivity(intent);
                                                                return;
                                                            }else {
                                                                requestPermission(context);
                                                            }
                                                        }
                                                    }
                                                });
                                            }

                                            else {
                                                progress.setVisibility(View.GONE);
                                                lin1.setVisibility(View.GONE);
                                                lin2.setVisibility(View.GONE);
                                                lin3.setVisibility(View.GONE);
                                                lin4.setVisibility(View.GONE);
                                                lin5.setVisibility(View.GONE);
                                                lin6.setVisibility(View.GONE);

                                                nodatatxt.setVisibility(View.VISIBLE);

                                            }
                                        }

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.setVisibility(View.GONE);
                            nodatatxt.setVisibility(View.VISIBLE);
                        }
                    });

                    alert.setView(view);
                    final AlertDialog alertDialog1 = alert.create();
                    alertDialog1.setCanceledOnTouchOutside(true);
                    alertDialog1.show();



                }
            });


            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            FWorkerBuilding fBuildings=fBuildingsList.get(getAdapterPosition());
            Intent intent=new Intent(v.getContext(), UpdateBldngInfoActivity.class);
            intent.putExtra("buildingID", fBuildings.getBuild_id());
            intent.putExtra("doc_id",fBuildings.getDoc_id());
            v.getContext().startActivity(intent);
            

        }
    }

    private void requestPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},1);
    }
}
