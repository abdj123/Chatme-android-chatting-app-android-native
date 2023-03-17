package com.example.uidesign1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class messiging_activity extends AppCompatActivity {

    TextView messagetxtview;
    EditText messageeditText;
    RecyclerView messagerecycler;
    ImageView messageimg,sendicon,backbtn,emoji;
    ProgressBar progressBar;
    ArrayList<messageHold> messages;

    String username_of_firends,email_of_firends,chatroomId;

    private messageAdapter messageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messiging);


        messagetxtview=findViewById(R.id.messagetxtview);
        messageeditText=findViewById(R.id.messageeditText);
        messagerecycler=findViewById(R.id.messagerecycler);
        messageimg=findViewById(R.id.messageimg);
        sendicon=findViewById(R.id.sendicon);
        progressBar=findViewById(R.id.progressbar);
        backbtn=findViewById(R.id.backbtn);
        emoji=findViewById(R.id.emoji);
        username_of_firends=getIntent().getStringExtra("username_of_firends");
        email_of_firends=getIntent().getStringExtra("email_of_firends");


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(messiging_activity.this,chatting.class));
            }
        });

        messagetxtview.setText(username_of_firends);
        messages=new ArrayList<>();


        sendicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("messages/"+chatroomId).push().setValue(new messageHold(FirebaseAuth.getInstance().getCurrentUser().getEmail(),email_of_firends,messageeditText.getText().toString()));
                messageeditText.setText("");
            }
        });
        messageAdapter=new messageAdapter(messages,getIntent().getStringExtra("my_email"),getIntent().getStringExtra("img_of_firends"),messiging_activity.this);
        messagerecycler.setLayoutManager(new LinearLayoutManager(this));
        messagerecycler.setAdapter(messageAdapter);

        Glide.with(messiging_activity.this).load(getIntent().getStringExtra("img_of_firends")).placeholder(R.drawable.defult_profile).error(R.drawable.defult_profile).into(messageimg);

        setchatRoom();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference ab=FirebaseDatabase.getInstance().getReference("messages");
        ab.keepSynced(true);
    }

    private void setchatRoom(){
        FirebaseDatabase.getInstance().getReference("user/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myuserName=snapshot.getValue(user.class).getUsername();
                if(username_of_firends.compareTo(myuserName)>0){
                    chatroomId=myuserName+username_of_firends;
                }else if(username_of_firends.compareTo(myuserName)==0){
                    chatroomId=myuserName+username_of_firends;
                }else {
                    chatroomId=username_of_firends+myuserName;
                }
                messageattach(chatroomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void messageattach(String chatroomId){
        FirebaseDatabase.getInstance().getReference("messages/"+chatroomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messages.add(dataSnapshot.getValue(messageHold.class));
                }
                messageAdapter.notifyDataSetChanged();
                messagerecycler.scrollToPosition(messages.size()-1);
                messagerecycler.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}