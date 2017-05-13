package com.messapp.iitmandi.messapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminOnLeave extends Fragment {


    private RecyclerView recyclerView;
    ArrayList<AdminOnLeaveGetter> feedList;
    private AdminOnLeaveAdapter adapterSearch;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef, emailRef;
    private ProgressBar progressBar;

    public AdminOnLeave() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View admin_on_leave = inflater.inflate(R.layout.fragment_admin_on_leave, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = (ProgressBar)admin_on_leave.findViewById(R.id.progressBar);
        RelativeLayout admin_on_leave_layout = (RelativeLayout) admin_on_leave.findViewById(R.id.admin_on_leave_layout);

        admin_on_leave_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

        database = FirebaseDatabase.getInstance();
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) admin_on_leave.findViewById(R.id.recycler_view);
        feedList = new ArrayList<>();
        feedList.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapterSearch = new AdminOnLeaveAdapter(getActivity(), feedList);
        recyclerView.setAdapter(adapterSearch);

        myRef = database.getReference("onLeave");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String uId = postSnapshot.getKey().toString();
                    final String from_date = postSnapshot.child("From").getValue().toString();
                    final String reason = postSnapshot.child("Reason").getValue().toString();
                    final String to_date = postSnapshot.child("To").getValue().toString();
                    emailRef = database.getReference("users").child(uId);
                    emailRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            String emailText = dataSnapshot.getValue().toString();
                            feedList.add(new AdminOnLeaveGetter(emailText,to_date,reason,from_date));
                            adapterSearch.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return admin_on_leave;
    }

}
