package com.txh.sms;

import java.io.File;

import com.txh.Api.common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class backup extends baseclass {

	private common txApi;
	private txApplication tx;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backup);

		txApi = new common();
		tx = (txApplication)getApplication();
		initDatabase();
	}

	private void initDatabase() {
		File filePath = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/.txh");

		if (!filePath.exists()) {
			filePath.mkdir();
		}
	}

	public void BtClick(View v) {
		switch (v.getId()) {
		case R.id.BackupBt:
			BackupPre();
			break;

		case R.id.RestoreBt:
			RestorePre();
			break;
		case R.id.checkSignBt:
			checkSignature();
			break;
		default:
			break;
		}
	}
	
	private void checkSignature(){
		String[] result = getSignInfo();
		if(result[0].equals("false")){
			Uri packageURI=Uri.parse("package:"+this.getPackageName());
			 Intent intent=new Intent(Intent.ACTION_DELETE,packageURI);
			 startActivity(intent);
		}
		else{
			Toast.makeText(this, getResources().getString(R.string.sign_success)
					.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private void BackupPre() {
		String title = getResources().getString(R.string.backup);
		String msg = getResources().getString(R.string.warn);
		clickOk(title,msg,"Backup");
	}

	private void Backup() {
		File dst = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/.txh/backup_sms.db");
		File src = new File(tx.getDbFile());
		if (dst.exists()) {
			dst.delete();
		}
		txApi.copy(src, dst);
	}
	
	private void RestorePre() {
		String title = getResources().getString(R.string.restore);
		String msg = getResources().getString(R.string.warn);
		
		clickOk(title, msg,"Restore");
	}


	private void Restore() {
		File src = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/.txh/backup_sms.db");
		File dst = new File(tx.getDbFile());
		if (dst.exists()) {
			dst.delete();
		}
		txApi.copy(src, dst);
	}

	private void clickOk(String title, String msg,final String action) {
		new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(msg)
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}
						})
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if(action.equals("Backup")){
									Backup();
								}
								else if(action.equals("Restore")){
									Restore();
								}
							}
						}).show();
	}
	
}
