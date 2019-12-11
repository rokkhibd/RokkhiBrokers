package com.rokkhi.brokers.Model;

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
import com.rokkhi.brokers.R;

import java.util.List;

public class DueMeetingListAdapter extends RecyclerView.Adapter<DueMeetingListAdapter.MyViewHolder> {


    List<FWorkerBuilding> fBuildingsList;
    Context context;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public DueMeetingListAdapter(List<FWorkerBuilding> fBuildingsList, Context context) {
        this.fBuildingsList = fBuildingsList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_layout,parent,false);
        MyViewHolder mv=new MyViewHolder(v);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();


        return mv;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FWorkerBuilding fWorkerBuilding=fBuildingsList.get(position);

        //holder.name.setText(fBuildings.getHousename());
//
        db.collection(context.getString(R.string.col_fBuildings)).whereEqualTo("build_id",fWorkerBuilding.getBuild_id())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot doc:task.getResult()){
                        FBuildings fb=doc.toObject(FBuildings.class);


                        holder.name.setText(fb.getHousename());

                        if (fb.getStatus_id().equalsIgnoreCase("rUyWv6FLEgZ0EIB6aNNP")){

                            holder.status.setText("Meeting Pending");
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fBuildingsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.totalMeeting_bldngName);
            status=itemView.findViewById(R.id.totalMeeting_status);
        }
    }

}
