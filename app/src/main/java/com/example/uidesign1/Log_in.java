package com.example.uidesign1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Log_in extends AppCompatActivity {
    MaterialButton login;
    EditText email,password;
    TextView create;
    CheckBox rememberme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        login=findViewById(R.id.btnlogin);
        create=findViewById(R.id.createaccount);
        email=findViewById(R.id.loginemail);
        password=findViewById(R.id.loginpassword);
        rememberme=findViewById(R.id.rememberme);

        rememberme.setChecked(true);
            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                if(rememberme.isChecked()){
                    startActivity(new Intent(Log_in.this,chatting.class));
            }
        }





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog load=new ProgressDialog(Log_in.this);
                load.setMessage("Loading...");
                load.setProgress(10);
                load.setMax(100);
                load.show();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Log_in.this,"Log In Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Log_in.this,chatting.class));
                        }
                        else{
                            Toast.makeText(Log_in.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                        load.dismiss();
                    }
                });
            }


        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Log_in.this,signup.class));


            }
        });
    }

}
