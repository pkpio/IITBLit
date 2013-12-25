package in.co.praveenkumar.iitblit.networking;

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

public class SendCode {
	private final String codeURL = "http://iitblit.praveenkumar.co.in/email/";
	private final String SUCCESS_CONTENT = "success";

	public Boolean sendCodeToServer(String ldap, String code) {
		Boolean status = false;
		String urlResp = "";

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(codeURL);

		try {
			// NameValuePairs for POST data holding
			List<NameValuePair> dataForUrl = new ArrayList<NameValuePair>();
			dataForUrl.add(new BasicNameValuePair("ldap", ldap));
			dataForUrl.add(new BasicNameValuePair("code", code));

			// Add the ansForUrl to HTTP post request
			httppost.setEntity(new UrlEncodedFormEntity(dataForUrl));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			InputStream is = response.getEntity().getContent();

			// Read content from stream
			String line = "";
			StringBuilder total = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			// Read response until the end
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			urlResp = total.toString();

			if (urlResp.contentEquals(SUCCESS_CONTENT))
				status = true;

		} catch (ClientProtocolException e) {
			status = false;
		} catch (IOException e) {
			status = false;
		}

		return status;
	}
}
