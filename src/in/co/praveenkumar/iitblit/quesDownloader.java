package in.co.praveenkumar.iitblit;

import in.co.praveenkumar.iitblit.MainActivity.UIupdater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class quesDownloader {
	private final String DEBUG_TAG = "ScoresDownloader";
	private String serverURL = "http://home.iitb.ac.in/~praveendath92/IITBLit/";
	private String[][] quesURL = new String[4][4]; // Array with URLs to
													// questions

	public quesDownloader() {
		buildQuesURLs();
	}

	private void buildQuesURLs() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				quesURL[i][j] = serverURL + "cat" + i + "Ques" + j + ".jpg";
				quesURL[1][0] = "http://home.iitb.ac.in/~praveendath92/hits.png";
			}
		}
	}

	public Boolean downloadFiles() {
		Boolean downloadStatus = false;
		// 0 = failed (default), 1 = success
		Log.d(DEBUG_TAG, "File download requested");
		DownloadFiles downloadFiles = new DownloadFiles();
		downloadFiles.execute();
		return downloadStatus;
	}

	private class DownloadFiles extends AsyncTask<Integer, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... quesParam) {
			Log.d(DEBUG_TAG, "Async thread started");
			Boolean downloadStatus = false;
			for (int catNum = 0; catNum < 1; catNum++) {
				for (int quesNum = 0; quesNum < 1; quesNum++) {

					try {
						Log.d(DEBUG_TAG, "Download requested for Cat : "
								+ catNum + "Ques : " + quesNum);
						final URL url = new URL(quesURL[catNum][quesNum]);
						URLConnection connection = url.openConnection();
						connection.connect();

						// download the file
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"/IITBLit/" + "Cat" + catNum + "Ques" + quesNum
										+ ".jpg");
						InputStream input = new BufferedInputStream(
								url.openStream());
						OutputStream output = new FileOutputStream(file);

						byte data[] = new byte[1024];
						int count;
						while ((count = input.read(data)) != -1) {
							output.write(data, 0, count);
						}

						output.flush();
						output.close();
						input.close();
						downloadStatus = true;

					} catch (MalformedURLException e) {
						// URL malformed
						e.printStackTrace();
						Log.d(DEBUG_TAG, "Malformed URL");
					} catch (IOException e) {
						// Error while making connection
						e.printStackTrace();
						Log.d(DEBUG_TAG, "IOException ! Net problem ?");
					}
				}
			}

			return downloadStatus;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Log.d(DEBUG_TAG, "UI update requested");
			UIupdater.questionsUIUpdate();
		}
	}
}
