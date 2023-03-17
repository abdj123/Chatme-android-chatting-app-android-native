package com.example.uidesign1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.zip.Inflater;

public class chatting extends AppCompatActivity {

    firendsHold firendsHold;
    ArrayList<user> User;
    RecyclerView recyclerView;
    firendsHold.onUserClickListner onUserClickListner;
    ProgressBar progressBar;
    String myimgUrl;
    SwipeRefreshLayout swipper;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    DatabaseReference RootRef;
    String currentUserID;


    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    FirebaseUser firebaseUser;
    //DatabaseReference reference;

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (currentUser != null) {
//            updateUserStatus("online");
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (currentUser != null)
//        {
//            updateUserStatus("offline");
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (currentUser != null)
//        {
//            updateUserStatus("offline");
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);



        drawerLayout=findViewById(R.id.drawemain);
        navigationView=findViewById(R.id.navigator);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_Open,R.string.close_menu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        swipper=findViewById(R.id.refresher);
//
//        swipper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getusers();
//                swipper.setRefreshing(false);
//            }
//        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Glide.with(getApplicationContext()).load(User.get(position).getProfilepictuer()).error(R.drawable.defult_profile).placeholder(R.drawable.defult_profile).into(holder.profileimg);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(chatting.this,Log_in.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(chatting.this,chat_profile.class));
                        break;
                    case R.id.exit:
                        finish();
                        System.exit(0);
                        break;

                }



                return true;
            }
        });

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        User=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler);
        progressBar=findViewById(R.id.prgg);
        onUserClickListner=new firendsHold.onUserClickListner() {
            @Override
            public void onUserClicked(int position) {
                startActivity(new Intent(chatting.this,messiging_activity.class)
                        .putExtra("username_of_firends",User.get(position).getUsername())
                        .putExtra("img_of_firends",User.get(position).getProfilepictuer())
                        .putExtra("email_of_firends",User.get(position).getEmail())
                        .putExtra("my_email",myimgUrl));
            }
        };
        getusers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private  void getusers(){
        //User.clear();
        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User.add(dataSnapshot.getValue(user.class));
                }
                firendsHold=new firendsHold(User,chatting.this,onUserClickListner);
                recyclerView.setLayoutManager(new LinearLayoutManager(chatting.this));
                recyclerView.setAdapter(firendsHold);
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                for(user Us:User){
                    if(Us.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        myimgUrl=Us.getProfilepictuer();
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUserStatus(String state)
    {
        currentUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("state", state);

        RootRef.child("user").child(currentUserID).child("userState")
                .updateChildren(onlineStateMap);

    }
    private void status(String status) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RootRef = FirebaseDatabase.getInstance().getReference( "user" ).child( firebaseUser.getUid() );

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put( "status", status );

        RootRef.updateChildren( hashMap );
    }

    @Override
    protected void onResume() {
        super.onResume();
        status( "online" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        status( "offline" );
    }
}
