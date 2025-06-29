package com.webb.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.crypto.digests.SM3Digest;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.security.SecureRandom; // 仅用于临时生成KEY/IV的main方法

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoUtils {

    private static final Logger logger = LoggerFactory.getLogger(CryptoUtils.class);
    private static final String ALGORITHM_SM4 = "SM4"; // 用于KeyGenerator和SecretKeySpec
    private static final String SM4_CIPHER_ALGORITHM = "SM4/CBC/PKCS5Padding"; // BouncyCastle Provider支持PKCS5Padding作为PKCS7Padding的别名

    private static final String FIXED_SM4_KEY_HEX = "725b22f9742be37755b322523f16d103"; // 必须是32个十六进制字符 (16字节)
    private static final String FIXED_SM4_IV_HEX = "eb43b69cd9fd4c94484003433ef64d83";   // 必须是32个十六进制字符 (16字节)

    public static final byte[] SM4_KEY;
    public static final byte[] SM4_IV;

    static {
        // 动态添加 BouncyCastle Provider，确保它被正确加载和注册
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
            logger.info("BouncyCastle Provider 已添加并注册。");
        } else {
            logger.info("BouncyCastle Provider 已存在。");
        }

        try {
            SM4_KEY = Hex.decode(FIXED_SM4_KEY_HEX);
            SM4_IV = Hex.decode(FIXED_SM4_IV_HEX);
            logger.info("SM4 密钥和IV已从固定的Hex字符串加载。");
            // 为了安全，不在生产日志中打印密钥或IV本身
            if (SM4_KEY.length != 16 || SM4_IV.length != 16) {
                throw new IllegalArgumentException("SM4密钥或IV长度必须是16字节 (32个十六进制字符)。");
            }
        } catch (Exception e) {
            logger.error("从固定的Hex字符串初始化SM4密钥或IV失败! 请检查HEX字符串的格式和长度。", e);
            throw new RuntimeException("SM4密钥/IV初始化失败", e);
        }
    }

    /**
     * 计算字符串的 SM3 哈希值
     * @param input 输入字符串
     * @return SM3 哈希值的十六进制表示
     */
    public static String sm3Hash(String input) {
        if (input == null) {
            return null;
        }
        try {
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            SM3Digest digest = new SM3Digest();
            digest.update(inputBytes, 0, inputBytes.length);
            byte[] hash = new byte[digest.getDigestSize()];
            digest.doFinal(hash, 0);
            return Hex.toHexString(hash);
        } catch (Exception e) {
            // 避免在日志中记录完整的输入内容，以防包含敏感信息
            logger.error("SM3 哈希计算失败 for input (first 10 chars): {}",
                    input.substring(0, Math.min(input.length(), 10)) + (input.length() > 10 ? "..." : ""), e);
            return null;
        }
    }

    /**
     * SM4 CBC模式加密 (使用类静态固定的密钥和IV)
     * @param plainText 明文字符串
     * @return 加密后数据的十六进制字符串，如果加密失败则返回null
     */
    public static String sm4EncryptHex(String plainText) {
        if (plainText == null) return null;
        try {
            byte[] encryptedBytes = sm4Encrypt(plainText.getBytes(StandardCharsets.UTF_8), SM4_KEY, SM4_IV);
            if (encryptedBytes == null) return null;
            return Hex.toHexString(encryptedBytes);
        } catch (Exception e) {
            logger.error("SM4 (Hex) 加密失败 for plaintext (first 10 chars): {}",
                    plainText.substring(0, Math.min(plainText.length(),10)) + (plainText.length() > 10 ? "..." : ""), e);
            return null;
        }
    }

    /**
     * SM4 CBC模式解密 (使用类静态固定的密钥和IV)
     * @param hexCipherText 十六进制表示的密文
     * @return 解密后的明文字符串，如果解密失败则返回null
     */
    public static String sm4DecryptHex(String hexCipherText) {
        if (hexCipherText == null) return null;
        try {
            byte[] cipherTextBytes = Hex.decode(hexCipherText);
            byte[] decryptedBytes = sm4Decrypt(cipherTextBytes, SM4_KEY, SM4_IV);
            if (decryptedBytes == null) return null;
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) { // 包括Hex解码异常和解密异常
            logger.error("SM4 (Hex) 解密失败 for ciphertext (first 10 chars): {}",
                    hexCipherText.substring(0, Math.min(hexCipherText.length(),10)) + (hexCipherText.length() > 10 ? "..." : ""), e);
            return null;
        }
    }

    /**
     * SM4 CBC模式加密 (返回byte数组)
     * @param plainBytes 明文byte数组
     * @param key 16字节密钥
     * @param iv 16字节IV
     * @return 加密后的byte数组，如果加密失败则返回null
     */
    public static byte[] sm4Encrypt(byte[] plainBytes, byte[] key, byte[] iv) {
        if (plainBytes == null || key == null || iv == null) {
            logger.warn("SM4加密参数不合法：plainBytes, key, or iv 为空。");
            return null;
        }
        if (key.length != 16 || iv.length != 16) {
            logger.warn("SM4密钥或IV长度不正确，必须为16字节。Key length: {}, IV length: {}", key.length, iv.length);
            return null;
        }

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM_SM4);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // 明确指定使用 BouncyCastle Provider
            Cipher cipher = Cipher.getInstance(SM4_CIPHER_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(plainBytes);
        } catch (Exception e) {
            logger.error("SM4 加密失败 (byte[]): ", e);
            return null;
        }
    }

    /**
     * SM4 CBC模式解密 (输入byte数组)
     * @param cipherBytes 密文byte数组
     * @param key 16字节密钥
     * @param iv 16字节IV (必须是加密时使用的同一个IV)
     * @return 解密后的byte数组，如果解密失败则返回null
     */
    public static byte[] sm4Decrypt(byte[] cipherBytes, byte[] key, byte[] iv) {
        if (cipherBytes == null || key == null || iv == null) {
            logger.warn("SM4解密参数不合法：cipherBytes, key, or iv 为空。");
            return null;
        }
        if (key.length != 16 || iv.length != 16) {
            logger.warn("SM4密钥或IV长度不正确，必须为16字节。Key length: {}, IV length: {}", key.length, iv.length);
            return null;
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM_SM4);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // 明确指定使用 BouncyCastle Provider
            Cipher cipher = Cipher.getInstance(SM4_CIPHER_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(cipherBytes);
        } catch (Exception e) {
            // 在解密失败时，不记录密文本身，以防日志泄露敏感信息片段
            logger.error("SM4 解密失败 (byte[]): {}", e.getMessage()); // 只记录异常信息
            return null;
        }
    }

    /**
     * 辅助main方法，用于生成一次性的SM4密钥和IV的Hex字符串。
     * 生成后，请将这些值手动复制到上面的 FIXED_SM4_KEY_HEX 和 FIXED_SM4_IV_HEX 常量中。
     * 【【不要在生产代码中保留此main方法的调用或使其可公开访问。】】
     */
    public static void main(String[] args) {
        // 确保BouncyCastle Provider已添加，对于独立运行main方法可能需要
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        System.out.println("--- SM3 哈希测试 ---");
        String originalText = "这是一段需要加密的敏感信息12345";
        System.out.println("原始文本: " + originalText);
        String sm3Hashed = sm3Hash(originalText);
        System.out.println("SM3 哈希: " + sm3Hashed);
        String sm3PasswordHash = sm3Hash("Admin@123");
        System.out.println("SM3 密码哈希 for 'Admin@123': " + sm3PasswordHash);
        System.out.println("SM3 密码哈希 for 'Admin': " + sm3Hash("Admin"));


        System.out.println("\n--- 用于生成固定SM4密钥和IV的辅助代码 ---");
        try {
            // 生成SM4密钥
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM_SM4, BouncyCastleProvider.PROVIDER_NAME);
            keyGen.init(128); // SM4密钥长度为128位 (16字节)
            SecretKey secretKey = keyGen.generateKey();
            System.out.println("请将此SM4密钥Hex值用于 FIXED_SM4_KEY_HEX: " + Hex.toHexString(secretKey.getEncoded()));

            // 生成SM4 IV (初始化向量)
            byte[] ivBytes = new byte[16]; // SM4的块大小是128位，所以IV也是16字节
            SecureRandom random = new SecureRandom();
            random.nextBytes(ivBytes);
            System.out.println("请将此SM4 IV Hex值用于 FIXED_SM4_IV_HEX:  " + Hex.toHexString(ivBytes));
        } catch (Exception e) {
            logger.error("生成SM4密钥/IV辅助代码执行失败", e);
        }

        System.out.println("\n--- 使用当前类中固定KEY/IV的SM4加解密测试 ---");
        // 使用类中定义的 FIXED_SM4_KEY_HEX 和 FIXED_SM4_IV_HEX 进行测试
        if (SM4_KEY != null && SM4_IV != null) {
            String sm4EncryptedHex = sm4EncryptHex(originalText);
            System.out.println("SM4 加密后 (Hex): " + sm4EncryptedHex);

            String sm4DecryptedText = sm4DecryptHex(sm4EncryptedHex);
            System.out.println("SM4 解密后: " + sm4DecryptedText);

            if (originalText.equals(sm4DecryptedText)) {
                System.out.println("SM4加解密测试成功！");
            } else {
                System.err.println("SM4加解密测试失败！原始文本与解密后文本不一致。");
            }
        } else {
            System.err.println("SM4_KEY 或 SM4_IV 未正确初始化，无法进行加解密测试。");
        }
    }
}