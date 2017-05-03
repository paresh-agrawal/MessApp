package com.messapp.iitmandi.messapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user.isEmailVerified()) {

            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();


        } else {

            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(VerifyActivity.this, "Verification Email sent Champion!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }



    }
}
