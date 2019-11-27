package com.rokkhi.brokers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


handleNowdataforparcel(remoteMessage);
    }



    private void handleNowdataforparcel(final RemoteMessage remoteMessage) {

        String company = remoteMessage.getData().get("company");
        String guard = remoteMessage.getData().get("guard");
        String pic = remoteMessage.getData().get("pic");
        String uid= remoteMessage.getData().get("uid");
        String type= remoteMessage.getData().get("type");
        final String click_action= remoteMessage.getData().get("click_action");
        Long time = remoteMessage.getSentTime();



        Log.d(TAG, "handleNowdataforparcel: time3 "+ time);


        int mNotificationID = (int) System.currentTimeMillis();

/*


        Intent intent = new Intent(getApplicationContext(), FullscreenParcel.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("company",company);
        intent.putExtra("guard",guard);
        intent.putExtra("pic",pic);
        intent.putExtra("uid",uid);
        intent.putExtra("time",time);
        intent.putExtra("type",type);
        intent.putExtra("nid",mNotificationID);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

*/


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"notification_channel_id")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("New parcel arrived")
                .setContentText(company)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(company))

                .setLights(Color.WHITE, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_MAX);





        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification_channel_id",
                    "Default channel", NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }


        manager.notify(mNotificationID, mBuilder.build());

//        startActivity(intent);

        Log.d(TAG, "Short lived task is done.");

    }




}
