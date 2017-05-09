package com.messapp.iitmandi.messapp;

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
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Spinner spinner_item,spinner_time;
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


    }

    private void SubmitFeed() {
        if(editText_feedback.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Please give some feedback.",
                    Toast.LENGTH_LONG).show();
        }else if (ratingBar.getRating()==0){
            Toast.makeText(MainActivity.this, "Please enter some rating.",
                    Toast.LENGTH_LONG).show();
        }else {
            myRef = database.getReference("feedback").child(date).child(user.getUid().toString()).child(spinner_time.getSelectedItem().toString()).child(spinner_item.getSelectedItem().toString());
            myRef.child("Feed").setValue(editText_feedback.getText().toString());
            myRef.child("Rating").setValue(ratingBar.getRating());
            editText_feedback.setText("");
            ratingBar.setRating(0);
            Toast.makeText(MainActivity.this, "Your feedback has been recorded.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getTime() {
        myRef = database.getReference("menu").child(dayOfTheWeek).child(spinner_time.getSelectedItem().toString());
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
                    Toast.makeText(MainActivity.this, "Enter your feedback.",
                            Toast.LENGTH_SHORT).show();
                }
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
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_log_out) {
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
        } else if (id == R.id.nav_on_leave) {
            displayView(R.id.nav_on_leave);
        } else if (id == R.id.nav_mess_menu) {
            displayView(R.id.nav_mess_menu);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displayView(int viewId) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_on_leave:
                fragment = new OnLeave();
                title = "Going on Leave!";
                break;
            case R.id.nav_feedback:
                fragment = null;
                title = "Feedback";
                break;
            case R.id.nav_mess_menu:
                fragment = new MessMenu();
                title = "Mess Menu";
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
}
