package com.rokkhi.brokers.Model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rokkhi.brokers.R;


import java.util.List;

public class ShowDueBuildingListAdapter extends RecyclerView.Adapter<ShowDueBuildingListAdapter.DueBuildingViewHolder> {

    Context context;
    List<PaymentBuildingShow> paymentBuildingShowsList;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FBuildings fBuildings;
    String userId,buildId,amount;

    public ShowDueBuildingListAdapter(Context context, List<PaymentBuildingShow> paymentBuildingShowsList) {
        this.context = context;
        this.paymentBuildingShowsList = paymentBuildingShowsList;
    }

    @NonNull
    @Override
    public DueBuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;




        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.due_building_layout, parent, false);
        DueBuildingViewHolder dueBuildingViewHolder=new DueBuildingViewHolder(v);

        db = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        return dueBuildingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DueBuildingViewHolder holder, int position) {

        PaymentBuildingShow paymentBuildingShow=paymentBuildingShowsList.get(position);


        db.collection(context.getString(R.string.col_paymentHistory)).whereEqualTo("f_uid",userId).whereEqualTo("payment_status","pending")
                .whereEqualTo("payment_type","w8t5NYlrk68ebdllxdsC").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot:task.getResult()){
                        buildId=documentSnapshot.getString("build_id");
                        amount=String.valueOf(documentSnapshot.get("amount"));

                        Log.e("TAG","build_id:"+buildId);
                        Log.e("TAG","amount:"+amount);

                        holder.amount.setText(amount);


                        db.collection(context.getString(R.string.col_fBuildings)).document(buildId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()){
                                    DocumentSnapshot doc=task.getResult();
                                    String housename=doc.getString("housename");
                                    holder.houseName.setText(housename);

                                }
                            }
                        });
                    }
                }else {
                    Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



       // holder.houseName.setText(paymentBuildingShow.getB_name());
       // holder.amount.setText(paymentBuildingShow.getAmount());




    }

    @Override
    public int getItemCount() {
        return paymentBuildingShowsList.size();
    }

    class DueBuildingViewHolder extends RecyclerView.ViewHolder {

        TextView houseName,amount;

        public DueBuildingViewHolder(@NonNull View itemView) {
            super(itemView);

            houseName=itemView.findViewById(R.id.duebldng_bldngName);
            amount=itemView.findViewById(R.id.duebldng_amount);
        }
    }

}
