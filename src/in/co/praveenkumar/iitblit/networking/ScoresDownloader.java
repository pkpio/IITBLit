package in.co.praveenkumar.iitblit.networking;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.util.Log;

public class ScoresDownloader {
	private final String DEBUG_TAG = "ScoresDownloader";
	private String scoresURL = "http://www.iitblit.praveenkumar.co.in/scores/";

	public ScoresDownloader() {

	}

	public Boolean downloadScores() {
		Boolean downStatus = false;
		// 0 = failed (default), 1 = success
		Log.d(DEBUG_TAG, "Scores download requested");
		try {
			final URL url = new URL(scoresURL);
			URLConnection connection = url.openConnection();
			connection.connect();

			// download the file
			File file = new File(Environment.getExternalStorageDirectory(),
					"/IITBLit/scores.json");
			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(file);

			byte data[] = new byte[1024];
			int count;
			while ((count = input.read(data)) != -1) {
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
			downStatus = true;

		} catch (MalformedURLException e) {
			// URL malformed
			e.printStackTrace();
			Log.d(DEBUG_TAG, "Malformed URL");
		} catch (IOException e) {
			// Error while making connection
			e.printStackTrace();
			Log.d(DEBUG_TAG, "IOException ! Net problem ?");
		}

		return downStatus;
	}

}
