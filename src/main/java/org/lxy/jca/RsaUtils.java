package org.lxy.jca;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @Since 2017/12/21 14:30
 * @Auther XinyiLiu
 *
 * 比如 Java 默认的 RSA 加密实现不允许明文长度超过密钥长度减去 11(单位是字节，也就是 byte)。
 * 也就是说，如果我们定义的密钥(我们可以通过 java.security.KeyPairGenerator.initialize(int keysize) 来定义密钥长度)
 * 长度为 1024(单位是位，也就是 bit)，生成的密钥长度就是 1024位 / 8位/字节 = 128字节，
 * 那么我们需要加密的明文长度不能超过 128字节 -11 字节 = 117字节。也就是说，我们最大能将 117 字节长度的明文进行加密。
 *
 */
@SuppressWarnings(value = "all")
public class RsaUtils {

    private RsaUtils() {
    }

    /**
     * for now bouncycastle KeyFactory and KeyPairGenerator only support RSA
     * @see org.bouncycastle.jcajce.provider.asymmetric.RSA
     */
    private static final String DEFAULT_KEY_PAIR_ALGORITHM = "RSA";

    private static final String DEFAULT_KEY_FACTORY_ALGORITHM = "RSA";

    public static final String DEFAULT_CIPHER_ALGORITHM = "RSA/ECB/OAEPWithMD5AndMGF1Padding";
    /**
     * for more sign ALGORITHM
     * @see org.bouncycastle.jcajce.provider.asymmetric.RSA
     */
    private static final String DEFAULT_SIGN_ALGORITHM = "SHA256withRSA";

    private static final int DEFAULT_KEY_SIZE = 1024;

    private static final int ENCRYPT_LENGTH = 64;

    private static final int DECRYPT_LENGTH = ENCRYPT_LENGTH * 2;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * DEFAULT_KEY_PAIR_ALGORITHM
     * @return
     */
    public static RsaBase64KeyPair generateKeyPairString() {
        return generateKeyPairString(DEFAULT_KEY_PAIR_ALGORITHM);
    }

    public static RsaBase64KeyPair generateKeyPairString(String keyPairAlgorithm) {
        RsaBase64KeyPair rsaBase64KeyPair = new RsaBase64KeyPair();
        KeyPair keyPair = generateKeyPair(keyPairAlgorithm);
        rsaBase64KeyPair.setBase64Public(Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
        rsaBase64KeyPair.setBase64Private(Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
        return rsaBase64KeyPair;
    }

    /**
     * generateKeyPair
     * @return
     */
    public static KeyPair generateKeyPair() {
        return generateKeyPair(DEFAULT_KEY_PAIR_ALGORITHM);
    }

    public static KeyPair generateKeyPair(String keyPairAlgorithm) {
        try {
            KeyPairGenerator keyPairGenerator =
                    KeyPairGenerator.getInstance(keyPairAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            keyPairGenerator.initialize(DEFAULT_KEY_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(" generateKeyPair failed with message:" + e.getMessage());
        }
    }

    /**
     * DEFAULT_KEY_FACTORY_ALGORITHM
     * @param base64PublicKeyString
     * @return
     */
    public static RSAPublicKey getPublicKey(String base64PublicKeyString) {
        return getPublicKey(base64PublicKeyString, DEFAULT_KEY_FACTORY_ALGORITHM);
    }

    public static RSAPublicKey getPublicKey(String base64PublicKeyString, String keyFactoryAlgorithm) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance(keyFactoryAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            return (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(" getPublicKey failed with message:" + e.getMessage());
        }
    }

    /**
     * DEFAULT_KEY_FACTORY_ALGORITHM
     * @param base64PrivateKeyString
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String base64PrivateKeyString) {
        return getPrivateKey(base64PrivateKeyString, DEFAULT_KEY_FACTORY_ALGORITHM);
    }

    public static RSAPrivateKey getPrivateKey(String base64PrivateKeyString, String keyFactoryAlgorithm) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(keyFactoryAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKeyString));
            return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(" getPrivateKey failed with message:" + e.getMessage());
        }
    }

    /**
     * 公钥加密
     *
     * @param source
     * @param publicKey
     * @return
     */
    public static String encrypt(String source, PublicKey publicKey) {
        return encrypt(source, publicKey, DEFAULT_CIPHER_ALGORITHM);
    }

    public static String encrypt(String source, PublicKey publicKey, String cipherAlgorithm) {
        try {
            final Cipher cipher = Cipher.getInstance(cipherAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] sourceBytes = source.getBytes("UTF-8");
            /** 分段加密 */
            byte[] encryptBytes = null;
            for (int i = 0; i < sourceBytes.length; i += ENCRYPT_LENGTH) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(sourceBytes, i, i + ENCRYPT_LENGTH));
                encryptBytes = ArrayUtils.addAll(encryptBytes, doFinal);
            }
            return Base64.encodeBase64String(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException("encrypt failed with message:" + e.getMessage());
        }
    }

    /**
     * 私钥解密
     * @param encryptString
     * @param privateKey
     * @return
     */
    public static String decrypt(String encryptString, PrivateKey privateKey) {
        return decrypt(encryptString, privateKey, DEFAULT_CIPHER_ALGORITHM);
    }

    public static String decrypt(String encryptString, PrivateKey privateKey, String cipherAlgorithm) {
        try {
            final Cipher cipher = Cipher.getInstance(cipherAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] encryptBytes = Base64.decodeBase64(encryptString);
            byte[] decryptBytes = null;
            for (int i = 0; i < encryptBytes.length; i += DECRYPT_LENGTH) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(encryptBytes, i, i + DECRYPT_LENGTH));
                decryptBytes = ArrayUtils.addAll(decryptBytes, doFinal);
            }
            return new String(decryptBytes);
        } catch (Exception e) {
            throw new RuntimeException("decrypt failed with message:" + e.getMessage());
        }
    }

    /**
     * 私钥签名
     * @param source
     * @param privateKey
     * @return
     */

    public static String signHex(String source, PrivateKey privateKey) {
        return signHex(source, privateKey, DEFAULT_SIGN_ALGORITHM);
    }

    public static String signHex(String source, PrivateKey privateKey, String signAlgorithm) {
        try {
            Signature signature = Signature.getInstance(signAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            signature.initSign(privateKey);
            signature.update(source.getBytes());
            byte[] sign = signature.sign();
            return Hex.encodeHexString(sign);
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate RSA signature. " + e.getMessage(), e);
        }
    }

    /**
     * 公钥验签
     * @param source
     * @param publicKey
     * @param sign
     * @return
     */
    public static boolean verifyHexSign(String source, PublicKey publicKey, String sign) {
        return verifyHexSign(source, publicKey, sign, DEFAULT_SIGN_ALGORITHM);
    }

    public static boolean verifyHexSign(String source, PublicKey publicKey, String sign, String signAlgorithm) {
        try {
            Signature signature = Signature.getInstance(signAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            signature.initVerify(publicKey);
            signature.update(source.getBytes());
            return signature.verify(Hex.decodeHex(sign.toCharArray()));
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate RSA signature. " + e.getMessage(), e);
        }
    }
}
