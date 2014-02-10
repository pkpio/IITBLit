package in.co.praveenkumar.iitblit.tools;

import java.util.ArrayList;

public class scoreSorter {

	private ArrayList<String> uNames;
	private ArrayList<String> scores;

	public scoreSorter(ArrayList<String> uNames, ArrayList<String> scores) {
		this.uNames = uNames;
		this.scores = scores;
	}

	public void sort() {
		// We will be doing a normal sort
		// Worst case: O(N^2)
		// Best case: O(N)
		for (int i = 0; i < scores.size(); i++) {

			int curMinIndex = i;
			int curMinVal = Integer.parseInt(scores.get(i));
			String curMinValUser = uNames.get(i);

			for (int j = i; j < scores.size(); j++) {

				int curVal = Integer.parseInt(scores.get(j));
				String curValUser = uNames.get(j);

				// Find a new minimum ?
				if (curVal > curMinVal) {
					// Exchange positions with current minimum
					scores.set(curMinIndex, curVal + "");
					uNames.set(curMinIndex, curValUser);
					scores.set(j, curMinVal + "");
					uNames.set(j, curMinValUser);
					curMinVal = curVal;
				}

			}
		}
	}

	public void setuNames(ArrayList<String> uNames) {
		this.uNames = uNames;
	}

	public void setScores(ArrayList<String> scores) {
		this.scores = scores;
	}

	public ArrayList<String> getSorteduNames() {
		return uNames;
	}

	public ArrayList<String> getSortedScores() {
		return scores;
	}

}
