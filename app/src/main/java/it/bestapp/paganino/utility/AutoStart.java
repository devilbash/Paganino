package it.bestapp.paganino.utility;

import it.bestapp.paganino.utility.db.DataBaseAdapter;
import it.bestapp.paganino.utility.setting.SettingsManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class AutoStart extends BroadcastReceiver {

	private DataBaseAdapter dataBA = null;
	private HashMap<Integer, String> config = null;
	private Notifica notifica = new Notifica();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction() != null)
			if ((intent.getAction()
					.equals("android.intent.action.BOOT_COMPLETED")))
				scheduleAlarms(context);

	}
	
	
	public  static void scheduleAlarms(Context ctxt) {
		/*
		 * Devo recuperare dal db l'ora di avvio della notifica/archiviazione
		 * trasformarlo in millisecondi e preparare il service che scatterà
		 * all'ora tot.
		 */

		/*
		dataBA = new DataBaseAdapter(ctxt);
		dataBA.open();
		config = dataBA.getAllOtion();
		String day = config.get(Integer.parseInt(DataBaseAdapter.DAY));
		String ora = config.get(Integer.parseInt(DataBaseAdapter.TIME));
		dataBA.close();
		*/

		SingletonParametersBridge singleton = SingletonParametersBridge.getInstance();
		SettingsManager settings = (SettingsManager) singleton.getParameter("settings",(Activity) ctxt);
		int day = settings.getGiorno();
		String ora = settings.getOra();

		String time[] = ora.split(":");

		Calendar calendar = Calendar.getInstance();

		if  ( calendar.get(Calendar.DAY_OF_MONTH) > day )
			return;
		
		/*
		 *inserire condizione della notifica per il giorno stesso dell'avviso
		 *se gia suonato non deve suonare più 
		 */
		
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				day,
				Integer.parseInt(time[0]),
				Integer.parseInt(time[1]),
				00 );

		long milliseconds = calendar.getTimeInMillis();

		Intent intentAlarm = new Intent(ctxt, Notifica.class);
		AlarmManager alarmManager = (AlarmManager) ctxt
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, milliseconds,
				PendingIntent.getBroadcast(ctxt, 
						2323422, 
						intentAlarm, 
						0 ));

	}
}