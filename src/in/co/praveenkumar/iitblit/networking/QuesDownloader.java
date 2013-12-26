package in.co.praveenkumar.iitblit.networking;

import in.co.praveenkumar.iitblit.Quizzing.UIupdater;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
import android.util.Log;

public class QuesDownloader {
	private final String DEBUG_TAG = "quesDownloader";
	private String quesBaseURL = "http://www.iitblit.praveenkumar.co.in/questions/";
	private String quesStatusURL = "http://www.iitblit.praveenkumar.co.in/questions/getQsStatus";
	private String[][] quesURL = new String[4][4]; // Array with URLs to
													// questions
	private Boolean[][] qStatus = new Boolean[4][4];
	private String qStatusStrng;

	public QuesDownloader() {
		buildQuesURLs();
	}

	private void buildQuesURLs() {
		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 5; j++) {
				quesURL[i - 1][j - 1] = quesBaseURL + "c" + i + "q" + j
						+ ".jpg";
			}
		}
	}

	public Boolean downQues() {
		Boolean downStatus = false;
		// 0 = failed (default), 1 = success
		Log.d(DEBUG_TAG, "File download requested");
		for (int catNum = 0; catNum < 4; catNum++) {
			for (int quesNum = 0; quesNum < 4; quesNum++) {
				UIupdater.updateQsDownCountUI(catNum * 4 + quesNum);

				try {
					Log.d(DEBUG_TAG, "Download requested for Cat : " + catNum
							+ "Ques : " + quesNum);
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
			}
		}

		return downStatus;
	}

	public Boolean downQStatus() {
		Log.d(DEBUG_TAG, "Question availability check requested");
		String output = "";
		Boolean downStatus = false;

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(quesStatusURL);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			output = EntityUtils.toString(httpEntity);
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

		if (!output.contentEquals("") && output.length() == 16) {
			qStatusStrng = output;
			char[] resp = output.toCharArray();
			for (int c = 0; c < 4; c++) {
				for (int q = 0; q < 4; q++) {
					if (resp[c * 4 + q] == '1')
						qStatus[c][q] = true;
					else
						qStatus[c][q] = false;
				}
			}
			downStatus = true;
		}

		return downStatus;

	}

	public Boolean[][] getAvailQStatus() {
		return qStatus;
	}

	public String getAvailQStatusStrng() {
		return qStatusStrng;
	}
}
