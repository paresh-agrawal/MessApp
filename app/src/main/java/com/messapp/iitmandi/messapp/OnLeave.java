package com.messapp.iitmandi.messapp;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class OnLeave extends Fragment {

    TextView from_date,to_date;
    ImageButton choose_from_date,choose_to_date;
    DatePickerDialog datePickerDialog;
    FrameLayout on_leave_frame_layout;
    EditText editText_reason;
    Button submit;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authListener;
    private String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int selected_month_to, selected_month_from, selected_day_to, selected_day_from, selected_year_to, selected_year_from;

    public OnLeave() {
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
        View on_leave = inflater.inflate(R.layout.fragment_on_leave, container, false);

        // initiate the from_date picker and a button
        from_date = (TextView) on_leave.findViewById(R.id.from_date);
        to_date = (TextView)on_leave.findViewById(R.id.to_date);
        choose_from_date = (ImageButton)on_leave.findViewById(R.id.choose_from_date);
        choose_to_date = (ImageButton)on_leave.findViewById(R.id.choose_to_date);
        on_leave_frame_layout = (FrameLayout)on_leave.findViewById(R.id.on_leave_frame_layout);
        editText_reason = (EditText)on_leave.findViewById(R.id.editText_reason);
        submit = (Button)on_leave.findViewById(R.id.btn_submit);

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

        database = FirebaseDatabase.getInstance();

        // perform click event on edit text
        on_leave_frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        choose_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current from_date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // from_date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                from_date.setText(month[monthOfYear] + " " + dayOfMonth + ", " +
                                                    year);
                                selected_day_from = dayOfMonth;
                                selected_month_from = monthOfYear;
                                selected_year_from = year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        choose_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current from_date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // from_date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                to_date.setText(month[monthOfYear] + " " + dayOfMonth + ", " +
                                        year);
                                selected_day_to = dayOfMonth;
                                selected_month_to = monthOfYear;
                                selected_year_to = year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_year_to>=selected_year_from){
                    if (selected_month_to>=selected_month_from){
                        if (selected_day_to>=selected_day_from){
                            myRef = database.getReference("onLeave").child(user.getUid().toString());
                            myRef.child("From").setValue(from_date.getText().toString());
                            myRef.child("To").setValue(to_date.getText().toString());
                            myRef.child("Reason").setValue(editText_reason.getText().toString());
                            from_date.setText("");
                            to_date.setText("");
                            editText_reason.setText("");
                            Toast.makeText(getActivity(), "Your response has been recorded.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Please fill the details properly.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), "Please fill the details properly.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Please fill the details properly.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });




        return on_leave;
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
