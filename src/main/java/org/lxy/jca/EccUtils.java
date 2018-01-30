package org.lxy.jca;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @Since 2017/12/22 11:45
 * @Auther XinyiLiu
 */
@SuppressWarnings(value = "all")
public class EccUtils {

    /**
     * @see org.bouncycastle.jcajce.provider.asymmetric.EC
     * @see org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator
     */
    private static final String DEFAULT_KEY_PAIR_ALGORITHM = "ECIES";

    private static final String DEFAULT_KEY_FACTORY_ALGORITHM = "ECDH";

    /**
     * no_use Chipher不支持EC算法 未能实现
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "ECIESwithDESede/NONE/PKCS7Padding";

    /**
     * @see org.bouncycastle.eac.operator.jcajce.EACHelper
     */
    private static final String DEFAULT_SIGN_ALGORITHM = "SHA256withECDSA";

    public static KeyPair generateKeyPair() {
        return generateKeyPair(DEFAULT_KEY_PAIR_ALGORITHM);
    }

    public static KeyPair generateKeyPair(String keyPairAlgorithm) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyPairGenerator =
                    KeyPairGenerator.getInstance(DEFAULT_KEY_PAIR_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            keyPairGenerator.initialize(192);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(" generateKeyPair failed with message:" + e.getMessage());
        }
    }

    public static EccBase64KeyPair generateKeyPairString() {
        return generateKeyPairString(DEFAULT_KEY_PAIR_ALGORITHM);
    }

    public static EccBase64KeyPair generateKeyPairString(String keyPariAlgorithm) {
        KeyPair keyPair = generateKeyPair(keyPariAlgorithm);
        EccBase64KeyPair eccBase64KeyPair = new EccBase64KeyPair();
        PublicKey publicKey = keyPair.getPublic();
        eccBase64KeyPair.setBase64Public(Base64.encodeBase64String(publicKey.getEncoded()));
        PrivateKey privateKey = keyPair.getPrivate();
        eccBase64KeyPair.setBase64Private(Base64.encodeBase64String(privateKey.getEncoded()));
        eccBase64KeyPair.setKeyFactoryAlgorithm(DEFAULT_KEY_FACTORY_ALGORITHM);
        return eccBase64KeyPair;
    }

    /**
     * 公钥加密
     *
     * @param source
     * @param ecPublicKey
     * @return
     */
    public static String encrypt(String source, ECPublicKey ecPublicKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            // Chipher不支持EC算法 未能实现
            final Cipher cipher = new NullCipher();
            cipher.init(Cipher.ENCRYPT_MODE, ecPublicKey, ecPublicKey.getParameters());
            byte[] sourceBytes = source.getBytes("UTF-8");
            /** 分段加密 */
            byte[] encryptBytes = cipher.doFinal(sourceBytes);
            return Base64.encodeBase64String(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException("encrypt failed with message:" + e.getMessage());
        }
    }

    /**
     * 私钥解密
     * @param encryptString
     * @param ecPrivateKey
     * @return
     */
    public static String decrypt(String encryptString, ECPrivateKey ecPrivateKey) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            // Chipher不支持EC算法 未能实现
            final Cipher cipher = new NullCipher();
            cipher.init(Cipher.DECRYPT_MODE, ecPrivateKey, ecPrivateKey.getParameters());
            byte[] encryptBytes = Base64.decodeBase64(encryptString);
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes);
        } catch (Exception e) {
            throw new RuntimeException("decrypt failed with message:" + e.getMessage());
        }
    }

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
            throw new RuntimeException("cannot generate ECC signature. " + e.getMessage(), e);
        }
    }

    public static boolean verifyHexSign(String source, PublicKey publicKey, String sign) {
        return verifyHexSign(source, publicKey, sign, DEFAULT_SIGN_ALGORITHM);
    }

    public static boolean verifyHexSign(String source, PublicKey publicKey, String sign, String signAlgorithm) {
        try {
            Signature signature = Signature.getInstance(DEFAULT_SIGN_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            signature.initVerify(publicKey);
            signature.update(source.getBytes());
            return signature.verify(Hex.decodeHex(sign.toCharArray()));
        } catch (Exception e) {
            throw new RuntimeException("cannot generate verify ecc signature. " + e.getMessage(), e);
        }
    }

    public static ECPublicKey getPublicKey(String base64PublicKeyString) {
        return getPublicKey(base64PublicKeyString, DEFAULT_KEY_FACTORY_ALGORITHM);
    }

    public static ECPublicKey getPublicKey(String base64PublicKeyString, String keyFactoryAlgorithm) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance(keyFactoryAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (Exception e) {
            throw new RuntimeException(" getPublicKey failed with message:" + e.getMessage());
        }
    }

    public static ECPrivateKey getPrivateKey(String base64PriateKeyString) {
        return getPrivateKey(base64PriateKeyString, DEFAULT_KEY_FACTORY_ALGORITHM);
    }

    public static ECPrivateKey getPrivateKey(String base64PriateKeyString, String keyFactoryAlgorithm) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance(keyFactoryAlgorithm, BouncyCastleProvider.PROVIDER_NAME);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PriateKeyString));
            ECPrivateKey privateKey = (ECPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (Exception e) {
            throw new RuntimeException(" getPrivateKey failed with message:" + e.getMessage());
        }
    }
}
