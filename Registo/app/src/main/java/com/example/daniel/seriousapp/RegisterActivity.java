package com.example.daniel.seriousapp;

import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressDialog PD;
    private EditText Name;
    private EditText Pass;
    private Button Register;
    private Button Signin;
    private static final String nome_do_user = "nome_do_user";
    private static final String pass_do_user = "pass_do_user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(RegisterActivity.this, MenuPrincipal.class);
                startActivity(intent);
                finish();
            }
        };

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        Name = (EditText) findViewById(R.id.email);
        Pass = (EditText) findViewById(R.id.password);
        Register = (Button) findViewById(R.id.sign_up_button);
        Signin = findViewById(R.id.Signin);

        Signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    startActivity(new Intent(RegisterActivity.this, SigninActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //acao do botao REGISTER
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Log.d(nome_do_user, Name.getText().toString());
                    Log.d(pass_do_user, Pass.getText().toString());
                    Log.d("existe", "password");

                    CallDatabase(Name.getText().toString(), Pass.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void CallDatabase(final String username, String password) {

        //password restrictions

        if (password.length() > 0 && username.length() > 0) {
            PD.show();
            String domain = "@serious.com";
            String email = username + domain;

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        }
                        // if user enters wrong email.
                        catch (FirebaseAuthWeakPasswordException weakPassword) {
                            Toast.makeText(RegisterActivity.this, "onComplete: weak_password", Toast.LENGTH_LONG).show();

                            // TODO: take your actions!
                        }
                        // if user enters wrong password.
                        catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                            Toast.makeText(RegisterActivity.this, "onComplete: malformed_email", Toast.LENGTH_LONG).show();

                            // TODO: Take your action
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            Toast.makeText(RegisterActivity.this, "onComplete: exist_email", Toast.LENGTH_LONG).show();


                            // TODO: Take your action
                        } catch (Exception e) {
                            Toast.makeText(RegisterActivity.this, "onComplete: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

//                        Toast.makeText(
//                                RegisterActivity.this,
//                                "Authentication Failed",
//                                Toast.LENGTH_LONG).show();
//                        Log.v("error in password", task.getResult().toString());
                    } else {
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                        //makes sure every info being saved is saved at the same time
                        Map newPost = new HashMap();
                        newPost.put("name",username);
                        newPost.put("longitude",0f);
                        newPost.put("latitude",0f);
                        newPost.put("card_number",0);
                        newPost.put("age",0);
                        newPost.put("clinical_background"," ");
                        newPost.put("CFH_attendance",0);
                        newPost.put("real_job"," ");

                        database.setValue(newPost);
                        Intent intent = new Intent(RegisterActivity.this, MenuPrincipal.class);
                        startActivity(intent);
                        finish();
                    }
                    PD.dismiss();
                }
            });

        } else {
            Toast.makeText(
                    RegisterActivity.this, "Fill All fields", Toast.LENGTH_LONG).show();
        }


    }


}