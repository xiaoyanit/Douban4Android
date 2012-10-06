package org.czzz.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class HttpDownloadAsyncTask extends AsyncTask<Object, Object, Object> {

	HttpListener taskListener;

	public HttpDownloadAsyncTask(HttpListener taskListener) {
		this.taskListener = taskListener;
	}

	@Override
	protected Object doInBackground(Object... urls) {
		// TODO Auto-generated method stub
		try {
			return downloadUrl(String.valueOf(urls[0]));
		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		taskListener.onTaskCompleted(result);
		
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private String downloadUrl(String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.

		try {
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();
			Log.d("DEBUG_TAG", "The response is: " + response);
			
			if(response == 200){
				is = conn.getInputStream();
				// Convert the InputStream into a string
				String contentAsString = readIt(is);
				return contentAsString;
			}else{
				return null;
			}

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	// Reads an InputStream and converts it to a String.
	public String readIt(InputStream stream) throws IOException,
			UnsupportedEncodingException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				stream, "UTF-8"));
		StringBuilder sb = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}

		return sb.toString();
	}

}
