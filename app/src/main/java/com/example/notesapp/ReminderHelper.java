package com.example.notesapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ReminderHelper {

    private Context context;

    public ReminderHelper(Context context) {
        this.context = context;
    }

    // In ReminderHelper.java (corrected to match usage)
    public void setReminder(Calendar time, String title, String message) {
        // ... implementation ...

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("reminder_message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    time.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
}
