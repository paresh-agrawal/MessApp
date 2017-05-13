package com.messapp.iitmandi.messapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class Credits extends Fragment {


    public Credits() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View credits = inflater.inflate(R.layout.fragment_credits, container, false);

        LinearLayout github_link = (LinearLayout)credits.findViewById(R.id.github_link);
        LinearLayout app_credits_layout = (LinearLayout)credits.findViewById(R.id.app_credits_layout);

        app_credits_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return ;
            }
        });

        github_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/paresh-agrawal");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return credits;
    }

}
