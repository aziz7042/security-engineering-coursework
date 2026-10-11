import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/** Demonstrates authenticated encryption with a fresh 96-bit nonce per message. */
public final class AesGcmDemo {
    private static final int NONCE_BYTES = 12;
    private static final int TAG_BITS = 128;
    private static final SecureRandom RANDOM = new SecureRandom();

    private AesGcmDemo() {}

    public record EncryptedMessage(byte[] nonce, byte[] ciphertext) {
        public EncryptedMessage {
            nonce = nonce.clone();
            ciphertext = ciphertext.clone();
        }

        @Override
        public byte[] nonce() {
            return nonce.clone();
        }

        @Override
        public byte[] ciphertext() {
            return ciphertext.clone();
        }
    }

    public static SecretKey generateKey() throws GeneralSecurityException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey();
    }

    public static EncryptedMessage encrypt(String plaintext, SecretKey key) throws GeneralSecurityException {
        byte[] nonce = new byte[NONCE_BYTES];
        RANDOM.nextBytes(nonce);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, nonce));
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return new EncryptedMessage(nonce, ciphertext);
    }

    public static String decrypt(EncryptedMessage message, SecretKey key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, message.nonce()));
        return new String(cipher.doFinal(message.ciphertext()), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        SecretKey key = generateKey();
        EncryptedMessage message = encrypt("Security and safety reinforce each other.", key);

        System.out.println("nonce: " + Base64.getEncoder().encodeToString(message.nonce()));
        System.out.println("ciphertext: " + Base64.getEncoder().encodeToString(message.ciphertext()));
        System.out.println("decrypted: " + decrypt(message, key));

        byte[] modified = message.ciphertext();
        modified[0] ^= 1;
        try {
            decrypt(new EncryptedMessage(message.nonce(), modified), key);
            throw new AssertionError("tampering was not detected");
        } catch (AEADBadTagException expected) {
            System.out.println("tamper check: authentication tag rejected the modified ciphertext");
        }

        Arrays.fill(modified, (byte) 0);
    }
}
