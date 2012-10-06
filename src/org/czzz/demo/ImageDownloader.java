package org.czzz.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloader extends AsyncTask<Object, Object, Object> {

	HttpListener taskListener;
	
	public ImageDownloader(HttpListener taskListener){
		this.taskListener = taskListener;
	}
	
	@Override
	protected Object doInBackground(Object... urls) {
		// TODO Auto-generated method stub
		try {
			return downloadImage(String.valueOf(urls[0]));
		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}

	/**
	 * result 返回的Bitmap对象
	 */
	@Override
	protected void onPostExecute(Object bitmap) {
//		Message msg = handler.obtainMessage();
//		msg.what = 1;
//		msg.obj = bitmap;
//		handler.sendMessage(msg);
		taskListener.onTaskCompleted(bitmap);
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as a InputStream, which it returns as
	// a string.
	private Bitmap downloadImage(String myurl) throws IOException {
		InputStream is = null;

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
			is = conn.getInputStream();

			// Convert the InputStream into a bitmap
			Bitmap bm = convertInputStreamToBitmap(is);
			return bm;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	public Bitmap convertInputStreamToBitmap(InputStream is){
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		return bitmap;
	}

}
