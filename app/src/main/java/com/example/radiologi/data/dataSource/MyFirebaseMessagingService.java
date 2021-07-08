package com.example.radiologi.data.dataSource;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.radiologi.R;
import com.example.radiologi.admin.home.DataAdminActivity;
import com.example.radiologi.data.dataSource.local.SharedPreferenceManager;
import com.example.radiologi.dokter.home.DataDokterActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    PendingIntent pendingIntent;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token" + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() !=null) {
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
            Log.d(TAG, remoteMessage.toString());
        }
    }

    private void sendNotification(String messageBody, String title) {
        String channelId = getString(R.string.channelId);
        String channelName = getString(R.string.channelName);

        if (SharedPreferenceManager.getStringPreferences(getApplicationContext(), "role").equals("admin")) {
            Intent intent = new Intent(this, DataAdminActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else if (SharedPreferenceManager.getStringPreferences(getApplicationContext(), "role").equals("dokter")) {
            Intent intent = new Intent(this, DataDokterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setVibrate(new long[]{ 1000, 1000, 1000 })
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .setCategory(messageBody);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.canShowBadge();
            mBuilder.setChannelId(channelId);

            if (mNotificationManager !=null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        mBuilder.setFullScreenIntent(pendingIntent, true);
        mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
        Notification notification = mBuilder.build();

        if (mNotificationManager !=null) {
            mNotificationManager.notify(0, notification);
        }
    }
}
