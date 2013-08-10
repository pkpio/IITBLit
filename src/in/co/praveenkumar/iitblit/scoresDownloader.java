package in.co.praveenkumar.iitblit;

import in.co.praveenkumar.iitblit.MainActivity.UIupdater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;

public class scoresDownloader {
	private final String DEBUG_TAG = "ScoresDownloader";
	private String serverScoresURL = "http://home.iitb.ac.in/~praveendath92/IITBLit/Scores/";

	public scoresDownloader() {

	}

	public void downloadScores() {

	}

	private class DownloadScores extends AsyncTask<Integer, Integer, Boolean> {
		StringBuilder content = new StringBuilder();

		@Override
		protected Boolean doInBackground(Integer... quesParam) {
			Boolean downloadStatus = false;
			try {
				Log.d(DEBUG_TAG, "Async thread started");
				final URL url = new URL(serverScoresURL);
				URLConnection connection = url.openConnection();
				connection.connect();

				BufferedReader input = new BufferedReader(
						new InputStreamReader(url.openStream()));
				String data;

				while ((data = input.readLine()) != null) {
					content.append(data + "\n");
				}

				input.close();
				downloadStatus = true;

			} catch (MalformedURLException e) {
				// URL malformed
				Log.d(DEBUG_TAG, "Malformed URL");
			} catch (IOException e) {
				// Error while making connection
				e.printStackTrace();
				Log.d(DEBUG_TAG, "IOException ! Net problem ?");
			}

			return downloadStatus;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Log.d(DEBUG_TAG, "UI update requested");
			UIupdater.scoresUIUpdate(content.toString());
		}
	}

}
