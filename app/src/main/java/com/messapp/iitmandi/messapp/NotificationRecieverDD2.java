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
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationRecieverDD2 extends BroadcastReceiver {

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
        final PendingIntent pendingIntent = PendingIntent.getActivity(context,30,intent_repeating,PendingIntent.FLAG_UPDATE_CURRENT);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("menu").child("D2").child(dayOfTheWeek).child("Dinner");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final StringBuilder menu= new StringBuilder(1000);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String item = postSnapshot.getKey().toString();
                    menu.append(item);
                    menu.append(" | ");
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.mess_logo)
                        .setContentTitle("Dinner D2 Menu")
                        .setSound(alarmSound)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(menu))
                        .setContentText(menu)
                        .setAutoCancel(true);

                notificationManager.notify(30,builder.build());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

