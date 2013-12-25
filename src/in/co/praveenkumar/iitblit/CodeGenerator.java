package in.co.praveenkumar.iitblit;

import java.util.Random;

public class CodeGenerator {
	private final int CODE_LENGTH = 7;
	private final String ALLOWED_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private Random rnd = new Random();

	public String getCode() {
		StringBuilder rc = new StringBuilder(CODE_LENGTH);
		for (int i = 0; i < CODE_LENGTH; i++)
			rc.append(ALLOWED_CHARS.charAt(rnd.nextInt(ALLOWED_CHARS.length())));
		return rc.toString();
	}
}
