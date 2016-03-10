package org.jamiesmith.bookkeeper;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditBook extends Activity{
	
	EditText title;
	
	DBTools dbTools = new DBTools(this);
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_book);
		title = (EditText) findViewById(R.id.title);
		
		Intent theIntent = getIntent();
		
		String contactId = theIntent.getStringExtra("bookId");
		
		HashMap<String, String> contactList = dbTools.getBookInfo(contactId);
		
		if(contactList.size() != 0){
			title.setText(contactList.get("title"));			
		}		
	}
	
	public void editBook(View view){
		HashMap<String, String> queryValuesMap = new HashMap<String, String>();
		
		title = (EditText) findViewById(R.id.title);
		
		Intent theIntent = getIntent();
		
		String bookId = theIntent.getStringExtra("bookId");
		
		queryValuesMap.put("bookId", bookId);
		queryValuesMap.put("title", title.getText().toString());
		
		dbTools.updateBook(queryValuesMap);
		this.callMainActivity(view);		
	}
	
	public void removeBook(View view){
		Intent theIntent = getIntent();
		
		String bookId = theIntent.getStringExtra("bookId");
		
		dbTools.deleteBook(bookId);
		
		this.callMainActivity(view);
	}
	
	public void callMainActivity(View view){
		Intent objIntent = new Intent(getApplication(), MainActivity.class);
		
		startActivity(objIntent);
	}
}
