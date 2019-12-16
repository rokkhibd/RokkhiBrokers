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
import com.rokkhi.broker.Utils.Normalfunc;


import java.util.List;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.BuildingViewHolder> {

    List<PaymentHistory> paymentHistoryList;
    Context context;
    Normalfunc normalfunc;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public PaymentListAdapter(List<PaymentHistory> paymentHistoryList, Context context) {
        this.paymentHistoryList = paymentHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_layout, parent, false);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        return new BuildingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {

        PaymentHistory paymentHistory=paymentHistoryList.get(position);

        db.collection(context.getString(R.string.col_fPaymentType)).whereEqualTo("id",paymentHistory.getPayment_type()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot doc:task.getResult()){
                                String paymentType_id=doc.getString("id");

                                if (paymentType_id.equalsIgnoreCase("w8t5NYlrk68ebdllxdsC")){
                                    holder.paymentType.setText("meeting500");
                                }else if (paymentType_id.equalsIgnoreCase("4A8atzx7x2lUs37FcTKO")){
                                    holder.paymentType.setText("onetime500");
                                }else if (paymentType_id.equalsIgnoreCase("5PcOnjyk3uZthDGePKOz")){
                                    holder.paymentType.setText("bonus20");
                                }else if (paymentType_id.equalsIgnoreCase("7AY3TjCve45jWSiwNrOQ")){
                                    holder.paymentType.setText("meeting300");
                                }else if (paymentType_id.equalsIgnoreCase("7lE8iJsw2cnH6GiefOKr")){
                                    holder.paymentType.setText("referral");
                                }else if (paymentType_id.equalsIgnoreCase("Bck8TREle0EQIuJiyQVq")){
                                    holder.paymentType.setText("monthly");
                                }else if (paymentType_id.equalsIgnoreCase("kaOtHlMk44VT32cST5c6")){
                                    holder.paymentType.setText("bonus50");
                                }else if (paymentType_id.equalsIgnoreCase("pcbfkRdwKjbEkZ76jIjU")){
                                    holder.paymentType.setText("bonus100");
                                }else if (paymentType_id.equalsIgnoreCase("xtWIppHeLxXGTnVv3KGc")){
                                    holder.paymentType.setText("onetime700");
                                }

                            }
                        }
                    }
                });



       // holder.paymentType.setText(paymentHistoryList.get(position).getPayment_type());
        holder.payment_amount.setText(String.valueOf(paymentHistoryList.get(position).getAmount()));
        holder.payment_date.setText(normalfunc.getDatehhmmdMMMMyyyy(paymentHistoryList.get(position).getMonth()));
        holder.payment_status.setText(paymentHistoryList.get(position).getPayment_status());

    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }

    public class BuildingViewHolder extends RecyclerView.ViewHolder {

        TextView paymentType, payment_amount, payment_date, payment_status;

        public BuildingViewHolder(@NonNull final View itemView) {
            super(itemView);
            normalfunc = new Normalfunc();
            paymentType = itemView.findViewById(R.id.payment_type);
            payment_amount = itemView.findViewById(R.id.payment_amount);
            payment_date = itemView.findViewById(R.id.payment_date);
            payment_status = itemView.findViewById(R.id.payment_status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Click Item", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}
