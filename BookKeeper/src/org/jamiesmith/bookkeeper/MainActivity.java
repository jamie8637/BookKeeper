package org.jamiesmith.bookkeeper;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



@SuppressLint("NewApi")
public class MainActivity extends ListActivity {

	Intent intent;
	TextView bookId;	
	DBTools dbTools = new DBTools(this);	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try {
			SharedPreferences prefs = getSharedPreferences("myDataStorage", MODE_PRIVATE);
			dbTools.setOrder(prefs.getString("order", ""));
		} catch (Exception e) {	}
		listSetup();
		}
	
	public void showAddBook(View view){		
		Intent theIntent = new Intent(getApplication(), NewBook.class);		
		startActivity(theIntent);
	}
	
	public void aToZ(View view){		
		SharedPreferences prefs = getSharedPreferences("myDataStorage", MODE_PRIVATE);
		Editor mEditor = prefs.edit();
		mEditor.putString("order", "az");
		mEditor.apply();
		dbTools.setOrder("az");
		listSetup();
	}
	
	public void zToA(View view){
		SharedPreferences prefs = getSharedPreferences("myDataStorage", MODE_PRIVATE);
		prefs = getSharedPreferences("myDataStorage", MODE_PRIVATE);
		Editor mEditor = prefs.edit();
		mEditor.putString("order", "za");
		mEditor.apply();
		dbTools.setOrder("za");
		listSetup();
	}
	
	private void listSetup(){		
		ArrayList<HashMap<String, String>> bookList = dbTools.getAllBooks();
		dbTools.getAllBooks();
		
		if(bookList.size() != 0){
			ListView listView = getListView();
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {					
					bookId = (TextView) view.findViewById(R.id.bookId);					
					String bookIdValue = bookId.getText().toString();					
					Intent theIntent = new Intent(getApplication(), EditBook.class);					
					theIntent.putExtra("bookId", bookIdValue);					
					startActivity(theIntent);					
				}				
			});
			
			ListAdapter adapter = new SimpleAdapter(
					MainActivity.this, bookList, R.layout.book_entry, 
					new String[] {"bookId", "title"}, 
					new int[] {R.id.bookId, R.id.title});
			
			setListAdapter(adapter);
		}
	}
}
