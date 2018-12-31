package usrportal.utils;

import java.security.SecureRandom;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils implements PasswordEncoder {

  public PasswordUtils() {}

  private String generateRandomSalt() {
    byte[] randBytes = new byte[28];
    SecureRandom csprng = new SecureRandom();
    csprng.nextBytes(randBytes);
    String salt = new String(Hex.encode(randBytes));
    return salt;
  }

  private String createDigest(String password, String salt) {
    SHA3.DigestSHA3 digestSHA3 = new SHA3.Digest512();
    digestSHA3.update(password.getBytes());
    digestSHA3.update(Hex.decode(salt));
    String hash = new String(Hex.encode(digestSHA3.digest()));
    return hash;
  }

  /**
   * Modular Crypt Format.
   * 
   * @param String base64 encoded digest
   * @param String base64 encoded salt
   * @return
   */
  private String createFormattedString(String digest, String salt) {
    return "$sha3-512" + "$na,1" + "$" + salt + "$" + digest;
  }

  @Override
  public String encode(CharSequence rawPassword) {
    String salt = generateRandomSalt();
    String digest = createDigest(rawPassword.toString(), salt);
    return createFormattedString(digest, salt);
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    String[] strArr = encodedPassword.split("\\$");
    String salt = strArr[3];
    String encodedPasswordDigest = strArr[4];
    String rawPassworDigest = createDigest(rawPassword.toString(), salt);
    return rawPassworDigest.equals(encodedPasswordDigest) ? true : false;
  }
}
