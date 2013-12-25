package in.co.praveenkumar.iitblit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Database {
	private static final String APP_SHARED_PREFS = "iitblit_preferences";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;

	// Preferences
	public Database(Context context) {
		this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.prefsEditor = appSharedPrefs.edit();
	}

	public String getAnswer(String qName) {
		return appSharedPrefs.getString(qName, "");
	}

	public String getLDAP() {
		return appSharedPrefs.getString("ldap", "");
	}

	public void saveAnswer(String qName, String aVal) {
		prefsEditor.putString(qName, aVal);
		prefsEditor.commit();
	}

	public void setLDAP(String ldap) {
		prefsEditor.putString("ldap", ldap);
		prefsEditor.commit();
	}
}