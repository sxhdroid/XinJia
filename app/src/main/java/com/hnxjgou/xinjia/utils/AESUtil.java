package com.hnxjgou.xinjia.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密工具类
 * 
 * @author yyh
 * 
 */
public class AESUtil {

	private static final String SEED = "fqJfdzGDvfwbedsKSUGty3VZ9taXxjks";
	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";


	/**
	 * 加密
	 * @param clearText
	 *            要加密的字串
	 * @return 加密之后的字串
	 * @throws Exception
	 */
	public static String encrypt(String clearText) throws Exception {
//		byte[] rawkey = getRawKey(SEED.getBytes("utf-8"));
		byte[] rawkey = SEED.getBytes("utf-8");
		byte[] result = encrypt(rawkey, clearText.getBytes("utf-8"));
//		return toHex(result);
		return Base64.encodeToString(result, Base64.NO_WRAP);
	}

	/**
	 * 解密
	 * 
	 * @param encrypted
	 *            加密之后的字串
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encrypted) throws Exception {
        if (TextUtils.isEmpty(encrypted))return null;
//		byte[] rawKey = getRawKey(SEED.getBytes("utf-8"));
		byte[] rawKey = SEED.getBytes("utf-8");
//		byte[] enc = toByte(encrypted);
		byte[] enc = Base64.decode(encrypted, Base64.NO_WRAP);
		byte[] result = decrypt(rawKey, enc);
		return new String(result, "utf-8");
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
//		if (android.os.Build.VERSION.SDK_INT >= 17) {
//			sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
//		} else {
//			sr = SecureRandom.getInstance("SHA1PRNG");
//		}
		sr.setSeed(seed);
		kgen.init(128, sr);
		SecretKey sKey = kgen.generateKey();
		byte[] raw = sKey.getEncoded();

		return raw;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
//		IvParameterSpec iv = new IvParameterSpec(IV.getBytes("utf-8"));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		Cipher cipher = Cipher.getInstance(ALGORITHM);
//		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
//		IvParameterSpec iv = new IvParameterSpec(IV.getBytes("utf-8"));// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
//		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	private static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private static void appendHex(StringBuffer sb, byte b) {
		final String HEX = "0123456789ABCDEF";
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

}
