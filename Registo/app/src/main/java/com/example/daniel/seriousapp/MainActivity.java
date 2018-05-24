package com.example.daniel.seriousapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.support.annotation.NonNull;
import android.app.ProgressDialog;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements ValueEventListener{
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog PD;
    private EditText Name;
    private EditText Pass;
    private Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        Name = findViewById(R.id.user);
        Pass = findViewById(R.id.pass);
        Register = findViewById(R.id.action_sign_in_short);


        //acao do botao REGISTER
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = Pass.getText().toString();
                String username= Name.getText().toString();

                try {

                    if (password.length()>0 && username.length()>0){
                        PD.show();
                        String domain = "@serious.com";
                        String email= username+domain;
                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG).show();
                                    Log.v("error", task.getResult().toString());
                                } else {
                                    Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                                    startActivity(intent);
                                    finish();
                                }
                                PD.dismiss();
                            }
                        });
                    }else{
                        Toast.makeText(
                                MainActivity.this,"Fill All fields",Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                }
        });
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {


    }
    @Override
    protected void onResume() {
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, MenuPrincipal.class));
            finish();
        }
        super.onResume();
    }


}
