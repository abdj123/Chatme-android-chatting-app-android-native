package com.example.uidesign1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class signup extends AppCompatActivity {
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";
    FloatingActionButton signbtn;
    EditText name,phone,email,password;
    String nametxt,phonetxt,emailtxt,passwordtxt,mytxt;

    public ArrayList<String> ab=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floatingbutton);

        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);

        signbtn= findViewById(R.id.signbtn);
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ProgressDialog load=new ProgressDialog(signup.this);
                        load.setMessage("Loading...");
                        load.setProgress(10);
                        load.setMax(100);


                        if(task.isSuccessful()){
                            FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new user(email.getText().toString(),name.getText().toString(),"",""));
                            load.show();
                            startActivity(new Intent(signup.this,chatting.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("userEmail", (Parcelable) email));
                            Toast.makeText(signup.this,"Sign Up is Successful",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            load.show();
                            Toast.makeText(signup.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                        load.dismiss();
                    }
                });

            }
        });

    }

}