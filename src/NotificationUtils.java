import android.annotation.TargetApi;
import android.app.Notification;
// import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ragult on 10/20/2017.
 */

public class NotificationUtils extends BroadcastReceiver {

    private NotificationManager mManager;
    private Context context;
    private PendingIntent intent_pending;
    private Notification myNotification;
    private String title, author;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private String result = "";
    private String replyLabel = "Reply";
    public static final int REPLY_NOTIFICATION_ID = 2;
    private NotificationManagerCompat mNotificationManager;
    /* private NotificationChannel androidChannel;
    public static final String ANDROID_CHANNEL_ID = "com.alert.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL"; */

    public NotificationUtils() {
        // super();
        /* if ( Build.VERSION.SDK_INT >= 26 )
            createChannels(); */
    }

    public void setMessageDetails( String msg_title, String msg_author ){
        title = msg_title;
        author = msg_author;
    }

    public PendingIntent getReplyPendingIntent(){

        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // start a
            // (i)  broadcast receiver which runs on the UI thread or
            // (ii) service for a background task to b executed , but for the purpose of
            // this codelab, will be doing a broadcast receiver
            intent = new Intent(context, NotificationUtils.class);
            /* intent.setAction(REPLY_ACTION);
            intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
            intent.putExtra(KEY_MESSAGE_ID, messageId); */
            return PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity for Android M and below
            intent = new Intent(context, MainActivity.class);
            /* intent.setAction(REPLY_ACTION);
            intent.putExtra(KEY_MESSAGE_ID, messageId);
            intent.putExtra(KEY_NOTIFICATION_ID, notifyId); */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return PendingIntent.getActivity(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    /* @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {

        // create android channel
        androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        // mManager.createNotificationChannel(androidChannel);
    } */

    @Override
    public void onReceive(Context context, Intent intent){

        /* Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if( remoteInput == null ){
            return;
        }
        String reply = remoteInput.getString(KEY_TEXT_REPLY);
        Notification replyNotification = new Notification.Builder(context).setSmallIcon(R.drawable.ic_stat_ic_notification).setContentTitle("From: " + author).setContentText("Message: " + reply).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(REPLY_NOTIFICATION_ID, replyNotification); */

        Intent notificationIntent = new Intent(context, NotificationUtils.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Notification.FLAG_AUTO_CANCEL);
        intent_pending = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        myNotification = new Notification.Builder(context)
                .setContentTitle("Message: " + title)
                .setContentText("From: " + author)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true)
                .setContentIntent(intent_pending)
                .addAction(R.drawable.ic_send_black_24dp, "Reply", intent_pending)
                .build();
        if ( mManager == null ){
            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        /* RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();
        Notification.Action replyAction = new Notification.Action.Builder(
                R.drawable.ic_send_black_24dp, replyLabel, getReplyPendingIntent())
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        Notification.Builder mBuilder = new Notification.Builder(context)
            .setSmallIcon(R.drawable.ic_facebook_white_22dp)
            .setContentTitle("msg")
            .setContentText("user")
            .setShowWhen(true)
            .addAction(replyAction); // reply action from step b above */

        /* if ( mManager == null ){

            mNotificationManager = (NotificationManagerCompat) context.getSystemService(Context.NOTIFICATION_SERVICE); // NotificationManagerCompat.from(this);
        } */
        /* if ( mManager == null ){
            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mManager.notify(0, mBuilder.build()); */

        /* if ( Build.VERSION.SDK_INT >= 26 )
            mManager.createNotificationChannel(androidChannel); */
        mManager.notify(0, myNotification);

        /* RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY)
                .setLabel("Reply")
                .build();

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_stat_ic_notification, "Reply", getReplyPendingIntent())
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        Intent intents;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // start a
            // (i)  broadcast receiver which runs on the UI thread or
            // (ii) service for a background task to b executed , but for the purpose of
            // this codelab, will be doing a broadcast receiver
            intents = new Intent(context, NotificationUtils.class);
            intents.setAction(REPLY_ACTION);
            intents.putExtra(KEY_NOTIFICATION_ID, notificationId);
            intents.putExtra(KEY_MESSAGE_ID, messageId);
            return PendingIntent.getBroadcast(getApplicationContext(), 100, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity for Android M and below
            intents = new Intent(context, ReplyActivity.class);
            intents.setAction(REPLY_ACTION);
            intents.putExtra(KEY_MESSAGE_ID, messageId);
            intents.putExtra(KEY_NOTIFICATION_ID, notifyId);
            intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return PendingIntent.getActivity(this, 100, intents,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } */

    }
}