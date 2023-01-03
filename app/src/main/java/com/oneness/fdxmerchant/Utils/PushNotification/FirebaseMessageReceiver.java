package com.oneness.fdxmerchant.Utils.PushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.oneness.fdxmerchant.Activity.EntryPoint.SplashActivity;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.Prefs;

public class FirebaseMessageReceiver extends FirebaseMessagingService {
    final  static String CHANNEL_ID="1";
    static String TAG="pushdata";
    Prefs prefs;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        prefs = new Prefs(getApplicationContext());
        prefs.saveData("hasNoti", "y");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            issueNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }
        else
        {
            addNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void addNotification2aa(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }


    void issueNotification(String title,String msg)
    {
        int notificationId = 001;
        Intent viewIntent = new Intent(this, SplashActivity.class);
        PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addNotification2aa("CHANNEL_1", "Echannel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        /*Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        r.play();*/
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notification
                //.setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(msg)
                .setLargeIcon(bitmap)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(viewPendingIntent)
                .setNumber(3);
                //.setSound(soundUri);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(String msg,String title)
    {
        int notificationId = 001;
        Intent viewIntent = new Intent(this, SplashActivity.class);
        PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        int myColor =getResources().getColor(R.color.colorAccent);
        Notification mNotification =new NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.ic_stat_name)
                //.setColor(myColor)
                .setContentTitle(title)
                .setContentText(msg)
                .setLargeIcon(bitmap)
                .setContentIntent(viewPendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, mNotification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FIRE-TOKEN", token);
        //sendTokenToTheAppServer(token);
    }
}
