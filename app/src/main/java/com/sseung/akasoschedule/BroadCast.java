package com.sseung.akasoschedule;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BroadCast extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("tlqkf2", "왜 실행 안되냐");
        PendingIntent mPending = PendingIntent.getActivity(context,
                0, new Intent(context, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel mChannel = new NotificationChannel("alarm", "ddd", NotificationManager.IMPORTANCE_DEFAULT);

        mChannel.setDescription("descripionText");

        NotificationCompat.Builder mbuilder =
                new NotificationCompat.Builder(context, "alarm")
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("알람")
                        .setContentText("오늘 아카소의 일정이 있습니다.")
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setContentIntent(mPending);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(mChannel);
        manager.notify(0, mbuilder.build());
    }
}
