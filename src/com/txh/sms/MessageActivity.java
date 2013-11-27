/**
 * date:2013-06-02
 * send message background !
 **/

package com.txh.sms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.Toast;

import com.txh.Api.sqlite;

public class MessageActivity extends Service{

	SmsManager smsManager;
	EditText editText = null;
	SharedPreferences shared;
	String dbFile;
	sqlite api;
	private txApplication tx;
	int i = 0;

	public IBinder onBind(Intent arg0){
		return null;
	}

	public void onCreate(){
		api = new sqlite();
		tx = (txApplication)getApplication();
		dbFile = tx.getDbFile();
		super.onCreate();
	}

	public void onDestroy(){

		super.onDestroy();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		
		send();
		return START_STICKY;
	}

	private void send(){
		SharedPreferences newmessage = getSharedPreferences("send_new",
															Context.MODE_PRIVATE);
		String content = newmessage.getString("content", "");
		String phone = newmessage.getString("phone", "");
		
		smsManager = SmsManager.getDefault();	

		if (PhoneNumberUtils.isGlobalPhoneNumber(phone)&&!content.equals("")){			
			
			smsManager.sendTextMessage(phone, null, content, null, null);
			addData(phone, content +  "\n" + tx.getDate("yyyy-MM-dd  HH:mm:ss"), "true");			
			newmessage.edit().clear().commit();	
		} 
		else{
			Toast.makeText(this, R.string.send_error, Toast.LENGTH_LONG).show();
		}
		stopSelf();
	}
	
	private void addData(String phone, String content, String type){
		boolean isExist;
		isExist = api.exists(dbFile, "phone", "phone", phone);
		String[]column = {"phone","content","ismy"};
		String[]value = {phone,content,type};
		if(isExist){
			String[] args = {phone};
			String sql = "phone=?";
			api.update(dbFile, "phone", value, column, args, sql);
		}
		else{
			api.insertData(dbFile, "phone", value, column);
		}
		
		api.insertData(dbFile, "sms", value, column);	
		
		isExist = false;
		isExist = api.exists(dbFile, "recent", "phone", phone);
		if(!isExist){
			String[] value2 = {phone};
			String[] column2 = {"phone"};
			api.insertData(dbFile, "recent", value2, column2);
		}
	}
}
