package it.bestapp.paganino.utility;

import android.app.Activity;

import java.util.HashMap;

import it.bestapp.paganino.utility.setting.SettingsManager;

public class SingletonParametersBridge {
	private static SingletonParametersBridge instance = null;
	private HashMap<String, Object> map;

	public static SingletonParametersBridge getInstance() {
		if (instance == null)
			instance = new SingletonParametersBridge();
		return instance;
	}

	private SingletonParametersBridge() {
		map = new HashMap<String, Object>();
	}

	public void addParameter(String key, Object value) {
		map.put(key, value);
	}

	public Object getParameter(String key, Activity act) {
		Object obj = map.get(key);
		if (obj == null) {
			obj = new SettingsManager( act.getApplicationContext());
			addParameter(key, obj);
		}
		return obj;
	}

	public void removeParameter(String key) {
		map.remove(key);
	}
}