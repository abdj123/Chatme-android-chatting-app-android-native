package com.example.uidesign1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{ //implements View.OnClickListener{
    MaterialButton login,signup;
    EditText email,password;
    String emailer,passer;

    private final String pref="our_shared_pref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup s=new signup();

        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,ui_part.class));
        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog load=new ProgressDialog(MainActivity.this);
                load.setMessage("Loading...");
                load.setProgress(10);
                load.setMax(100);
                load.show();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Log In Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,ui_part.class));
                        }
                        else{
                            Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                        load.dismiss();
                    }
                });
            }


        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,signup.class));


            }
        });
    }
}