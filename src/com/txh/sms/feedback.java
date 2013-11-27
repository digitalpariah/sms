
package com.txh.sms;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.txh.Api.libs;

public class feedback extends baseclass{
	
	private ProgressDialog dialog;
	private EditText contact,content;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        
        int height = metric.heightPixels;
        
        EditText content = (EditText)findViewById(R.id.msg);
        content.setHeight(7*height/12);
	}
	
	public void btClick(View v){
		startSave();
	}
	
	private void startSave(){

		String title = getResources().getString(R.string.dialog_title);
		String content = getResources().getString(R.string.dialog_content);
		dialog = ProgressDialog.show(this, title, content, true);
		Thread thread = new Thread(null, domywork, "Background");
		thread.start();
	}

	@SuppressLint("HandlerLeak")
	private Runnable domywork = new Runnable(){

		public void run(){

			libs lib = new libs();
			txApplication tx = (txApplication)getApplication();
			String phone = "apk: "+tx.getVersion()+" phone: "+tx.getNumber();
			
			content = (EditText)findViewById(R.id.msg);
			contact = (EditText)findViewById(R.id.contact);
			
			String msg = content.getText().toString();
			String info = " qq: "+contact.getText().toString();
			
			String[] args = {"justyce2013@163.com","MM|2013@163.com","1558789954@qq.com",
					phone+info,msg};
			lib.sendMail(args);
			
			handler.sendEmptyMessage(0);
		}

		/* deal dialog! */
		Handler handler = new Handler(){
			public void handleMessage(Message msg){

				/* because there just a message */
				dialog.dismiss();
				empty();
			}
		};
	};
	
	private void empty(){
		content.setText("");
		contact.setText("");
	}
}
