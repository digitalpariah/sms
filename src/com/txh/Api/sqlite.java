/**
 * 2013-06-07 50:58
 * 
 * bbs.ijustyce.com by justyce
 */

package com.txh.Api;

import java.io.File;
import java.io.IOException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class sqlite{
	private static String tag = "txTag";
	private boolean use = false;
	SQLiteDatabase mSQLiteDatabase;
	Cursor cur;
	
	/**
	 * open or create sqlite file ! make sure it's parent folder is exists !
	 * @param dbFile
	 */
	
	private void openDb(String dbFile){
		
		while(use);
		use = true;
		Log.i(tag, "now dbFile in using ...");
		File f = new File(dbFile);
		if (!f.exists()){
			try{
				f.createNewFile();
			}
			catch (IOException e)
			{}
		}
		mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(f, null);
		Log.i(tag, "openfile");
	}
	
	private void closeDb() {
		
		use = false;
		Log.i(tag, "now dbFile finish using ...");
		mSQLiteDatabase.close();
	}
	
	/**
	 * create table of a sqlite file !
	 * @param sql
	 * @param dbFile
	 */
	
	public void createTable(String sql,String dbFile){
		
		openDb(dbFile);
		mSQLiteDatabase.execSQL(sql);
		closeDb();
		Log.i(tag, "create table if not exist !");
	}
	
	/**
	 * insert data to sqlite !
	 * @param dbFile
	 * @param table
	 * @param value
	 * @param column
	 */
	
	public void insertData(String dbFile,String table,String []value,String[]column){
		
		int i;
		openDb(dbFile);
		ContentValues cv = new ContentValues();
		for(i=0;i<value.length;i++){
			cv.put(column[i], value[i]);
		}
		mSQLiteDatabase.insert(table, null, cv);
		closeDb();
		Log.i(tag, "insert value to: "+table);
	}
	
	/**
	 * update sqlite
	 * @param dbFile
	 * @param table
	 * @param value
	 * @param column
	 * @param args
	 * @param sql
	 */
	
	public void update(String dbFile,String table,String []value,String[]column,
			String args[],String sql){
		
		int i;
		openDb(dbFile);
		ContentValues cv = new ContentValues();
		for(i=0;i<value.length;i++){
			cv.put(column[i], value[i]);
		}
		mSQLiteDatabase.update(table, cv,sql,args);
		closeDb();
		Log.i(tag, "update value: "+table);
	}
	
	/**
	 * read by row
	 * @param dbFile
	 * @param table
	 * @param sql
	 * @param args
	 * @param column
	 * @return
	 */
	
	public String[][] getData(String dbFile,String table,String sql,
			String[]args,String[]column){
		
		int i , j = 0;
		openDb(dbFile);
		Log.i("---Api---", sql);
		cur = mSQLiteDatabase.rawQuery(sql, args);
		String[][] result = new String[cur.getCount()][column.length];
		if (cur.moveToFirst()) {
			do {
				for(i=0;i<column.length;i++){
					int index = cur.getColumnIndex(column[i]);
					result[j][i] = cur.getString(index);
				}
				j++;
			} while (cur.moveToNext());
		}
		cur.close();
		closeDb();
		Log.i(tag, "get value of : "+table);
		return result;
	}
	
	/**
	 * delete data
	 * @param dbFile
	 * @param table
	 * @param sql
	 * @param args
	 */
	public void delete(String dbFile,String table,String sql,String[]args){
		openDb(dbFile);
		
		mSQLiteDatabase.delete(table, sql, args);
		closeDb();
		Log.i(tag, "delete value of :"+table);
	}
	
	/**
	 * check if a value is exist !
	 * @param dbFile
	 * @param table
	 * @param column
	 * @param value
	 * @return
	 */
	public boolean exists(String dbFile,String table,String column,String value){
		
		openDb(dbFile);
		boolean result = false;
		cur = mSQLiteDatabase.rawQuery("select * from "+table, null);
		int index = cur.getColumnIndex(column);
		if(cur.moveToFirst()){
			do{
				String text = cur.getString(index);
				if(value.equals(text)){
					result = true;
				}
			}while(cur.moveToNext());
		}
		cur.close();
		closeDb();
		Log.i(tag, "check if exist value of :"+table);
		return result;
	}
	
	/**
	 * check if a value is contained !
	 * @param dbFile
	 * @param table
	 * @param column
	 * @param value
	 * @return
	 */
	public boolean contains(String dbFile,String table,String column,String value){
		openDb(dbFile);
		boolean result = false;
		cur = mSQLiteDatabase.rawQuery("select * from "+table, null);
		int index = cur.getColumnIndex(column);
		if(cur.moveToFirst()){
			do{
				String text = cur.getString(index);
				if(value.contains(text)){
					result = true;
				}
			}while(cur.moveToNext());	
		}
		cur.close();
		closeDb();
		Log.i(tag, "value of :"+table +" is contained :" + String.valueOf(result));
		return result;
	}
}