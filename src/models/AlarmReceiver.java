package models;

import com.example.wmm.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.CalendarView;

public class AlarmReceiver extends BroadcastReceiver
{
    public void onReceive(Context pContext, Intent pIntent) {

//      if (pIntent.getAction().equalsIgnoreCase("")) {
//          
//      }
        Log.d("Alarm Receiver", "onReceive called");        
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(pContext).setSmallIcon(R.drawable.ic_logo).setContentTitle("Reminder Notification")
                        .setContentText("Reminder Notification");
        Intent resultIntent = new Intent(pContext, CalendarView.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(pContext);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(CalendarView.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) pContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notificationBuilder.build());
    }
}
