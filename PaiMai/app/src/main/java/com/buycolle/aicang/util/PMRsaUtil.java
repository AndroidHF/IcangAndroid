package com.buycolle.aicang.util;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


/**
 *
 *Create by zjh 2016年4月18日 上午9:54:51
 */
public class PMRsaUtil {

	private final String ALGORITHM = "RSA";

	private final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	
	//RSA最大加密明文大小
	private final int MAX_ENCRYPT_BLOCK = 117;
     
	//RSA最大解密密文大小
	private final int MAX_DECRYPT_BLOCK = 128;

	private static PMRsaUtil uniqueInstance = null;

	private PMRsaUtil() {
	}
	public static PMRsaUtil getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new PMRsaUtil();
		}
		return uniqueInstance;

	}
	
	/**
	 * 私钥加密
	 * create by zjh 2016年4月18日  上午10:14:24
	 */
	public String priKeyEncrypt(String message) throws Exception{
		PrivateKey privateKey = readPrivateKey();
		return PriKeyEncrypt(message, privateKey);
	}
	
	/**
	 * 公钥加密
	 * create by zjh 2016年4月18日  上午10:15:26
	 */
	public String pubKeyEncrypt(String message) throws Exception{
		PublicKey publicKey = getPublicKeyFromX509(ALGORITHM, readRAWPublicKey());
		return PubKeyEncrypt(message, publicKey);
	}
	/**
	 * 私钥解密
	 * create by zjh 2016年4月18日  上午10:16:39
	 */
	public String priKeyDecrypt(String message) throws Exception{
		PrivateKey privateKey = readPrivateKey();
		return PriKeyDecrypt(message, privateKey);
	}
	
	/**
	 * 公钥解密
	 * create by zjh 2016年4月18日  上午10:17:05
	 */
	public String pubKeyDecrypt(String message) throws Exception{
		PublicKey publicKey = getPublicKeyFromX509(ALGORITHM, readRAWPublicKey());
		return PubKeyDecrypt(message, publicKey);
	}

	private PrivateKey readPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String privateKeyData = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKzuT4pV+n+MwzkN9EqODyhp/mK880Ki27+mMJSOqHKe3XuoQkOgWAVRzUzw7lGJoeTFWwN1alxKayr1V3tRpc0EjqyAr5pQvhnGW+GIGIbWYqlrJasjahcntpSoPA9IN2mAFEu6y91me/MQnZy/bZaO4Z9uVJ1m+u3WNPxcmLBhAgMBAAECgYEAqsO5b9VvQ7zwmsqYzXZyqWnhdgc4ADlp3lpx5oDo/ia6d32z0avov3gDz1KrQ4ExiQMJ2OR9Xx1trkIPXQtHOQhaf1Bbatk9hAb/bpbqptquCw5TTY+tjOUZtLkXjHUr6P6cRNyCOfE9PWY/sxplGsvkc/9SAlFv3xSaKR8DqzECQQDfU/dBaZSR39W55vHLomIWLmctUqw3oLmfEnLdBzfBDBwMzddlaooIny+fcXMA4FRKeVGwe8mK9BXji/e+pwVHAkEAxjriJDijrBGvfBMAc6exslbzoahqpd5MAdnH0DCILEPBnNZRrZjOoTlZXelecUF2SgsbVJm0Bp0Qm60oJpaRFwJBAK2y5AP3d3vCCbyu2G/W281+x/cjbxlRJC6KLdalz4KRxZtb2mVh6Pxtu5+aoKUU1dqa46ONlCNEV2YFLmsWAu8CQFfws3ZCMkoZpnIys9abJHfrnzWuU3G2Rp3jkYNIpICOpov/gEi1K6XWnVDOQPmZgvKiLsO/VGBCuaV2kgNcdI0CQQCI22svumUQlATyJYoexXy1J8eEqggW/UnZAGlcW6mQPCqWRRo3yTsCrMKUztUBOO5FZfVLEpHtE1Av/2N1lw3/";
		byte[] decodedKeyData = Base64.decode(privateKeyData.getBytes(),Base64.DEFAULT);
		return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedKeyData));
	}

	private byte[] readRAWPublicKey() throws IOException {
		String pubKeyData = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCs7k+KVfp/jMM5DfRKjg8oaf5ivPNCotu/pjCUjqhynt17qEJDoFgFUc1M8O5RiaHkxVsDdWpcSmsq9Vd7UaXNBI6sgK+aUL4ZxlvhiBiG1mKpayWrI2oXJ7aUqDwPSDdpgBRLusvdZnvzEJ2cv22WjuGfblSdZvrt1jT8XJiwYQIDAQAB";
		byte[] decodedKeyData = Base64.decode(pubKeyData.getBytes(),Base64.DEFAULT);
		return decodedKeyData;
	}
	
	private String PriKeyEncrypt(String content, PrivateKey priKey) {
		try {
			Cipher cipher = Cipher.getInstance(priKey.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, priKey);
			byte plaintext[] = content.getBytes(Charset.forName("UTF-8"));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int inputLen = plaintext.length;
			int offSet = 0;
	        byte[] cache;
	        int i = 0;
			// 对数据分段加密
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
	                cache = cipher.doFinal(plaintext, offSet, MAX_ENCRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(plaintext, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_ENCRYPT_BLOCK;
	        }
	        byte[] encryptedData = out.toByteArray();
	        out.close();
			String s = new String( Base64.decode(encryptedData,Base64.DEFAULT));
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String PriKeyDecrypt(String content, PrivateKey priKey) {
		try {
			byte[] data = Base64.decode(content.getBytes(Charset.forName("UTF-8")),Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance(priKey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			byte encryptedData[] = Base64.decode(content.getBytes(Charset.forName("UTF-8")),Base64.DEFAULT);
			int inputLen = encryptedData.length;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        byte[] cache;
	        int i = 0;
	        // 对数据分段解密
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
	                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_DECRYPT_BLOCK;
	        }
	        byte[] decryptedData = out.toByteArray();
	        out.close();
			return  new String(decryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private PublicKey getPublicKeyFromX509(String algorithm, byte[] bysKey)
			throws NoSuchAlgorithmException, Exception {
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(bysKey);
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(x509);
	}

	private String PubKeyEncrypt(String content, PublicKey pubKey) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte plaintext[] = content.getBytes(Charset.forName("UTF-8"));
			 int inputLen = plaintext.length;
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        int offSet = 0;
		        byte[] cache;
		        int i = 0;
		        // 对数据分段加密
		        while (inputLen - offSet > 0) {
		            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
		                cache = cipher.doFinal(plaintext, offSet, MAX_ENCRYPT_BLOCK);
		            } else {
		                cache = cipher.doFinal(plaintext, offSet, inputLen - offSet);
		            }
		            out.write(cache, 0, cache.length);
		            i++;
		            offSet = i * MAX_ENCRYPT_BLOCK;
		        }
		        byte[] encryptedData = out.toByteArray();
		        out.close();
		    	String s = new String(Base64.encode(encryptedData,Base64.DEFAULT));
				return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String PubKeyDecrypt(String content, PublicKey pubKey) {
		try {
			byte[] data = Base64.decode(content.getBytes(Charset.forName("UTF-8")),Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			byte encryptedData[] = Base64.decode(content.getBytes(Charset.forName("UTF-8")),Base64.DEFAULT);
			 int inputLen = encryptedData.length;
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        int offSet = 0;
		        byte[] cache;
		        int i = 0;
		        // 对数据分段解密
		        while (inputLen - offSet > 0) {
		            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
		                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
		            } else {
		                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
		            }
		            out.write(cache, 0, cache.length);
		            i++;
		            offSet = i * MAX_DECRYPT_BLOCK;
		        }
		        byte[] decryptedData = out.toByteArray();
		        out.close();
		        return new String(decryptedData,Charset.forName("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

