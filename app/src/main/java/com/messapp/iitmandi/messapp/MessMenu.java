package com.messapp.iitmandi.messapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessMenu extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authListener;
    private RecyclerView recyclerView;
    ArrayList<MenuItemGetter> userList;
    private MessItemAdapter adapterSearch;
    private String dayOfTheWeek;
    private Date d;
    private String date;
    private ArrayList<String> ar,time;
    private ArrayAdapter<String> adapter,adapter_time;

    public MessMenu() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mess_menu = inflater.inflate(R.layout.fragment_menu, container, false);

        LinearLayout mess_menu_ll = (LinearLayout)mess_menu.findViewById(R.id.mess_menu_ll);
        final ProgressBar progressBar = (ProgressBar)mess_menu.findViewById(R.id.progressBar);
        final Spinner spinner_day = (Spinner)mess_menu.findViewById(R.id.spinner_day);
        final Spinner spinner_time = (Spinner)mess_menu.findViewById(R.id.spinner_time);
        final Spinner spinner_mess = (Spinner)mess_menu.findViewById(R.id.spinner_mess);
        final Button btn_go = (Button)mess_menu.findViewById(R.id.btn_go);

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
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        };

        recyclerView = (RecyclerView) mess_menu.findViewById(R.id.recycler_view);
        userList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapterSearch = new MessItemAdapter(getActivity(),userList);
        recyclerView.setAdapter(adapterSearch);

        database = FirebaseDatabase.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        d = new Date();
        dayOfTheWeek = sdf.format(d);
        ar = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, ar);

        mess_menu_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                userList.clear();
                adapterSearch.notifyDataSetChanged();
                if (spinner_day.getSelectedItem().toString().equals("Today")){
                    myRef = database.getReference("menu").child(spinner_mess.getSelectedItem().toString())
                            .child(dayOfTheWeek).child(spinner_time.getSelectedItem().toString());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                String item = postSnapshot.getKey().toString();
                                userList.add(new MenuItemGetter(item));
                                adapterSearch.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else{
                    myRef = database.getReference("menu").child(spinner_mess.getSelectedItem().toString())
                            .child(spinner_day.getSelectedItem().toString()).child(spinner_time.getSelectedItem().toString());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                String item = postSnapshot.getKey().toString();
                                userList.add(new MenuItemGetter(item));
                                adapterSearch.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }
        });


        return mess_menu;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}