package com.example.uidesign1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.logging.Logger;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;


public class chat_profile extends AppCompatActivity {

    private DatabaseReference RootRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private MaterialButton UpdateAccountSettings;
    private EditText userName, userStatus;
    private static final int GalleryPick = 1;
    private StorageReference UserProfileImagesRef;
    private ProgressDialog loadingBar;
    private Toolbar SettingsToolBar;
    private ImageView userProfileImage;

    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("profilepictuer");

        InitializeFields();
        userName.setVisibility(View.INVISIBLE);

        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSettings();
            }
        });

        RetrieveUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,IMAGE_REQUEST);
            }
        });
    }



    private void UpdateSettings()
    {
        String setUserName = userName.getText().toString();
        String setStatus = userStatus.getText().toString();
        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this, "Please write your user name first....", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setStatus))
        {
            Toast.makeText(this, "Please write your status....", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> profileMap = new HashMap<>();
            profileMap.put("uid",currentUserID);
            profileMap.put("username",setUserName);
            profileMap.put("bio",setStatus);

            RootRef.child("user").child(currentUserID).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(getApplicationContext(), "Profile Updated Successfully...", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = task.getException().toString();
                        Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void RetrieveUserInfo()
    {
        RootRef.child("user").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Intent intent=getIntent();
                user user = dataSnapshot.getValue(user.class);
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("username") && (dataSnapshot.hasChild("profilepictuer"))))
                {


                    String retrieveUserName=user.getUsername();
                    String retrieveStatus=user.getBio();

//                    String retrieveUserName = dataSnapshot.child("username").toString();
//                    String retrieveStatus = dataSnapshot.child("status").toString();
                    String retrieveProfilePhoto = dataSnapshot.child("profilepictuer").toString();

                    userName.setText(retrieveUserName);
                    userStatus.setText(retrieveStatus);
                    Glide.with(chat_profile.this).load(user.getProfilepictuer()).into(userProfileImage);


                    userName.setVisibility(View.VISIBLE);

                }
                else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("username")))
                {

                    String retrieveUserName=user.getUsername();
                    String retrievesStatus=user.getStatus();

//                    String retrieveUserName = dataSnapshot.child("username").getValue().toString();
//                    String retrievesStatus = dataSnapshot.child("status").getValue().toString();

                    userName.setText(retrieveUserName);
                    userStatus.setText(retrievesStatus);
                    userName.setVisibility(View.VISIBLE);
                }
                else
                {
                    userName.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Please set & update your profile information...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(getApplicationContext(), chatting.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void InitializeFields()
    {
        UpdateAccountSettings = findViewById(R.id.update_settings_button);
        userName = findViewById(R.id.set_user_name);
        userStatus = findViewById(R.id.set_profile_status);
        userProfileImage =findViewById(R.id.set_profile_image);

        loadingBar = new ProgressDialog(this);

        SettingsToolBar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(SettingsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Account Settings");
    }



    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()){
                    throw Objects.requireNonNull(task.getException());
                }

                return  fileReference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getApplicationContext(), "Upload in preogress", Toast.LENGTH_SHORT).show();
           } else {
                uploadImage();
           }
        }
    }




}