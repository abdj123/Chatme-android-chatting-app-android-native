package com.example.uidesign1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ui_part extends AppCompatActivity {
    CardView cardchat;
    ImageView pic,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_part);

        cardchat=findViewById(R.id.cardchat);
        pic=findViewById(R.id.uipic);
        exit=findViewById(R.id.exit);

        cardchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ui_part.this,chatting.class));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                System.exit(1);
                finish();
            }
        });

//        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                user user = dataSnapshot.getValue(user.class);
//                //usernameS.setText(user.getUsername());
//                if (user.getProfilepictuer().equals("default")){
//                    pic.setImageResource(R.drawable.defult_profile);
//                } else {
//                    Glide.with(ui_part.this).load(user.getProfilepictuer()).into(pic);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//




    }
}