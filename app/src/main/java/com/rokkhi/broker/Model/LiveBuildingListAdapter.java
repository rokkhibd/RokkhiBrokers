package com.rokkhi.broker.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.broker.R;


import java.util.List;

public class LiveBuildingListAdapter extends RecyclerView.Adapter< LiveBuildingListAdapter.MyViewHolder> {

    List<FWorkerBuilding> fWorkerBuildingList;
    Context context;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    public LiveBuildingListAdapter(List<FWorkerBuilding> fWorkerBuildingList, Context context) {
        this.fWorkerBuildingList = fWorkerBuildingList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_layout, parent, false);

        MyViewHolder mv=new MyViewHolder(v);

        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        return mv;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String userID=mAuth.getCurrentUser().getUid();

        FWorkerBuilding fworkerBuilding=fWorkerBuildingList.get(position);

        db.collection(context.getString(R.string.col_fWorkerBuilding)).whereEqualTo("f_uid",userID).whereEqualTo("status_id","lACNetniNe4gjp6nBvWP")
                .whereLessThan("totalMonth",24).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (DocumentSnapshot doc:task.getResult()){
                        FWorkerBuilding fWorkerBuilding=doc.toObject(FWorkerBuilding.class);

                        String build_id=fworkerBuilding.getBuild_id();

                        if (fworkerBuilding.getStatus_id().toString().equalsIgnoreCase("lACNetniNe4gjp6nBvWP")){
                            holder.status.setText("Building Active");
                        }

                        db.collection(context.getString(R.string.col_fBuildings)).whereEqualTo("build_id",build_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (DocumentSnapshot doc:task.getResult()){
                                        FBuildings fb=doc.toObject(FBuildings.class);
                                        holder.name.setText(fb.getHousename());
                                    }
                                }
                            }
                        });


                    }
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return fWorkerBuildingList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.livebldng_bldngName);
            status=itemView.findViewById(R.id.livebldng_status);
        }
    }
}
