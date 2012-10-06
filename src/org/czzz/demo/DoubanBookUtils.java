package org.czzz.demo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

public class DoubanBookUtils {

	public static void fetchBookInfo(String url, HttpListener listener){
		new HttpDownloadAsyncTask(listener).execute(url);
	}
	
	public static void fetchBookCover(String url, HttpListener listener){
		new ImageDownloader(listener).execute(url);
	}
	
	/**
	 * 从json解析，获取Book对象
	 * @param jsonStr
	 * @return
	 */
	public static DoubanBook parseBookInfo(String jsonStr){
		
		JSONObject json = null;
		DoubanBook book = new DoubanBook();
		try {
			json = new JSONObject(jsonStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(json!=null) {
			book.init(json);
			return book;
		}
		
		return null;
	}
	
}
