package org.jamiesmith.bookkeeper;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBTools extends SQLiteOpenHelper{
	
	private String order;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	public DBTools(Context applicationcontext){
		
		super(applicationcontext, "bookkeeper4.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		String query = "CREATE TABLE books (bookId INTEGER PRIMARY KEY, title TEXT)";
		
		database.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
		String query = "DROP TABLE IF EXISTS bookkeeper3";
		
		database.execSQL(query);
		onCreate(database);
	}
	
	public void insertBook(HashMap<String, String> queryValues){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("title", queryValues.get("title"));
		
		database.insert("books", null, values);
		
		database.close();
	}
	
	public int updateBook(HashMap<String, String> queryValues){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("title", queryValues.get("title"));
		
		return database.update("books", values, "bookId" + " = ?", new String[] {queryValues.get("bookId")});
	}
	
	public void deleteBook(String id){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String deleQuery = "DELETE FROM books WHERE bookId='" + id + "'";
		
		database.execSQL(deleQuery);
	}
	
	public ArrayList<HashMap<String, String>> getAllBooks(){
		ArrayList<HashMap<String, String>> bookArrayList = new ArrayList<HashMap<String,String>>();

		String selectQuery = null;
		
		//if(order.equals("az")) {selectQuery = "SELECT * FROM books ORDER BY title";}
		if(order.equals("za")) {selectQuery = "SELECT * FROM books ORDER BY title DESC";}
		else {selectQuery = "SELECT * FROM books ORDER BY title";}
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{
				HashMap<String, String> bookMap = new HashMap<String, String>();
				
				bookMap.put("bookId", cursor.getString(0));
				bookMap.put("title", cursor.getString(1));
				
				bookArrayList.add(bookMap);								
			} while (cursor.moveToNext());
		}
		
		return bookArrayList;
	}

	public HashMap<String, String> getBookInfo(String id){
		HashMap<String, String> bookMap = new HashMap<String, String>();
		
		SQLiteDatabase database = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM books WHERE bookId='" + id + "'";
		
		Cursor cursor = database.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()){
			do{				
				bookMap.put("bookId", cursor.getString(0));
				bookMap.put("title", cursor.getString(1));
											
			} while (cursor.moveToNext());
		}
		
		return bookMap;
	}
	
	
}
