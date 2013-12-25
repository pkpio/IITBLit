package in.co.praveenkumar.iitblit;

import android.app.Activity;
import android.os.Bundle;

public class MenuClickHandler extends Activity {
	private final int ABOUT = 1;
	private final int HELP = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		int menuItemSel = extras.getInt("menu_item");
		if (menuItemSel == ABOUT)
			setContentView(R.layout.about);
		else if (menuItemSel == HELP)
			setContentView(R.layout.about);

	}

}
