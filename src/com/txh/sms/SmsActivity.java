/**
 * date:2013-06-02
 * receiver message then call dealmsg
 */


package com.txh.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsActivity extends BroadcastReceiver{

	String strMsg = "";

	String number = "";

	public void onReceive(Context context, Intent intent){
		this.abortBroadcast();
		Bundle bundle = intent.getExtras();
		if (bundle != null)
		{
			SmsMessage[] messages;
			StringBuffer sbMsg = new StringBuffer();
			try
			{
				Object[] myOBJpdus = (Object[]) bundle.get("pdus");
				messages = new SmsMessage[myOBJpdus.length];
				for (int i = 0; i < myOBJpdus.length; i++)
				{
					messages[i] = SmsMessage
							.createFromPdu((byte[]) myOBJpdus[i]);
				}

				// think of long message
				for (SmsMessage currentMessage : messages)
				{
					sbMsg.append(currentMessage.getDisplayMessageBody());
				}

				strMsg = sbMsg.toString();
				number = messages[0].getOriginatingAddress();
				if(number.startsWith("+86")){
					number = number.replace("+86", "");
				}

				SharedPreferences new_sms = context.getSharedPreferences(
						"new_sms", Context.MODE_PRIVATE);
				Editor new_edit = new_sms.edit();
				new_edit.putString("sms", strMsg);
				new_edit.putString("phone", number);
				new_edit.commit();

				Intent showMsg = new Intent(context, dealmsg.class);
				showMsg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(showMsg);
			} catch (Exception e)
			{
			}
		}
	}
}
