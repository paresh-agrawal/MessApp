package com.messapp.iitmandi.messapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Spinner spinner_item,spinner_time,spinner_mess;
    private String day;
    private Button day_selected,button_submit;
    private RatingBar ratingBar;
    private EditText editText_feedback;
    private ArrayList<String> ar,time;
    private ArrayAdapter<String> adapter,adapter_time;
    static boolean active = false;
    private DrawerLayout drawer;
    private ProgressBar progressBar;
    private LinearLayout feedback_linear_layout;
    private TextView tv_userId;
    private String dayOfTheWeek;
    private Date d;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        database = FirebaseDatabase.getInstance();

        //spinner_day = (Spinner)findViewById(R.id.spinner_day);
        spinner_item = (Spinner)findViewById(R.id.spinner_item);
        spinner_mess = (Spinner)findViewById(R.id.spinner_mess);
        spinner_time = (Spinner)findViewById(R.id.spinner_time);
        day_selected = (Button) findViewById(R.id.day_selected);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        editText_feedback = (EditText)findViewById(R.id.editText_feedback);
        button_submit = (Button)findViewById(R.id.button_submit);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        feedback_linear_layout = (LinearLayout)findViewById(R.id.feedback_linear_layout);
        tv_userId = (TextView)header.findViewById(R.id.textView_userId);

        tv_userId.setText(user.getEmail().toString());

        ar = new ArrayList<String>();
        time = new ArrayList<String>();
        date = DateFormat.getDateInstance().format(new Date());

        adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, ar);
        adapter_time = new ArrayAdapter<String>(this, R.layout.spinner_item, time);
        time.add("Breakfast");
        time.add("Lunch");
        time.add("Snacks");
        time.add("Dinner");
        spinner_time.setAdapter(adapter_time);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        d = new Date();
        dayOfTheWeek = sdf.format(d);

        myRef = database.getReference("users").child(user.getUid().toString());
        myRef.setValue(user.getEmail().toString());

        day_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime();
            }
        });
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitFeed();
            }
        });

        //notifications();

    }

    private void notifications() {
        long time = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,07);
        calendar.set(Calendar.MINUTE,20);
        if (time < calendar.getTimeInMillis()) {
            Intent intent = new Intent(getApplicationContext(), NotificationRecieverBD1.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 07);
        calendar1.set(Calendar.MINUTE, 20);
        if (time < calendar1.getTimeInMillis()) {
            Intent intent1 = new Intent(getApplicationContext(), NotificationRecieverBD2.class);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 90, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), alarmManager1.INTERVAL_DAY, pendingIntent1);
        }

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY,11);
        calendar2.set(Calendar.MINUTE,50);
        if (time < calendar2.getTimeInMillis()) {
            Intent intent2 = new Intent(getApplicationContext(), NotificationRecieverLD1.class);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 80, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), alarmManager2.INTERVAL_DAY, pendingIntent2);
        }

        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.HOUR_OF_DAY,11);
        calendar3.set(Calendar.MINUTE,50);
        if (time < calendar3.getTimeInMillis()) {
            Intent intent3 = new Intent(getApplicationContext(), NotificationRecieverLD2.class);
            PendingIntent pendingIntent3 = PendingIntent.getBroadcast(getApplicationContext(), 70, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager3 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), alarmManager3.INTERVAL_DAY, pendingIntent3);
        }

        Calendar calendar4 = Calendar.getInstance();
        calendar4.set(Calendar.HOUR_OF_DAY,16);
        calendar4.set(Calendar.MINUTE,50);
        if (time < calendar4.getTimeInMillis()) {
            Intent intent4 = new Intent(getApplicationContext(), NotificationRecieverSD1.class);
            PendingIntent pendingIntent4 = PendingIntent.getBroadcast(getApplicationContext(), 60, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager4 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager4.setRepeating(AlarmManager.RTC_WAKEUP, calendar4.getTimeInMillis(), alarmManager4.INTERVAL_DAY, pendingIntent4);
        }

        Calendar calendar5 = Calendar.getInstance();
        calendar5.set(Calendar.HOUR_OF_DAY,16);
        calendar5.set(Calendar.MINUTE,50);
        if (time < calendar5.getTimeInMillis()) {
            Intent intent5 = new Intent(getApplicationContext(), NotificationRecieverSD2.class);
            PendingIntent pendingIntent5 = PendingIntent.getBroadcast(getApplicationContext(), 50, intent5, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager5 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager5.setRepeating(AlarmManager.RTC_WAKEUP, calendar5.getTimeInMillis(), alarmManager5.INTERVAL_DAY, pendingIntent5);
        }

        Calendar calendar6 = Calendar.getInstance();
        calendar6.set(Calendar.HOUR_OF_DAY,19);
        calendar6.set(Calendar.MINUTE,20);
        if (time < calendar6.getTimeInMillis()) {
            Intent intent6 = new Intent(getApplicationContext(), NotificationRecieverDD1.class);
            PendingIntent pendingIntent6 = PendingIntent.getBroadcast(getApplicationContext(), 40, intent6, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager6 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager6.setRepeating(AlarmManager.RTC_WAKEUP, calendar6.getTimeInMillis(), alarmManager6.INTERVAL_DAY, pendingIntent6);
        }

        Calendar calendar7 = Calendar.getInstance();
        calendar7.set(Calendar.HOUR_OF_DAY,19);
        calendar7.set(Calendar.MINUTE,20);
        if (time < calendar7.getTimeInMillis()) {
            Intent intent7 = new Intent(getApplicationContext(), NotificationRecieverDD2.class);
            PendingIntent pendingIntent7 = PendingIntent.getBroadcast(getApplicationContext(), 30, intent7, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager7 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager7.setRepeating(AlarmManager.RTC_WAKEUP, calendar7.getTimeInMillis(), alarmManager7.INTERVAL_DAY, pendingIntent7);
        }
    }

    private void SubmitFeed() {
        if(editText_feedback.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Please give some feedback.",
                    Toast.LENGTH_LONG).show();
        }else if (ratingBar.getRating()==0){
            Toast.makeText(MainActivity.this, "Please enter some rating.",
                    Toast.LENGTH_LONG).show();
        }else {
            myRef = database.getReference("feedback").child(date).child(spinner_mess.getSelectedItem().toString())
                    .child(user.getUid().toString())
                    .child(spinner_time.getSelectedItem().toString()).child(spinner_item.getSelectedItem().toString());
            myRef.child("Feed").setValue(editText_feedback.getText().toString());
            myRef.child("Rating").setValue(ratingBar.getRating());
            editText_feedback.setText("");
            ratingBar.setRating(0);
            Toast.makeText(MainActivity.this, "Your feedback has been recorded.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getTime() {
        myRef = database.getReference("menu").child(spinner_mess.getSelectedItem().toString()).child(dayOfTheWeek).child(spinner_time.getSelectedItem().toString());
        ar.clear();
        feedback_linear_layout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        editText_feedback.setText("");
        ratingBar.setRating(0);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    feedback_linear_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    String item = postSnapshot.getKey().toString();

                    ar.add(item);
                    spinner_item.setAdapter(adapter);

                }
                Toast.makeText(MainActivity.this, "Enter your feedback.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {
            displayView(R.id.nav_feedback);
        } else if (id == R.id.nav_mess_menu) {
            displayView(R.id.nav_mess_menu);
        } else if (id == R.id.nav_share) {
            shareIt();
        } else if (id == R.id.nav_credits){
            displayView(R.id.nav_credits);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_feedback:
                fragment = null;
                title = "Feedback";
                break;
            case R.id.nav_mess_menu:
                fragment = new MessMenu();
                title = "Mess Menu";
                break;
            case R.id.nav_credits:
                fragment = new Credits();
                title = "Credits";
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {

            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mess App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Download the IIT Mandi Mess App and give your valuable feedback https://play.google.com/store/apps/details?id=com.messapp.iitmandi.messapp");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
