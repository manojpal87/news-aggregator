package com.example.news.aggregator.security;

import java.util.Base64;

/**
 * 
 * Class will be used to encrypt and decrypt application developer key
 * 
 * @author manoj
 *
 */
public final class AppKeyDecrypt {

	//public static void main(String[] args) {
		//System.out.println("Encrypt theguardian content search key : " + encrypt("5dfddff2-0199-45ab-bc63-3b4cd8fa87d8"));
		//System.out.println("Encrypt newyorktimes article search key : " + encrypt("06gCY9iqAMDmAelALDAEXRnC5R1IXXOo"));
	//}

	/**
	 * Construct the actual key
	 * 
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {

		return data != null ? new String(Base64.getDecoder().decode(data)) : "";
	}

	/**
	 * Encrpt application key using base64 encoder.
	 * 
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {

		return Base64.getEncoder().encodeToString(data.getBytes());
	}
}
