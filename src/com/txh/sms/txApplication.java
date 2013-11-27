package com.txh.sms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Process;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.txh.Api.sqlite;

public class txApplication extends Application {

	private static String tag = "txTag";
	private String dbFile; 
	public void onCreate() {
		firstUse();
		Log.i(tag, "Application onCreate , pid = " + Process.myPid());
	}
	
	/**
	 * check if is first time , return true if is first time
	 * @return
	 */
	public boolean firstUse(){
		File dbFile = new File(getDbFile());
		if(!dbFile.exists()){
			createTable();
			Log.i(tag, "this is first time to use");
			return true;
		}
		
		Log.i(tag, "this is not the first time to use");
		return false;
	}
	
	/**
	 * return sqlite file path ,if parent directory not exist
	 * it will create !
	 * @return
	 */
	public String getDbFile(){
	//	String file = Environment.getExternalStorageDirectory().getPath()+"/.txh";
		String file = this.getFilesDir().getPath();
		File f = new File(file);
		if(!f.exists()){
			f.mkdir();
		}
		dbFile = file + "/sms.db";
		Log.i(tag, dbFile);
		return dbFile;
	}
	
	
	/**
	 * return windows animation string
	 * @return
	 */
	
	public String getAnim(){
		SharedPreferences myshared = getSharedPreferences(
				"com.txh.sms_preferences", Context.MODE_PRIVATE);
		String anim = myshared.getString("anim", "ubuntu");
		Log.i(tag, "anim: "+anim);
		return anim;
	}
	
	/**
	 * find name by phone number , return phone number 
	 * and false if not exist , or return name and true
	 * @return
	 */
	public String[] getName(String number){
		String name = "";
		String temp = number;
		String[] result = new String[2];
		result[1] = "true";
		int error = 0;
		while(name.equals("")&&error<3){
			if(error ==1&&number.length()>10){
				temp = number.substring(0,3)+" "+number.substring(3,7)+" "+number.substring(7,11);
			}
			if(error ==2&&number.length()>10){
				temp = number.substring(0,3)+" "+number.substring(3,7)+" "+number.substring(7,11);
			}
			
			String[] projection = {
					ContactsContract.PhoneLookup.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NUMBER };
			Cursor cur = this.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					projection, // Which columns to return.
					ContactsContract.CommonDataKinds.Phone.NUMBER
					+ " = '" + temp + "'", // WHERE clause.
					null, // WHERE clause value substitution
					null); // Sort order.
			if (cur == null){
				result[0] = number;
				result[1] = "false";
				return result;
			}
			for (int i = 0; i < cur.getCount(); i++){
				cur.moveToPosition(i);
				int nameFieldColumnIndex = cur
				.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
				name = cur.getString(nameFieldColumnIndex);
			}
			cur.close();
			error++;
		}
		if (name.equals("")){
			name = number;
			result[1] = "false";
		}
		result[0] = name;
		return result;
	}
	
	/**
	 * return pkgName+" "+versionName , if fail return ""
	 * @return
	 */
	public String getVersion() {
	    try {
	        PackageManager manager = this.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	        String version = info.versionName;
	        String pkgName = info.applicationInfo.loadLabel(getPackageManager()).toString();
	        return pkgName+" "+version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "";
	    }
	}
	
	/**
	 * return date by your own formatter , like yyyy/MM/dd/HH/mm
	 * @return
	 */
	public String getDate(String formatter){
		 SimpleDateFormat ft = new SimpleDateFormat(formatter,Locale.CHINA);
		 Date dd = new Date();
		 return ft.format(dd);
	}
	
	
	/**
	 * @return phone number if success or return null 
	 */
	
	public String getNumber(){
		TelephonyManager mngr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE); 
		String number = mngr.getLine1Number();
	    
	    Log.i("---justyce---", "phone number :"+number);
	    return number;
	}
	
	private void createTable(){
		sqlite api = new sqlite();
		String dbFile = getDbFile();
		String sql = "create table if not exists 'intercept'('value')";		
		api.createTable(sql, dbFile);
		sql = "create table if not exists 'words'('value')";		
		api.createTable(sql, dbFile);
		sql = "create table if not exists 'sms'(_id INTEGER PRIMARY KEY,'phone','content','ismy')";
		api.createTable(sql, dbFile);
		sql = "create table if not exists 'phone'('phone','content','ismy')";
		api.createTable(sql, dbFile);
		sql = "create table if not exists 'timing'" +
		"(_id INTEGER PRIMARY KEY,'phone','content','year','month','day','hour','minute')";
		api.createTable(sql, dbFile);
		sql = "create table if not exists 'recent'('phone')";		
		api.createTable(sql, dbFile);
	}
}