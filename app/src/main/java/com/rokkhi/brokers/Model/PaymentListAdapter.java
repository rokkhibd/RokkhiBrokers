package com.rokkhi.brokers.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Utils.Normalfunc;


import java.util.List;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.BuildingViewHolder> {

    List<PaymentHistory> paymentHistoryList;
    Context context;
    Normalfunc normalfunc;

    public PaymentListAdapter(List<PaymentHistory> paymentHistoryList, Context context) {
        this.paymentHistoryList = paymentHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_layout, parent, false);

        return new BuildingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {

        holder.paymentType.setText(paymentHistoryList.get(position).getPayment_type());
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
