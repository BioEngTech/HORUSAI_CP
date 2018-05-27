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
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.NonNull;
import android.app.ProgressDialog;
import android.widget.Toast;


public class SigninActivity extends AppCompatActivity implements ValueEventListener {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog PD;
    private EditText Name;
    private EditText Pass;
    private Button Signin;
    private Button Register;
    private static final String nome_do_user = "nome_do_user_register";
    private static final String nome_do_user2 = "nome_do_user_signin";

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
        Signin = findViewById(R.id.action_sign_in_short);
        Register = findViewById(R.id.register);


        Register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Log.d( nome_do_user,Name.getText().toString());

                    startActivity(new Intent(SigninActivity.this, RegisterActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //acao do botao REGISTER
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = Pass.getText().toString();
                String username = Name.getText().toString();

                try {
                    Log.d( nome_do_user2,Name.getText().toString());

                    if (password.length() > 0 && username.length() > 0) {
                        PD.show();
                        String domain = "@serious.com";
                        String email = username + domain;
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        Toast.makeText(SigninActivity.this, "Invalid Emaild Id", Toast.LENGTH_LONG).show();

                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Toast.makeText(SigninActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();

                                    } catch (FirebaseNetworkException e) {
                                        Toast.makeText(SigninActivity.this, "error_message_failed_sign_in_no_network", Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                        Toast.makeText(SigninActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                } else {
                                    Intent intent = new Intent(SigninActivity.this, MenuPrincipal.class);
                                    startActivity(intent);
                                    finish();
                                }
                                PD.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(
                                SigninActivity.this, "Fill All fields", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
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


}
