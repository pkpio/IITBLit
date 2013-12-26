package in.co.praveenkumar.iitblit.networking;

import in.co.praveenkumar.iitblit.Quizzing.UIupdater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class SumbitAnswers {
	private String[][] answers = new String[4][4];
	private String ansURL = "http://www.iitblit.praveenkumar.co.in/evaluate/";
	private final String DEBUG_TAG = "Ans Submit";
	private Boolean status = false;
	private String urlResp = "";
	private String ldap = "";

	// For answer color evaluation on submit
	private Boolean[][] response = new Boolean[4][4];

	public SumbitAnswers(String[][] answers, String ldap) {
		this.answers = answers;
		this.ldap = ldap;
	}

	public void sync() {
		syncAnsScores sas = new syncAnsScores();
		sas.execute();
	}

	private class syncAnsScores extends AsyncTask<Integer, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Integer... quesParam) {
			Log.d(DEBUG_TAG, "Async thread started");
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(ansURL);

			try {
				// NameValuePairs for POST data holding
				List<NameValuePair> ansForUrl = new ArrayList<NameValuePair>();

				// Put all answers in NameValuePairs for POSTing
				ansForUrl.add(new BasicNameValuePair("ldap", ldap));
				for (int c = 0; c < 4; c++) {
					for (int q = 0; q < 4; q++) {
						ansForUrl.add(new BasicNameValuePair("c" + c + "q" + q,
								answers[c][q]));
					}
				}

				// Add the ansForUrl to HTTP post request
				httppost.setEntity(new UrlEncodedFormEntity(ansForUrl));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				InputStream is = response.getEntity().getContent();

				// Read content from stream
				String line = "";
				StringBuilder total = new StringBuilder();
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(is));

				// Read response until the end
				while ((line = rd.readLine()) != null) {
					total.append(line);
				}
				urlResp = total.toString();

				// Parse response to evaluate for correct answers
				evalBoolResp(urlResp);

				status = true;

				// Do a scores update now. User may be on top scores list.
				ScoresDownloader sd = new ScoresDownloader();
				sd.downloadScores();

			} catch (ClientProtocolException e) {
				status = false;
			} catch (IOException e) {
				status = false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (status) {
				UIupdater.scoresUIUpdate();
				UIupdater.ansUIUpdate(getBoolResp());
			}
		}
	}

	private void evalBoolResp(String stringResp) {
		char[] resp = stringResp.toCharArray();
		for (int c = 0; c < 4; c++) {
			for (int q = 0; q < 4; q++) {
				if (resp[c * 4 + q] == '1')
					response[c][q] = true;
				else
					response[c][q] = false;
			}
		}
	}

	private Boolean[][] getBoolResp() {
		return response;
	}
}
