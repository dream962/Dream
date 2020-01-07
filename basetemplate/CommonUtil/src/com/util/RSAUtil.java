package com.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RSAUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtil.class);

    /**
     * * 生成密钥对 *
     * 
     * @return KeyPair *
     * @throws EncryptException
     */
    public static KeyPair generateKeyPair() throws Exception
    {
        try
        {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
                    new BouncyCastleProvider());

            // KEY_SIZE关系到块加密的大小
            final int KEY_SIZE = 1024;
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return keyPair;
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public static RSAPublicKey generateRSAPublicKey(String pubModString,
            String pubPubExpString) throws Exception
    {
        byte[] pubModBytes = Base64.decodeBase64(pubModString);
        byte[] pubPubExpBytes = Base64.decodeBase64(pubPubExpString);

        return generateRSAPublicKey(pubModBytes, pubPubExpBytes);
    }

    // 生成公钥
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus,
            byte[] publicExponent) throws Exception
    {
        KeyFactory keyFac = null;
        try
        {
            keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new Exception(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(1,
                modulus), new BigInteger(1, publicExponent));
        try
        {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        }
        catch (InvalidKeySpecException ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    public static RSAPrivateKey generateRSAPrivateKey(String priModString,
            String priPriExpString) throws Exception
    {
        byte[] modBytes = Base64.decodeBase64(priModString);
        byte[] priExpBytes = Base64.decodeBase64(priPriExpString);
        return generateRSAPrivateKey(modBytes, priExpBytes);
    }

    // 生成私钥
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,
            byte[] privateExponent) throws Exception
    {
        KeyFactory keyFac = null;
        try
        {
            keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new Exception(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(1,
                modulus), new BigInteger(1, privateExponent));
        try
        {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        }
        catch (InvalidKeySpecException ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Encrypt String.
     * 
     * @return byte[]
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] obj)
    {
        if (publicKey != null)
        {
            try
            {
                Cipher cipher = Cipher.getInstance("RSA");
                // ENCRYPT_MODE : 用于将 cipher 初始化为加密模式的常量。
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                ByteBuffer buffer = ByteBuffer.wrap(obj);
                ByteBuffer result = ByteBuffer.allocate(2048);

                byte[] data = new byte[117];

                // 分段解密
                while (buffer.position() < buffer.limit())
                {
                    if (buffer.remaining() >= 117)
                        buffer.get(data);
                    else
                    {
                        data = new byte[buffer.remaining()];
                        buffer.get(data, 0, buffer.remaining());
                    }

                    result.put(cipher.doFinal(data));
                }

                result.flip();
                byte[] cipherBytes = new byte[result.limit()];
                result.get(cipherBytes);

                return cipherBytes;
            }
            catch (Exception e)
            {
                LOGGER.error("RSAUtil:encrypt", e);
            }
        }
        return null;
    }

    public static String encrypt(String pubModString, String pubPubExpString,String str)
    {
        try
        {
            RSAPublicKey recoveryPubKey = generateRSAPublicKey(pubModString,
                    pubPubExpString);
            byte[] raw = encrypt(recoveryPubKey, str.getBytes());
            return base64Encode(raw);
        }
        catch (Exception e)
        {
            LOGGER.error("RSAUtil:encrypt", e);
        }

        return null;
    }
    
    public static byte[] encrypt(String pubModString, String pubPubExpString,byte[] data)
    {
        try
        {
            RSAPublicKey recoveryPubKey = generateRSAPublicKey(pubModString,pubPubExpString);
            byte[] raw = encrypt(recoveryPubKey, data);
            return raw;
        }
        catch (Exception e)
        {
            LOGGER.error("RSAUtil:encrypt", e);
        }

        return null;
    }

    /**
     * Basic decrypt method
     * 
     * @return byte[]
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] obj)
    {
        if (privateKey != null)
        {
            try
            {
                Cipher cipher = Cipher.getInstance("RSA");
                ByteBuffer buffer = ByteBuffer.wrap(obj);
                ByteBuffer result = ByteBuffer.allocate(2048);

                // DECRYPT_MODE : 用于将 cipher 初始化为解密模式的常量。
                cipher.init(Cipher.DECRYPT_MODE, privateKey);

                byte[] data = new byte[128];

                // 分段解密
                while (buffer.position() < buffer.limit())
                {
                    if (buffer.remaining() >= 128)
                        buffer.get(data);
                    else
                    {
                        data = new byte[buffer.remaining()];
                        buffer.get(data, 0, buffer.remaining());
                    }

                    result.put(cipher.doFinal(data));
                }

                result.flip();
                byte[] cipherBytes = new byte[result.limit()];
                result.get(cipherBytes);

                return cipherBytes;
            }
            catch (Exception e)
            {
                LOGGER.error("RSAUtil:decrypt", e);
            }
        }

        return null;
    }

    public static byte[] decrypt(String priModString, String priPriExpString,
            byte[] clipherString) throws Exception
    {
        RSAPrivateKey recoveryPriKey = generateRSAPrivateKey(priModString,
                priPriExpString);
        return RSAUtil.decrypt(recoveryPriKey, clipherString);
    }

    public static String decrypt(String priModBytesNew,
            String priPriExpBytesNew, String str)
    {
        try
        {
            byte[] enRaw = Base64.decodeBase64(str.getBytes());
            byte[] data = RSAUtil.decrypt(priModBytesNew, priPriExpBytesNew,
                    enRaw);
            return new String(data);
        }
        catch (Exception e)
        {
            LOGGER.error("RSAUtil:decrypt", e);
        }

        return null;
    }

    public static String base64Encode(byte[] bts)
    {
        return Base64.encodeBase64String(bts);
    }

    public static void main(String[] args)
    {
        KeyPair keyPair;
        try
        {
            keyPair = generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            System.out.print(privateKey);
            System.out.print(keyPair.getPublic());
        }
        catch (Exception e)
        {
            LOGGER.error("RSAUtil:main", e);
        }
        
        //jdbc:mysql://127.0.0.1:3306/db_game ddt_root love7road!@#
        
        String publicModulus="AIv9tT8w1f3NmLU94btc4vQzUi3b2prtn+1kzxq6/+aVKbH424eFYphP7VxXRknAeUs3P+I7hs7WY+BdJra7O4Hx0t45Om6vs4DNpFhS20U7R8pxLW0zc0VE+qr0gi9JnpxhIvfaNEfN1DhH2czqxgVYpGumL1GJApA1R+rD+OK3";
        String publicExponent="AQAB";
        
        String privateModulus="AIv9tT8w1f3NmLU94btc4vQzUi3b2prtn+1kzxq6/+aVKbH424eFYphP7VxXRknAeUs3P+I7hs7WY+BdJra7O4Hx0t45Om6vs4DNpFhS20U7R8pxLW0zc0VE+qr0gi9JnpxhIvfaNEfN1DhH2czqxgVYpGumL1GJApA1R+rD+OK3";
        String privateExponent="QJ40il2VeVjsk0RkLOMcU5JY7ZgdGQR0nNS6kGEe/CWAIIl5c93K+/lw2/3hEOwKUQDIuTm26USmx4rX8uML/ShtuiPImvbyQI3m6nbo3O0IWbVaMFIXd0R/Wh/JzUA00idf4l36h3cJZoJJ3IJsBxABv1yjKAkFP/3SdqxX4vk=";
        
        String testString="jdbc:mysql://119.147.229.171:3306/db_game?allowMultiQueries=true";
        String newString=encrypt(publicModulus, publicExponent, testString);
        
        System.err.println("加密后:"+newString);
        
        System.err.println("解密后:"+decrypt(privateModulus, privateExponent, newString));

    }
}
