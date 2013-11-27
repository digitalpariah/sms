/**
 * date: 2013-06-02
 * read message !
 */

package com.txh.sms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.txh.Api.sqlite;

public class read extends baseclass{

	String num = "",replyname = "", next,dbFile;
	boolean isexit = false;
	sqlite api;	
	private txApplication tx; 
	private final static int MENU_clean = Menu.FIRST;
	private final static int MENU_add = Menu.FIRST + 1;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		api = new sqlite();
		tx = (txApplication)getApplication();
		dbFile = tx.getDbFile();	
		setmessage();
	}
	
	@SuppressWarnings("deprecation")
	private void setmessage(){
		int i ,j,count =1;
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;

		SharedPreferences sharedPreferences = getSharedPreferences("read", Context.MODE_PRIVATE);
		num = sharedPreferences.getString("number", "");

		String [] phone = {num};
		String _id="",content="",ismy="",number="";

		Button bt = (Button) findViewById(R.id.newnote);
		bt.setText(getResources().getString(R.string.with) + tx.getName(num)[0]
				   + getResources().getString(R.string.main_show));
		bt.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkcolor));
		String[] column = {"_id","ismy","content"};
		String[][] value = api.getData(dbFile, "sms", "select * from sms where phone = ?", phone, column);
		for(i = value.length-1;i>-1;i--){
			for(j=0;j<value[i].length;j++){
				if(j==0){
					_id = value[i][j];
				}
				if(j==1){
					ismy = value[i][j];
				}
				if(j==2){
					content = value[i][j];
					number = tx.getName(num)[0];
					Button tv = new Button(this);
					tv.setLayoutParams(new LinearLayout.LayoutParams(7 * screenWidth / 8, 
										LinearLayout.LayoutParams.WRAP_CONTENT));
				
					final String temp = _id;
					if (ismy.equals("true")){
						number = getResources().getString(R.string.me);
					}
					tv.setTextSize(18);
					tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkcolor));
					tv.setTextColor(android.graphics.Color.BLACK);
					tv.setText(number + ":  " + content);
					tv.setContentDescription("sms_"+String.valueOf(count));
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
					count++;
				}
			}
		}
	}

	private void show(){
		new AlertDialog.Builder(this)
		.setTitle(getResources().getString(R.string.ask))
		.setMessage(getResources().getString(R.string.ask_content))
		.setNegativeButton(getResources().getString(R.string.no),
		new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				
			}
		})
		.setPositiveButton(getResources().getString(R.string.yes),
		new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int whichButton){
					delete();
			}
		}).show();
	}

	private void delete(){
		
		String [] args = {next};
		api.delete(dbFile, "sms", "_id=?", args);
		
		boolean isExist;
		isExist = api.exists(dbFile, "sms", "phone", num);
		if(!isExist){
			String [] arg = {num};
			api.delete(dbFile, "phone", "phone=?", arg);
		}
		
		else{
			int j;
			String phone2="",content="",ismy="";
			String [] phone = {num};
			String[] column = {"phone","content","ismy"};
			String[][] value = api.getData(dbFile, "sms", "select * from sms where phone = ?", phone, column);
			for(j=0;j<value[0].length;j++){
				if(j==0){
					phone2 = value[value.length-1][j];
				}
				if(j==1){
					content = value[value.length-1][j];
				}
				if(j==2){
					ismy = value[value.length-1][j];
				}		
				String[]value2 = {phone2,content,ismy};
				String sql = "phone=?";
				api.update(dbFile, "phone", value2, column, phone, sql);
			}
		}
		
		Intent Intent = new Intent(this, conversation.class);
		startActivity(Intent);
		anim();
		this.finish();
	}

	private void del_total(){
		String [] arg = {num};
		api.delete(dbFile, "sms", "phone=?", arg);
		
		api.delete(dbFile, "phone", "phone=?", arg);
		
		Intent Intent = new Intent(this, conversation.class);
		startActivity(Intent);
		anim();
		this.finish();
	}

	public void yc(View v){

		switch (v.getId()){
			case R.id.newnote:
				SharedPreferences edit_new = getSharedPreferences("edit_new",
																  Context.MODE_PRIVATE);
				Editor my_edit = edit_new.edit();
				my_edit.putString("number", num);
				my_edit.commit();
				Intent newnote = new Intent(this, sendsms.class);
				startActivity(newnote);
				anim();
				this.finish();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu){

		// TODO Auto-generated method stub
		menu.add(0, MENU_clean, 1, R.string.clean);
		menu.add(0, MENU_add, 2, R.string.add);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item){

		// TODO Auto-generated method stub
		switch (item.getItemId()){
			case MENU_clean:
				clickClean();
				break;
			case MENU_add:
				add();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void clickClean(){
		new AlertDialog.Builder(this)
		.setTitle(getResources().getString(R.string.ask))
		.setMessage(getResources().getString(R.string.clickCleanContent))
		.setNegativeButton(getResources().getString(R.string.no),
		new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
			}
		})
		.setPositiveButton(getResources().getString(R.string.yes),
		new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int whichButton){
				del_total();
			}
		}).show();
	}

	private void add(){
		
		String[]column = {"value"};
		String[]value = {num};
		boolean isExist = api.exists(dbFile, "intercept", "value", num);
		if(isExist){
			Toast.makeText(this,getResources().getString(R.string.add_error),
					   Toast.LENGTH_LONG).show();
			return ;
		}
		else{
			api.insertData(dbFile, "intercept", value, column);
		}
	}
}
