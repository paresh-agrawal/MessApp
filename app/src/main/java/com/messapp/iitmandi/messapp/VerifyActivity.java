package com.messapp.iitmandi.messapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by root on 3/5/17.
 */

public class VerifyActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button verify_button,log_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        verify_button = (Button)findViewById(R.id.verify_button);
        log_in = (Button)findViewById(R.id.sign_in_button);
        Log.d("user Emial",user.getEmail().toString());
        if(user.getEmail().toString().equals("iitmandimessapp@gmail.com")){
            Intent intent = new Intent(VerifyActivity.this, AdminActivity.class);
            startActivity(intent);
            Log.d("verifeied","yes");
            finish();
        }
        else{
            if (user.isEmailVerified()) {

                Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                startActivity(intent);
                Log.d("verifeied","yes");
                finish();

            } else {
                Log.d("verifeied","no");
                verify_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user.isEmailVerified()) {

                            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                            startActivity(intent);
                            Log.d("verifeied","yes");
                            finish();

                        }
                        else {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(VerifyActivity.this, "Verification Email sent", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                        }

                    }
                });

            }
        }

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getEmail().toString().equals("iitmandimessapp@gmail.com")){
                    startActivity(new Intent(VerifyActivity.this, AdminActivity.class));
                    finish();
                }
                else{
                    if (user.isEmailVerified()) {

                        startActivity(new Intent(VerifyActivity.this, MainActivity.class));
                        finish();
                    } else {
                        auth.signOut();
                        startActivity(new Intent(VerifyActivity.this, LoginActivity.class));
                        finish();
                    }
                }

            }
        });


    }
}
