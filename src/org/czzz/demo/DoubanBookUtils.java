package org.czzz.demo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DoubanBookUtils {

	public static void fetchBookInfo(String isbn, HttpListener listener){
		String url = "https://api.douban.com/v2/book/isbn/" + isbn;
		new HttpDownloadAsyncTask(listener).execute(url);
	}
	
	public static void fetchBookCover(String url, HttpListener listener){
		new ImageDownloader(listener).execute(url);
	}
	
	public static void searchBooks(String keyword, HttpListener listener){
		String url = "https://api.douban.com/v2/book/search?";
		
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("q", keyword));
		
		String paramString = URLEncodedUtils.format(params, "utf-8");
		
		url += paramString;
		
		new HttpDownloadAsyncTask(listener).execute(url);
	}
	
	/**
	 * 从json解析，获取Book对象
	 * @param jsonStr
	 * @return
	 */
	public static DoubanBook parseBookInfo(String jsonStr){
		
		JSONObject json = null;
		Log.d("DEBUG", "jsonStr: " + jsonStr);
		DoubanBook book = new DoubanBook();
		try {
			json = new JSONObject(jsonStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(json!=null && !json.has("code")) {
			book.init(json);
			return book;
		}else{
			return null;
		}
		
	}
	
	public static ArrayList<DoubanBook> parseSearchBooks(String jsonStr){
		
		ArrayList<DoubanBook> list = new ArrayList<DoubanBook>();
		
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray books = json.getJSONArray("books");
			
			for(int i=0; i<books.length(); i++){
				DoubanBook book = new DoubanBook();
				book.init(new JSONObject(String.valueOf(books.get(i))));
				list.add(book);
			}
			
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void fetchBookCollection(HttpListener listener, int type, String uid){
		String url = "http://api.douban.com/people/" + uid + "/collection?cat=book&max-results=1000";
		new XmlDownloadTask(listener, type).execute(url);
	}
	
	public static void fetchBookComments(HttpListener listener, int type, String isbn){
		String url = "http://api.douban.com/book/subject/isbn/" + isbn + "/reviews";
		new XmlDownloadTask(listener, type).execute(url);
	}
	
}
