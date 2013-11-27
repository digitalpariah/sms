package com.txh.Api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class common{
	
	/**
	 * return date , format like yyyy/MM/dd/HH/mm
	 * @return
	 */
	public String getDate(){
		 SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd/HH/mm",Locale.CHINA);
		 Date dd = new Date();
		 return ft.format(dd);
	}
	
	/**
	 * return time1-time2 as a millisecond value
	 * @param time1
	 * @param time2
	 * @return
	 */	
	public long getQuot(String time1, String time2){
		 long quot = 0;
		 SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd/HH/mm",Locale.CHINA);
		 try {
			 Date date1 = ft.parse(time1);
			 Date date2 = ft.parse(time2);
			 quot = date1.getTime() - date2.getTime();
		  } catch (ParseException e) {
		   e.printStackTrace();
		  }
		  return quot;
	}
	
	/**
	 * copy src to dst if fail return false else return true
	 * @param src
	 * @param dst
	 * @return
	 */
	
	public boolean copy(File src, File dst){
		boolean result = true;
		try {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dst);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			result = false;
			e.printStackTrace();
		}
		return result ;
	}
}
