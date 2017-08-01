package com.messapp.iitmandi.messapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef, emailRef;
    private ImageButton date_select;
    private TextView date_display;
    private Spinner spinner_mess;
    private DatePickerDialog datePickerDialog;
    static boolean active = false;
    private String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Button day_selected;
    private RecyclerView recyclerView;
    ArrayList<AdminFeedGetter> feedList;
    private AdminFeedItemAdapter adapterSearch;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
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



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        date_select = (ImageButton)findViewById(R.id.choose_date);
        date_display = (TextView)findViewById(R.id.date_tv);
        spinner_mess = (Spinner)findViewById(R.id.spinner_mess);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(user.getUid().toString());
        myRef.setValue(user.getEmail().toString());

        day_selected = (Button)findViewById(R.id.day_selected);

        Calendar c = Calendar.getInstance();
        date_display.setText(c.get(Calendar.DAY_OF_MONTH) + " " + month[c.get(Calendar.MONTH)] + " "
                                + c.get(Calendar.YEAR));

        date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current from_date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // from_date picker dialog
                datePickerDialog = new DatePickerDialog(AdminActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                date_display.setText(dayOfMonth + " " + month[monthOfYear] + " " +
                                        year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        day_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFeedback();
            }
        });

    }

    private void loadFeedback() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        feedList = new ArrayList<>();
        feedList.clear();
        progressBar.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapterSearch = new AdminFeedItemAdapter(this, feedList);
        recyclerView.setAdapter(adapterSearch);

        myRef = database.getReference("feedback").child(date_display.getText().toString()).child(spinner_mess.getSelectedItem().toString());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String uId = postSnapshot.getKey().toString();
                    Log.d("UId",uId);
                    for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()){
                        final String meal = postSnapshot2.getKey().toString();
                        for (DataSnapshot postSanpshot3 : postSnapshot2.getChildren()){
                            final String itemName = postSanpshot3.getKey().toString();
                            final String feedText = postSanpshot3.child("Feed").getValue().toString();
                            final String rating = postSanpshot3.child("Rating").getValue().toString();
                            emailRef = database.getReference("users").child(uId);
                            emailRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String emailText = dataSnapshot.getValue().toString();
                                    feedList.add(new AdminFeedGetter(emailText,meal,itemName,rating,feedText));
                                    adapterSearch.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

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
            ft.replace(R.id.admin_content_frame, fragment);
            ft.commit();
        } else {

            startActivity(new Intent(AdminActivity.this, AdminActivity.class));
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
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
