
package com.txh.Api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class md5 {

	/**
	 * @param args
	 */
	public static String afterMd5(String args) {
		return md32(md16(args));
	}
	
	private static String md16(String text){
		
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(text.getBytes()); 
			byte b[] = md.digest(); 

			int i; 

			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
			i = b[offset]; 
			if(i<0) i+= 256; 
			if(i<16) 
			buf.append("0"); 
			buf.append(Integer.toHexString(i)); 
			} 
			
			return buf.toString().substring(8,24);

			} catch (NoSuchAlgorithmException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace();
		} 
		
		return null;
		
	}
	
	private static String md32(String text){
		
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(text.getBytes()); 
			byte b[] = md.digest(); 

			int i; 

			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
			i = b[offset]; 
			if(i<0) i+= 256; 
			if(i<16) 
			buf.append("0"); 
			buf.append(Integer.toHexString(i)); 
			} 
			
			return buf.toString();

			} catch (NoSuchAlgorithmException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace();
		} 
		return null;
	}
	
}
