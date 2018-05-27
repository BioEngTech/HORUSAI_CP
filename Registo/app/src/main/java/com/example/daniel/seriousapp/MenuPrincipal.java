package com.example.daniel.seriousapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MenuPrincipal extends AppCompatActivity {

    private Button GoBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);


        GoBack = findViewById(R.id.Signout);


        GoBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                try {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(MenuPrincipal.this, RegisterActivity.class));
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
