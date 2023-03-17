package com.example.uidesign1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class firendsHold extends RecyclerView.Adapter<firendsHold.usersHold> {

    ArrayList<user> User;
    Context context;
    onUserClickListner onUserClickListner;

    DatabaseReference UsersRef;

    public firendsHold(ArrayList<user> User, Context context, firendsHold.onUserClickListner onUserClickListner) {
        this.User = User;
        this.context = context;
        this.onUserClickListner = onUserClickListner;
    }

    interface onUserClickListner{
        void onUserClicked(int position);
    }

    @NonNull
    @Override
    public usersHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.users_hold,parent,false);
        //UsersRef = FirebaseDatabase.getInstance().getReference().child("user");
        return new usersHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull usersHold holder, int position) {
        holder.profilename.setText(User.get(position).getUsername());
        Glide.with(context).load(User.get(position).getProfilepictuer()).error(R.drawable.defult_profile).placeholder(R.drawable.defult_profile).into(holder.profileimg);

        if (User.get(position).getStatus()=="online"){
            holder.onlineIcon.setVisibility(View.VISIBLE);
            //holder.img_off.setVisibility(View.GONE);
        } else {
            holder.onlineIcon.setImageResource(R.drawable.offline);
            holder.onlineIcon.setVisibility(View.VISIBLE);
            //holder.img_off.setVisibility(View.VISIBLE);
        }

        //holder.userState.setText(User.get(position).getStatus());

        //String userIDs = FirebaseAuth.getInstance().getUid();
    }
    @Override
    public int getItemCount() {
        return User.size();
    }

    public class usersHold extends RecyclerView.ViewHolder{

        ImageView profileimg,onlineIcon;
        TextView profilename,userState;

        ImageView drawpicture;
        TextView drawusername;

        public usersHold(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListner.onUserClicked(getAdapterPosition());
                }
            });

            profileimg=itemView.findViewById(R.id.profileimg);
            onlineIcon=itemView.findViewById(R.id.user_online_status);
            profilename=itemView.findViewById(R.id.profilename);
            userState=itemView.findViewById(R.id.user_status);

            drawusername=itemView.findViewById(R.id.drawusername);
            drawpicture=itemView.findViewById(R.id.drawerpic);
        }
    }
}


