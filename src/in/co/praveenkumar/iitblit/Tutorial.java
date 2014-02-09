package in.co.praveenkumar.iitblit;

import in.co.praveenkumar.litiitb.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Tutorial extends Activity {
	private final String LDAP_VERIFY_BTN_TEXT = "Alrighty!";
	private final String APP_USAGE_BTN_TEXT = "I think I got it.";
	private final String REFER_TUT_BTN_TEXT = "Enough! Now let me in!";

	private LinearLayout AccessToApp;
	private LinearLayout LDAPVerify;
	private LinearLayout AppUsage;
	private LinearLayout ReferTut;

	private Button NextBtn;

	private int pos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);

		// Assign widgets
		AccessToApp = (LinearLayout) findViewById(R.id.tut_access_info);
		LDAPVerify = (LinearLayout) findViewById(R.id.tut_ldap_verify_info);
		AppUsage = (LinearLayout) findViewById(R.id.tut_app_usage_info);
		ReferTut = (LinearLayout) findViewById(R.id.tut_refer_tut_info);

		NextBtn = (Button) findViewById(R.id.tut_btn);
		NextBtn.setOnClickListener(nextListener);
	}

	private OnClickListener nextListener = new OnClickListener() {
		public void onClick(View v) {
			switch (pos) {
			case 0:
				AccessToApp.setVisibility(LinearLayout.GONE);
				NextBtn.setText(LDAP_VERIFY_BTN_TEXT);
				pos = 1;
				break;
			case 1:
				LDAPVerify.setVisibility(LinearLayout.GONE);
				NextBtn.setText(APP_USAGE_BTN_TEXT);
				pos = 2;
				break;
			case 2:
				AppUsage.setVisibility(LinearLayout.GONE);
				NextBtn.setText(REFER_TUT_BTN_TEXT);
				pos = 3;
				break;
			case 3:
				ReferTut.setVisibility(LinearLayout.GONE);
				Intent l = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivityForResult(l, 11);
				break;

			default:
				break;
			}

		}
	};

}
