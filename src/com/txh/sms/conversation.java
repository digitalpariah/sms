/**
 * date: 2013-06-02
 * Conversation with friends 
 * */

package com.txh.sms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txh.Api.sqlite;

public class conversation extends baseclass{

	String dbFile,num ,replyname ,next;
	public static String number;
	boolean isexit = false;
	sqlite api;
	
	private txApplication tx; 

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		tx = (txApplication)getApplication();
		api = new sqlite();
		Button bt = (Button)findViewById(R.id.newnote);
		bt.setVisibility(8);
		dbFile = tx.getDbFile();
		setmessage();
	}
	
	@SuppressWarnings("deprecation")
	private void setmessage(){
		int i,j;
		String phone ="",content="";
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;

		String[] column = {"phone","content"};
		String[][] value = api.getData(dbFile, "phone", "select * from phone", null, column);
		for(i = 0;i<value.length;i++){
			for(j=0;j<value[i].length;j++){
				if(j==0){
					phone = value[i][j];
				}
				else{
					content = value[i][j];
					Button tv = new Button(this);
					tv.setLayoutParams(new LinearLayout.LayoutParams(7 * screenWidth / 8, 
																	 LinearLayout.LayoutParams.WRAP_CONTENT));
					tv.setTextSize(18);
					tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkcolor));
					tv.setTextColor(android.graphics.Color.BLACK);
					tv.setText(tx.getName(phone)[0] + ":  " + content);
					tv.setContentDescription(phone);
					final String temp = phone;
					tv.setOnClickListener(new OnClickListener(){
						public void onClick(View arg0){
							next = temp;
							show();
						}
					});

					TextView textview = new TextView(this);
					textview.setHeight(8);
					final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.message);
					linearLayout.addView(tv, 0);
					linearLayout.addView(textview, 0);
				}					
			}			
		}
	}
	
	private void show(){
		SharedPreferences sharedPreferences = getSharedPreferences("read", Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("number", next).commit();
		
		Intent read = new Intent(this, read.class);
		startActivity(read);
		anim();
		finish();
	}
}
