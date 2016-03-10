package org.jamiesmith.bookkeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

public class NewBook extends Activity implements Runnable{
	String isbnStr = "";
	String resultsStr = null;
    EditText isbn = null;
    EditText results = null;
	
	DBTools dbTools = new DBTools(this);
	
	final Handler mHandler = new Handler();
	final Runnable mUpdateResults = new Runnable() {
		@Override
		public void run() {
    		results.setText(resultsStr);
		}
	};
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_book);
		
		isbn = (EditText) findViewById(R.id.isbn);
        results = (EditText) findViewById(R.id.title);	
	}
	
	public void searchIsbn(View view){
		isbnStr = isbn.getText().toString();
		Thread thread = new Thread(NewBook.this);
		thread.start();
	}
	
	@Override
    public void run() {
		try {
			resultsStr = GetBookInfo(isbnStr);
			mHandler.post(mUpdateResults);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
	
	private String GetBookInfo (String symbols) throws MalformedURLException, IOException, JSONException
    {
    	StringBuilder response = new StringBuilder();
    	
    	// call web service to get results
    	String urlStr = "https://www.googleapis.com/books/v1/volumes?q=isbn:"+symbols;
    	URL url = new URL(urlStr);
    	HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    	if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
    		BufferedReader input = new BufferedReader(new InputStreamReader(httpConn.getInputStream()), 8192);
    		String strLine = null;
    		while ((strLine = input.readLine()) != null) {
    			response.append(strLine);
    			response.append("\n");
    		}
    		input.close();
    	}

    	String result = ProcessJSON(response.toString());
    	return result;
    }
    
    private String ProcessJSON (String json) throws IOException, JSONException
    {
    	String result = "";
    	try{
    	JSONObject responseObject = new JSONObject(json);
    	result = responseObject.getString("items");

    	JSONObject metaObject = new JSONObject(parseJSON(result));
    	metaObject = metaObject.getJSONObject("volumeInfo");
    	result = metaObject.getString("title");
    	
    	}catch (JSONException e) {
    		result = "error";
    	}    	
    	return result;
    }
    
    private String parseJSON (String parse)
    {
    	StringBuilder builder = new StringBuilder(parse);
    	builder = builder.deleteCharAt(0);
    	builder = builder.deleteCharAt(builder.length()-1);
    	parse = builder.toString();
		return parse;    	
    }   
	
	public void addNewBook(View view){
		HashMap<String, String> queryValuesMap = new HashMap<String, String>();
		
		queryValuesMap.put("title", results.getText().toString());
		
		dbTools.insertBook(queryValuesMap);
		
		this.callMainActivity(view);
	}

	public void callMainActivity(View view) {
		
		Intent theIntent = new Intent(getApplication(), MainActivity.class);
		startActivity(theIntent);
	}

}
