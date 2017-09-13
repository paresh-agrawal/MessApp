package com.messapp.iitmandi.messapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, VerifyActivity.class));
            finish();
        }


        // set the view now
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        database = FirebaseDatabase.getInstance();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //notifications();

                email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(email.contains("@students.iitmandi.ac.in") || email.contains("iitmandimessapp@gmail.com") || email.equals("admin"))){
                    email = email + "@students.iitmandi.ac.in";
                }
                if (email.equals("admin")){
                    email = "iitmandimessapp@gmail.com";
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    if (email.equals("iitmandimessapp@gmail.com")){
                                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });
            }
        });
    }

    private void notifications() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,07);
        calendar.set(Calendar.MINUTE,20);
        Intent intent = new Intent(getApplicationContext(),NotificationRecieverBD1.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY,07);
        calendar1.set(Calendar.MINUTE,20);
        Intent intent1 = new Intent(getApplicationContext(),NotificationRecieverBD2.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(),90,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager1 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,calendar1.getTimeInMillis(),alarmManager1.INTERVAL_DAY,pendingIntent1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY,11);
        calendar2.set(Calendar.MINUTE,50);
        Intent intent2 = new Intent(getApplicationContext(),NotificationRecieverLD1.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(),80,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager2 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,calendar2.getTimeInMillis(),alarmManager2.INTERVAL_DAY,pendingIntent2);

        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.HOUR_OF_DAY,11);
        calendar3.set(Calendar.MINUTE,50);
        Intent intent3 = new Intent(getApplicationContext(),NotificationRecieverLD2.class);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getApplicationContext(),70,intent3,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager3 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP,calendar3.getTimeInMillis(),alarmManager3.INTERVAL_DAY,pendingIntent3);

        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(Calendar.HOUR_OF_DAY,16);
        calendar4.set(Calendar.MINUTE,50);
        Intent intent4 = new Intent(getApplicationContext(),NotificationRecieverSD1.class);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(getApplicationContext(),60,intent4,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager4 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager4.setRepeating(AlarmManager.RTC_WAKEUP,calendar4.getTimeInMillis(),alarmManager4.INTERVAL_DAY,pendingIntent4);

        Calendar calendar5 = Calendar.getInstance();
        calendar5.set(Calendar.HOUR_OF_DAY,16);
        calendar5.set(Calendar.MINUTE,50);
        Intent intent5 = new Intent(getApplicationContext(),NotificationRecieverSD2.class);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(getApplicationContext(),50,intent5,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager5 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager5.setRepeating(AlarmManager.RTC_WAKEUP,calendar5.getTimeInMillis(),alarmManager5.INTERVAL_DAY,pendingIntent5);

        Calendar calendar6 = Calendar.getInstance();
        calendar6.set(Calendar.HOUR_OF_DAY,19);
        calendar6.set(Calendar.MINUTE,20);
        Intent intent6 = new Intent(getApplicationContext(),NotificationRecieverDD1.class);
        PendingIntent pendingIntent6 = PendingIntent.getBroadcast(getApplicationContext(),40,intent6,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager6 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager6.setRepeating(AlarmManager.RTC_WAKEUP,calendar6.getTimeInMillis(),alarmManager6.INTERVAL_DAY,pendingIntent6);

        Calendar calendar7 = Calendar.getInstance();
        calendar7.set(Calendar.HOUR_OF_DAY,19);
        calendar7.set(Calendar.MINUTE,20);
        Intent intent7 = new Intent(getApplicationContext(),NotificationRecieverDD2.class);
        PendingIntent pendingIntent7 = PendingIntent.getBroadcast(getApplicationContext(),30,intent7,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager7 = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager7.setRepeating(AlarmManager.RTC_WAKEUP,calendar7.getTimeInMillis(),alarmManager7.INTERVAL_DAY,pendingIntent7);
    }
}