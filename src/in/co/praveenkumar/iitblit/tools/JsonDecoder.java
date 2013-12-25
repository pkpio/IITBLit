package in.co.praveenkumar.iitblit.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class JsonDecoder {
	private final String jsonFile = "/IITBLit/scores.json";
	private final String DEBUG_TAG = "JSON Decoder";

	// JSON Node names
	private static final String TAG_SEC_DEFAULT_START = "section";
	private static final String TAG_USER = "uname";
	private static final String TAG_SCORE = "score";

	// Json variables
	private JSONObject jObj = null;
	JSONArray[] section = new JSONArray[5];
	private String json = "";

	// ArrayList of ArrayList<String>.
	// Array of ArrayList<String> didn't work - Throws null pointer exception.
	// May be due to lack of proper ArrayList size allocation while creating
	// using array
	// Array of lists is also a bad practice. So ArrayList of ArrayList<String>
	// Below is equivalent to ArrayList<ArrayList<String>> uNames = new
	// ArrayList<ArrayList<String>>();
	// New class is just to keep things more clear and good looking :P
	ArrayList<StringList> uNames = new ArrayList<StringList>();
	ArrayList<StringList> sScores = new ArrayList<StringList>();

	public JsonDecoder() {
		File file = new File(Environment.getExternalStorageDirectory(),
				jsonFile);
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader reader = new BufferedReader(filereader);
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				// System.out.println(line);
			}
			reader.close();
			json = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		if (jObj != null)
			parseJsonToArray();
		else
			Log.d(DEBUG_TAG, "JSON object is null !");

	}

	private void parseJsonToArray() {
		try {
			// Getting Array of scores for each section
			for (int cat = 0; cat < 5; cat++) {
				section[cat] = jObj.getJSONArray(TAG_SEC_DEFAULT_START + cat);
				uNames.add(new StringList());
				sScores.add(new StringList());
			}

			// looping through all scores in each section
			for (int cat = 0; cat < 5; cat++) {
				for (int i = 0; i < section[cat].length(); i++) {
					// Each score object has username and score
					JSONObject s = section[cat].getJSONObject(i);
					// System.out.println(s.getString(TAG_USER));
					uNames.get(cat).add(s.getString(TAG_USER));
					sScores.get(cat).add(s.getString(TAG_SCORE));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public ArrayList<StringList> getSscores() {
		return sScores;
	}

	public ArrayList<StringList> getUnames() {
		return uNames;
	}

}
