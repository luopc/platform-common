package com.luopc.platform.web.common.core.util;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * <a href="https://mp.weixin.qq.com/s/sXPPQB5NNXyVNw0yPI3REQ">Spring Boot + Jasypt 三步搞定敏感信息加密</a>
 * <a href="https://mp.weixin.qq.com/s/KT6iw_8MNwL3URien_9BmA">Spring Boot 集成 BouncyCastle 实现加密算法</a>
 * <a href="https://mp.weixin.qq.com/s/_I-9BOstI6ZN9GvD4CzCEA">SpringBoot实现接口统一加密解密</a>
 */
public class JasyptEncryptorDemo {
    public static void main(String[] args) {
        String pwd = "12345";
        String secretKey = StringUtils.isBlank(System.getenv("JASYPT_ENCRYPTOR_PASSWORD")) ?
                "72$kL789sdf87&Po9876%987kjh876*9878765gfds7890" : System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
        String algorithm = "PBEwithSHA1AndDESede";
        System.out.println("secretKey=" + secretKey);
        StandardPBEStringEncryptor encryptor1 = new StandardPBEStringEncryptor();
        encryptor1.setPassword(secretKey);
        // PBEWithHMACSHA256AndAES_128
        // PBEWithMD5AndDES
        encryptor1.setAlgorithm(algorithm);
        PooledPBEStringEncryptor encryptor2 = getPooledPBEStringEncryptor(secretKey, algorithm);

        String encryptedText1 = encryptor1.encrypt(pwd);
        System.out.println("-".repeat(30) + "encryptedText1" + "-".repeat(30));
        System.out.println(encryptedText1);

        String encryptedText2 = encryptor2.encrypt(pwd);
        System.out.println("-".repeat(30) + "encryptedText2" + "-".repeat(30));
        System.out.println(encryptedText2);

        System.out.println("-".repeat(30) + "decryptedText1" + "-".repeat(30));
        String decryptedText1 = encryptor2.decrypt(encryptedText1);
        System.out.println(decryptedText1);

        System.out.println("-".repeat(30) + "decryptedText2" + "-".repeat(30));
        String decryptedText2 = encryptor2.decrypt(encryptedText2);
        System.out.println(decryptedText2);
        System.out.println("-".repeat(73));
    }

    private static PooledPBEStringEncryptor getPooledPBEStringEncryptor(String secretKey, String algorithm) {
        PooledPBEStringEncryptor encryptor2 = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(secretKey);
        config.setAlgorithm(algorithm);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor2.setConfig(config);
        return encryptor2;
    }
}
