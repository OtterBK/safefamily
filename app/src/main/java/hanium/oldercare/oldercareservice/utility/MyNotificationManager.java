package hanium.oldercare.oldercareservice.utility;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import hanium.oldercare.oldercareservice.HomeActivity;
import hanium.oldercare.oldercareservice.R;

public class MyNotificationManager {

    public static void sendAlarm(Activity activity, String content){
        //   handler.sendEmptyMessageDelayed(0,20000);
        //Notification 알림바 아이콘 설정
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.new_icon);

        //Notification 알림 사운드 설정
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //알림바 클릭시 이동을 위한 Intent
        Intent intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
        //버튼 클릭시 액티비티 중복 실행 방지를 위해 기존 액티비티를 불러옴. Single top
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //노티피케이션 빌더 : 위에서 생성한 이미지나 텍스트, 사운드등을 설정해줍니다.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity.getApplicationContext())
                .setSmallIcon(android.R.drawable.ic_menu_gallery)
                .setLargeIcon(bitmap)
                .setContentTitle("양키캔")
                .setContentText("버튼을 클릭하여 알림이 도착했습니다.")
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        //노티피케이션을 생성합니다.
        notificationManager.notify(0  /*ID of notification*/ , notificationBuilder.build());


    }

    public static void sendDangerNotification(Activity activity, String target_name) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, "default");

        builder.setSmallIcon(R.drawable.appicon);
        builder.setContentTitle("이상 징후 파악됨");
        builder.setContentText(target_name+" 님의 이상 징후가 파악되었습니다.");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(target_name+" 님의 이상 징후가 파악되었습니다.\n앱에서 상세정보를 확인하세요."));
        builder.setPriority(Notification.PRIORITY_MAX);

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값

        boolean isAlreadySend = false;

        for(StatusBarNotification activeNotification : notificationManager.getActiveNotifications()){
            if(activeNotification.getId() == 1){ //이미 이상징후 보냈다면
                isAlreadySend = true;
                break;
            }
        }

        if(!isAlreadySend)
            notificationManager.notify(1, builder.build());
    }

}
