package com.messapp.iitmandi.messapp;

/**
 * Created by paresh on 2/8/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by starapps on 1/24/2017.
 */
public class NotificationRecieverSD1 extends BroadcastReceiver {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String dayOfTheWeek;
    private Date d;

    @Override
    public void onReceive(final Context context, Intent intent) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        d = new Date();
        dayOfTheWeek = sdf.format(d);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent_repeating = new Intent(context,MainActivity.class);
        intent_repeating.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context,60,intent_repeating,PendingIntent.FLAG_UPDATE_CURRENT);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("menu").child("D1").child(dayOfTheWeek).child("Snacks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final StringBuilder menuD1= new StringBuilder(1000);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String item = postSnapshot.getKey().toString();
                    menuD1.append(item);
                    menuD1.append(" | ");
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.mess_logo)
                        .setContentTitle("Snacks D1 Menu")
                        .setSound(alarmSound)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(menuD1))
                        .setContentText(menuD1)
                        .setAutoCancel(true);

                notificationManager.notify(60,builder.build());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

