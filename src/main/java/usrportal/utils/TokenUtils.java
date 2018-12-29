package usrportal.utils;

import java.security.SecureRandom;

import org.bouncycastle.util.encoders.Hex;

public class TokenUtils {

	public String generateSecretToken() {
		byte[] randBytes = new byte[64];
		SecureRandom csprng = new SecureRandom();
		csprng.nextBytes(randBytes);
		String secretToken = new String(Hex.encode(randBytes));
		return secretToken;
	}
}
