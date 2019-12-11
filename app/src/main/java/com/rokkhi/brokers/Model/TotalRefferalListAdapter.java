package com.rokkhi.brokers.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rokkhi.brokers.R;
import com.rokkhi.brokers.Utils.Normalfunc;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TotalRefferalListAdapter extends RecyclerView.Adapter<TotalRefferalListAdapter.MyViewHolder> {

    List<Users> usersList;
    Context context;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String user_phone;
    String user_id;
    Normalfunc normalfunc;

    public TotalRefferalListAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.total_referral_layout,parent,false);
        MyViewHolder mv=new MyViewHolder(v);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user_phone=mAuth.getCurrentUser().getPhoneNumber();
        normalfunc=new Normalfunc();

        return mv;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Users users=usersList.get(position);

        holder.ref_mail.setText(users.getMail());
        holder.ref_name.setText(users.getName());
        holder.ref_phone.setText(normalfunc.makephone11(users.getPhone()));
        Glide.with(context).load(users.getThumb()).placeholder(R.drawable.profile2).into(holder.ref_image);




    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView ref_image;
        TextView ref_name,ref_mail,ref_phone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ref_image=itemView.findViewById(R.id.ref_users_pic);
            ref_name=itemView.findViewById(R.id.ref_users_name);
            ref_mail=itemView.findViewById(R.id.ref_users_mail);
            ref_phone=itemView.findViewById(R.id.ref_users_phone);

        }
    }
}
