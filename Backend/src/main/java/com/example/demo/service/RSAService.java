package com.example.demo.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class RSAService {

    private final String ALGORITHM = "RSA";
    private final int KEY_SIZE = 1024;

    private KeyPairGenerator gen;
    private Cipher cipher;
    private BASE64Encoder base64Encoder = new BASE64Encoder();
    private BASE64Decoder base64Decoder = new BASE64Decoder();

    public RSAService() {
        try {
            // 获取指定算法的密钥对生成器
            gen = KeyPairGenerator.getInstance(ALGORITHM);
            // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
            gen.initialize(KEY_SIZE);
            // 获取指定算法的密码器
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机生成密钥对（包含公钥和私钥）
     */
    public KeyPair generateKeyPair() {
        // 随机生成一对密钥（包含公钥和私钥）
        return gen.generateKeyPair();
    }

    /**
     * 将 公钥/私钥 编码后以 Base64 的格式保存到字符串
     */
    public String saveKeyForEncodedBase64(@NotNull Key key) {
        // 获取密钥编码后的格式
        byte[] encBytes = key.getEncoded();
        // 转换为 Base64 文本
        return base64Encoder.encode(encBytes);
    }

    /**
     * 根据公钥的 Base64 文本创建公钥对象
     */
    public PublicKey getPublicKey(@NotNull String pubKeyBase64) throws IOException {
        // 把 公钥的Base64文本 转换为已编码的 公钥bytes
        byte[] encPubKey = base64Decoder.decodeBuffer(pubKeyBase64);
        // 创建 已编码的公钥规格
        X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
        // 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
        try {
            return KeyFactory.getInstance(ALGORITHM).generatePublic(encPubKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据私钥的 Base64 文本创建私钥对象
     */
    public PrivateKey getPrivateKey(@NotNull String priKeyBase64) throws IOException {
        // 把 私钥的Base64文本 转换为已编码的 私钥bytes
        byte[] encPriKey = base64Decoder.decodeBuffer(priKeyBase64);
        // 创建 已编码的私钥规格
        PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);
        // 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
        try {
            return KeyFactory.getInstance(ALGORITHM).generatePrivate(encPriKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密数据
     */
    public byte[] decrypt(@NotNull byte[] cipherData, @NotNull PrivateKey priKey) {
        // 初始化密码器（私钥解密模型）
        try {
            cipher.init(Cipher.DECRYPT_MODE, priKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
        // 解密数据, 返回解密后的明文
        try {
            return cipher.doFinal(cipherData);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
