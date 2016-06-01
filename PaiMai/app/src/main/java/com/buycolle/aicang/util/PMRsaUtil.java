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
	
//	public static void main(String[] args) {
//		try {
//
//			String message  = "{\"status\" : \"1\",\"infos\" : {\"begin_auct_price\" : \"200.00\","
//					+ "\"open_but_it\" : \"1\",\"fahuo_time_type\" : \"1\",\"product_id\" : \"9\",\"st_name\" : \"9成新\","
//					+ "\"cate_name\" : \"热血系\",\"but_it_price\" : \"1000.00\",\"fahou_city\" : \"深圳市\",\"pm_end_time\" :"
//					+ " \"2016-04-07 12:47:17\",\"product_title\" : \"漫画拍卖啦\",\"fahou_province\" : \"广东省\",\"cate_id\" : "
//					+ "\"43\",\"cycle_pic\" : \"http://120.24.70.253:6080/imghouse/paimai/upload/image/image_160322113919_86_7da685001e153f41a83eb6255069dc3d$xxx$.jpg,http://120.24.70.253:6080/imghouse/paimai/upload/image/image_160322113919_86_7da685001e153f41a83eb6255069dc3d$xxx$.jpg,http://120.24.70.253:6080/imghouse/paimai/upload/image/image_160322113919_86_7da685001e153f41a83eb6255069dc3d$xxx$.jpg\","
//					+ "\"cover_pic\" : \"http://120.24.70.253:6080/imghouse/paimai/upload/image/image_160406174632_6_751d31dd6b56b26b29dac2c0e1839e34$xxx$.png\",\"st_id\" : \"9\",\"product_desc\" : "
//					+ "\"这是一个忍者的世界，五大国中的火之国的木叶村的忍者为代表e\",\"express_out_type\" : \"2\",\"is_same_express\" : \"1\"},\"desc\" : \"成功\"}";
//
//			PMRsaUtil util = new PMRsaUtil();
//
//			PrivateKey privateKey = util.readPrivateKey();
//
//			PublicKey publicKey = util.getPublicKeyFromX509(util.ALGORITHM, util.readRAWPublicKey());
//
//			String priKeyEncrypt = util.PriKeyEncrypt(message, privateKey);
//			String pubKeyEncrypt = util.PubKeyEncrypt(message, publicKey);
//			System.out.println("公钥加密后的字符串：" + pubKeyEncrypt);
////			pubKeyEncrypt = "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";//util.PubKeyEncrypt(message, publicKey);
//
//			System.out.println("私钥加密后的字符串：" + priKeyEncrypt);
//
//			System.out.println("用公玥对私密加密解密：" + util.PubKeyDecrypt(priKeyEncrypt, publicKey));
//			String prikeyDe = util.PriKeyDecrypt(pubKeyEncrypt, privateKey);
//			System.out.println("用私玥对公密加密解密：" + prikeyDe);
////			System.out.println("aaa用私玥对公密加密解密：" + util.PubKeyDecrypt(prikeyDe, publicKey));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
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
//			byte[] output = cipher.doFinal(plaintext);
//			String s = new String(Base64.getEncoder().encode(output));
//			return s;
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
//			byte[] decryptedData = cipher.doFinal(data);
//			return new String(decryptedData);
			
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
//			byte[] output = cipher.doFinal(plaintext);
//			String s = new String(Base64.getEncoder().encode(output));
//			return s;
			
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
//			byte[] decryptedData = cipher.doFinal(data);
//			return new String(decryptedData,Charset.forName("UTF-8"));
			
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

