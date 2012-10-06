package org.czzz.demo;

import org.json.JSONException;
import org.json.JSONObject;

public class DoubanBookUtils {

	public static void fetchBookInfo(String isbn, HttpListener listener){
		String url = "https://api.douban.com/v2/book/isbn/" + isbn;
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
	
	public static void fetchBookCollection(HttpListener listener, int type, String uid){
		String url = "http://api.douban.com/people/" + uid + "/collection?cat=book&max-results=1000";
		new XmlDownloadTask(listener, type).execute(url);
	}
	
	public static void fetchBookComments(HttpListener listener, int type, String isbn){
		String url = "http://api.douban.com/book/subject/isbn/" + isbn + "/reviews";
		new XmlDownloadTask(listener, type).execute(url);
	}
	
}
