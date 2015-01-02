package in.co.praveenkumar.iitblit;

import in.co.praveenkumar.iitblit.networking.SendCode;
import in.co.praveenkumar.litiitb.R;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final String DEBUG_TAG = "Main Activity";
	public static final String baseURL = "http://praveenkumar.co.in/iitblit";
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;
	private final String VERIFIED = "isVerified";
	private EditText ldapView;
	private EditText codeView;
	private Button sendBtn;
	private Button verifyBtn;
	private final int ACTIVITY_QUIZ = 0;
	private final int ACTIVITY_ABOUT = 1;
	private final int ACTIVITY_HELP = 2;
	private final int ACTIVITY_TUTORIAL = 3;
	private final String GUEST = "guest";
	public String ldap = "";
	private Database db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing_page);

		// Close if needed.
		if (getIntent().getBooleanExtra("EXIT", false))
			finish();
		else {

			// Android in-built preferences for landing page only.
			appSharedPrefs = this.getSharedPreferences("IITBLit_landing_page",
					Activity.MODE_PRIVATE);

			// Check if user is already verified. If so, open quiz page
			if (appSharedPrefs.getBoolean(VERIFIED, false))
				openActivity(ACTIVITY_QUIZ);

			// Set views
			ldapView = (EditText) findViewById(R.id.ldap_id);
			codeView = (EditText) findViewById(R.id.verification_code);
			sendBtn = (Button) findViewById(R.id.send_code_btn);
			verifyBtn = (Button) findViewById(R.id.verify_code_btn);

			// For LDAP saving.
			db = new Database(getApplicationContext());

			sendBtn.setOnClickListener(sendCodeBtnListener);
			verifyBtn.setOnClickListener(verifyCodeBtnListener);

			// Create a directory for the app content
			File file = new File(Environment.getExternalStorageDirectory(),
					"/IITBLit/");
			if (!file.exists()) {
				if (!file.mkdirs()) {
					Log.d(DEBUG_TAG, "Problem creating course folder");
					Toast.makeText(getBaseContext(),
							"failed to create folder " + file,
							Toast.LENGTH_SHORT).show();
				}
			}

			// If the user hasn't gone through the tutorial already take him
			// there
			if (!db.isTutored()) {
				db.setTutored();
				openActivity(ACTIVITY_TUTORIAL);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			openActivity(ACTIVITY_ABOUT);
			break;
		case R.id.menu_help:
			openActivity(ACTIVITY_HELP);
			break;
		case R.id.menu_tutorial:
			openActivity(ACTIVITY_TUTORIAL);
			break;
		}
		return true;
	}

	// Onclick listeners
	private OnClickListener sendCodeBtnListener = new OnClickListener() {

		public void onClick(View v) {
			String ldapId = ldapView.getText().toString();
			if (ldapId.contentEquals(GUEST)) {
				db.setLDAP(GUEST);
				openActivity(ACTIVITY_QUIZ);
			} else if (!ldapId.contentEquals("")) {
				// Set ldapId from field to global ldap variable
				ldap = ldapId;

				// Change button content on send success
				sendBtn.setText("Sending..");
				sendBtn.setClickable(false);

				// Send code
				sendCodeToServer sc = new sendCodeToServer();
				sc.execute();
			}
		}
	};

	private OnClickListener verifyCodeBtnListener = new OnClickListener() {

		public void onClick(View v) {
			String ldapId = ldapView.getText().toString();
			String codeGvn = codeView.getText().toString();
			if (ldapId.contentEquals(GUEST)) {
				db.setLDAP(GUEST);
				openActivity(ACTIVITY_QUIZ);
			} else if (!ldapId.contentEquals("")) {
				// Set ldapId from field to global ldap variable
				ldap = ldapId;
				String actualCode = appSharedPrefs.getString(ldapId, "");

				// Verify codes
				if (!actualCode.contentEquals("")
						&& codeGvn.contentEquals(actualCode)) {
					// Update this in preferences
					prefsEditor = appSharedPrefs.edit();
					prefsEditor.putBoolean(VERIFIED, true);
					prefsEditor.apply();

					// Store ldap id in app db.
					db.setLDAP(ldapId);

					// Open quiz
					openActivity(ACTIVITY_QUIZ);
				} else
					verifyBtn.setText("Wrong ! Try again !");

			}
		}
	};

	private void openActivity(int item) {
		switch (item) {
		case ACTIVITY_QUIZ:
			Intent j = new Intent(this, QuizzingActivity.class);
			j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(j, 11);
			break;
		case ACTIVITY_ABOUT:
			Intent k = new Intent(this, MenuClickHandler.class);
			k.putExtra("menu_item", ACTIVITY_ABOUT);
			startActivityForResult(k, 11);
			break;
		case ACTIVITY_HELP:
			Intent l = new Intent(this, MenuClickHandler.class);
			l.putExtra("menu_item", ACTIVITY_HELP);
			startActivityForResult(l, 11);
			break;
		case ACTIVITY_TUTORIAL:
			Intent m = new Intent(this, Tutorial.class);
			startActivityForResult(m, 11);
			break;
		default:
			break;
		}

	}

	private class sendCodeToServer extends AsyncTask<String, Integer, Boolean> {
		Boolean sendStatus = false;
		private String code = "";

		@Override
		protected Boolean doInBackground(String... quesParam) {
			CodeGenerator cg = new CodeGenerator();
			code = cg.getCode();

			SendCode sc = new SendCode();
			sendStatus = sc.sendCodeToServer(ldap, code);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (sendStatus) {
				// If code sent successfully save code in db
				// Use ldap as code name while saving. Unique code per id
				prefsEditor = appSharedPrefs.edit();
				prefsEditor.putString(ldap, code);
				prefsEditor.commit();

				sendBtn.setText("Sent ! Resend ?");
			} else
				sendBtn.setText("Failed ! Resend ?");

			sendBtn.setClickable(true);
		}
	}

}
