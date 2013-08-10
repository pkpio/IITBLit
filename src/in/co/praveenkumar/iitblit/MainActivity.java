package in.co.praveenkumar.iitblit;

import java.io.File;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public static View[] sectionRootView = new View[4];
	private final String DEBUG_TAG = "IITBLit.MainActivity";
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Start a background thread that downloads files in the bg
		// The newly obtained will be pushed to UI thread after each image
		// download
		quesDownloader qDown = new quesDownloader();
		qDown.downloadFiles();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private final String DEBUG_TAG = "IITBLit.DummySectionFragment";
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
			View rootView = inflater.inflate(R.layout.data_display, container,
					false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.question1Heading);
			switch (sectionNum) {
			case 1:
				Log.d(DEBUG_TAG, "Creating scetion 1");
				dummyTextView.setText("Question 1 Section 1"
						+ Integer.toString(getArguments().getInt(
								ARG_SECTION_NUMBER)));
				sectionRootView[0] = rootView;
				UIupdater.questionsUIUpdate();
				break;
			case 2:
				Log.d(DEBUG_TAG, "Creating scetion 1");
				dummyTextView.setText("Question 1 Section 2"
						+ Integer.toString(getArguments().getInt(
								ARG_SECTION_NUMBER)));
				sectionRootView[1] = rootView;
				UIupdater.questionsUIUpdate();
				break;
			case 3:
				Log.d(DEBUG_TAG, "Creating scetion 1");
				dummyTextView.setText("Question 1 Section 3"
						+ Integer.toString(getArguments().getInt(
								ARG_SECTION_NUMBER)));
				sectionRootView[2] = rootView;
				UIupdater.questionsUIUpdate();
				break;
			case 4:
				Log.d(DEBUG_TAG, "Creating scetion 1");
				dummyTextView.setText("Question 1 Section 4"
						+ Integer.toString(getArguments().getInt(
								ARG_SECTION_NUMBER)));
				sectionRootView[3] = rootView;
				UIupdater.questionsUIUpdate();
				break;

			}

			return rootView;
		}
	}

	public static class UIupdater {
		private final static String DEBUG_TAG = "IITBLit.UIUpdater";

		public UIupdater() {
			// Constructor does nothing
		}

		public static void questionsUIUpdate() {
			for (int catNum = 0; catNum < 4; catNum++) {
				for (int quesNum = 0; quesNum < 4; quesNum++) {
					File imgFile = new File(
							Environment.getExternalStorageDirectory(),
							"/IITBLit/" + "Cat" + catNum + "Ques" + quesNum
									+ ".jpg");
					System.out.println("File checking" + catNum + quesNum);
					if (imgFile.exists() && sectionRootView[catNum] != null) {
						System.out.println("File exists");
						Bitmap myBitmap = BitmapFactory.decodeFile(imgFile
								.getAbsolutePath());
						Log.d(DEBUG_TAG, "Updating UI for Cat : " + catNum
								+ " Ques : " + quesNum);
						switch (quesNum) {
						case 0:
							ImageView ques1View = (ImageView) sectionRootView[catNum]
									.findViewById(R.id.q1ImgView);
							ques1View.setImageBitmap(myBitmap);
							break;
						case 1:
							ImageView ques2View = (ImageView) sectionRootView[catNum]
									.findViewById(R.id.q2ImgView);
							ques2View.setImageBitmap(myBitmap);
							break;
						case 2:
							ImageView ques3View = (ImageView) sectionRootView[catNum]
									.findViewById(R.id.q3ImgView);
							ques3View.setImageBitmap(myBitmap);
							break;
						case 3:
							ImageView ques4View = (ImageView) sectionRootView[catNum]
									.findViewById(R.id.q4ImgView);
							ques4View.setImageBitmap(myBitmap);
							break;

						}
					}
				}
			} // End of loop code
		}

		public static void scoresUIUpdate(String scoresString) {
			
		}

	}

	// End of UI updation class

}
