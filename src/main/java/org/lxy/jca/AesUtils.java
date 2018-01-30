package org.lxy.jca;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;

/**
 *
 * @Since 2017/12/21 14:08
 * @Auther XinyiLiu
 */
public class AesUtils {

    private static final String KEY_GENERATOR_ALGORITHM = "AES";
    private static final String Cipher_ALGORITHM = "AES/ECB/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * @see com.sun.crypto.provider.AESKeyGenerator
     * available values                 128, 192 or 256
     * SecretKeySpec keyBytes length    16    24    32
     * java 默认只能处理128位 local_policy.jar文件和US_export_policy.jar
     */
    private static final int LENGTH = 128;


    public static String generateSecretKeyString() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_GENERATOR_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            keyGenerator.init(LENGTH, new SecureRandom());
            return Base64.encodeBase64String(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(" generateBase64 failed with message:" + e.getMessage());
        }
    }

    public static String encrypt(String source, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(Cipher_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, base64ToSecretKey(secretKey));
            byte[] encrypt = cipher.doFinal(source.getBytes());
            return Base64.encodeBase64String(encrypt);
        } catch (Exception e) {
            throw new RuntimeException(" encryptByBase64 failed with message:" + e.getMessage());
        }
    }

    public static String decode(String source, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance(Cipher_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, base64ToSecretKey(secretKey));
            byte[] decrypt = cipher.doFinal(Base64.decodeBase64(source));
            return new String(decrypt);
        } catch (Exception e) {
            throw new RuntimeException(" decodeByBase64 failed with message:" + e.getMessage());
        }
    }

    public static SecretKeySpec base64ToSecretKey(String base64SecretKey) {
        return new SecretKeySpec(Base64.decodeBase64(base64SecretKey), KEY_GENERATOR_ALGORITHM);
    }
}
