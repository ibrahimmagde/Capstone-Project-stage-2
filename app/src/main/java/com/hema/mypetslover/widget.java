package com.hema.mypetslover;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;



public class widget extends  AppWidgetProvider {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    int x;
    int m=0;


    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
        final     RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            firebaseDatabase = FirebaseDatabase.getInstance();
            myRef = firebaseDatabase.getReference("User_Details");
/**************/
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                     m=0;
                    x=new Random().nextInt((int)dataSnapshot.getChildrenCount());

                    for (DataSnapshot val2 : dataSnapshot.getChildren()) {

                          if(m==x)

                           {

                               Intent itemIntent = new Intent(context,ShowData.class);
                               PendingIntent myPI = PendingIntent.getActivity(context, 0, itemIntent , 0);
                               remoteViews.setOnClickPendingIntent(R.id.lover,myPI);

                               remoteViews.setTextViewText(R.id.gender, val2.child("Gender").getValue(String.class));
                               remoteViews.setTextViewText(R.id.live, val2.child("District").getValue(String.class));
                               remoteViews.setTextViewText(R.id.call, val2.child("Phone").getValue(String.class));
                               remoteViews.setTextViewText(R.id.email, val2.child("Email").getValue(String.class));
                               remoteViews.setTextViewText(R.id.name, val2.child("Image_Title").getValue(String.class));

                               try {
                                   Picasso.with(context).load(Uri.parse(val2.child("Image_URL").getValue(String.class)))
                                           .into(remoteViews, R.id.loverimage, appWidgetIds);

                               } catch (Exception e) {

                                   remoteViews.setImageViewResource(R.id.loverimage, R.drawable.icon);
                                   e.printStackTrace();
                               }



                           }
                           m++;


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



/*****************/

            Intent intent = new Intent(context, widget.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.load_another, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
               // Log.i(widget.class.getName(), context.getResources().getString(R.string.widget_refreshing_msg));
            }
        }
    }
}