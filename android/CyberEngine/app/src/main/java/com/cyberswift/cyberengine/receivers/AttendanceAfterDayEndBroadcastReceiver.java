package com.cyberswift.cyberengine.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.RemoteViews;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.services.AttendanceAfterDayEndService;
import com.cyberswift.cyberengine.utility.Constants;


public class AttendanceAfterDayEndBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent actionIntentNo = new Intent(context, AttendanceAfterDayEndService.class);
        actionIntentNo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        actionIntentNo.putExtra(Constants.IS_WORKING, false);

        Intent actionIntentYes = new Intent(context, AttendanceAfterDayEndService.class);
        actionIntentYes.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        actionIntentYes.putExtra(Constants.IS_WORKING, true);

        Spannable appName = new SpannableString(context.getResources().getString(R.string.app_name_first_part) + context.getResources().getString(R.string.app_name_second_part));
        appName.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorSecondaryText)), context.getResources().getString(R.string.app_name_first_part).length()+1,
                appName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        RemoteViews mContentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification_buttons);
        mContentView.setImageViewResource(R.id.noti_image, R.drawable.notification_icon);
        mContentView.setTextViewText(R.id.noti_title, appName);
        mContentView.setTextViewText(R.id.noti_text, "It's after 6:30pm. Are you still working?");
        mContentView.setOnClickPendingIntent(R.id.tv_yes, PendingIntent.getService(context, 0, actionIntentYes, PendingIntent.FLAG_UPDATE_CURRENT));
        mContentView.setOnClickPendingIntent(R.id.tv_no, PendingIntent.getService(context, 1, actionIntentNo, PendingIntent.FLAG_UPDATE_CURRENT));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "AttendanceAfterDayEndBroadcastReceiver")
                .setSmallIcon(R.mipmap.notification_icon)
                .setAutoCancel(true)
                .setSound(uri)
                .setOngoing(true)
                .setCustomBigContentView(mContentView);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Constants.MANUAL_ATTENDANCE_AFTER_DAY_END, mBuilder.build());
    }
}