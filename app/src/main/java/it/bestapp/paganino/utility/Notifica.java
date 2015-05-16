package it.bestapp.paganino.utility;

import it.bestapp.paganino.Main;
import it.bestapp.paganino.R;
import it.bestapp.paganino.utility.db.DataBaseAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class Notifica extends BroadcastReceiver {

	private DataBaseAdapter dataBA = null;
	private HashMap<Integer, String> config = null;

	private static final int SIMPLE_NOTIFICATION_ID = 1;
	NotificationManager mNotificationManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
	
		
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context);

		// Titolo e testo della notifica
		notificationBuilder.setContentTitle("Avviso Paganino");
		notificationBuilder.setContentText("Controlla la tua busta paga");

		// Testo che compare nella barra di stato non appena compare la notifica
		notificationBuilder.setTicker("Paganino");

		// Data e ora della notifica
		notificationBuilder.setWhen(System.currentTimeMillis());

		// Icona della notifica
		notificationBuilder.setSmallIcon(R.drawable.notifica);

		// Creiamo il pending intent che verrà lanciato quando la notifica
		// viene premuta
		Intent notificationIntent = new Intent(context, Main.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notificationBuilder.setContentIntent(contentIntent);

		// Impostiamo il suono, le luci e la vibrazione di default
		notificationBuilder.setDefaults(Notification.DEFAULT_SOUND
				| Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

		mNotificationManager.notify(SIMPLE_NOTIFICATION_ID,
				notificationBuilder.build());

		Toast.makeText(context, "sveglia!!!", Toast.LENGTH_LONG).show();
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(2000);
		
		wl.release();
	}
	
}