package com.rokkhi.broker.Ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rokkhi.broker.Model.FNotification;
import com.rokkhi.broker.R;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NotificationViewHolder> {

    List<FNotification> fNotificationList;
    private Context context;
    FirebaseFirestore firebaseFirestore;


    public NoticeAdapter(List<FNotification> fNotificationList, Context context) {
        this.fNotificationList = fNotificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        NotificationViewHolder notificationViewHolder=new NotificationViewHolder(view);
        firebaseFirestore= FirebaseFirestore.getInstance();
        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        final FNotification notifications = fNotificationList.get(position);
        holder.ntype.setText(notifications.getN_type());
        holder.nbody.setText(notifications.getN_body());
        holder.ntittle.setText(notifications.getN_tittle());
        Date date1=notifications.getN_time();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);

        String myFormat = "EEE, MMM d , hh:mm a "; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        holder.ntime.setText(sdf.format(cal.getTime()));


       if (!notifications.getN_sender().isEmpty())firebaseFirestore.
               collection(context.getString(R.string.col_fNotification)).document(notifications.getN_id()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot!=null && documentSnapshot.exists()){
                        String n_picUrl=documentSnapshot.getString("n_pic");

                        if (!n_picUrl.isEmpty() && !n_picUrl.equals("none")){
                            Glide.with(context).load(n_picUrl).into(holder.propic);
                        }else {
                            Toast.makeText(context, "No pic found..", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return fNotificationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  position;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        public View view;
        TextView ntype;
        TextView nbody ;
        TextView ntime;
        CircleImageView propic;
        TextView ntittle;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            ntype = view.findViewById(R.id.ntype);
            nbody = view.findViewById(R.id.nbody);
            propic=view.findViewById(R.id.propic);
            ntime=view.findViewById(R.id.ntime);
            ntittle= view.findViewById(R.id.ntittle);

        }
    }


}
