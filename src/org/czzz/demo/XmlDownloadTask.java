package org.czzz.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.czzz.demo.BookCollectionXmlParser.Entry;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;

public class XmlDownloadTask extends AsyncTask<Object, Object, Object>{

	HttpListener taskListener;
	int taskType;
	
	public XmlDownloadTask(HttpListener taskListener, int type) {
		this.taskListener = taskListener;
		this.taskType = type;
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
	private Object downloadUrl(String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 5000;

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
			
			switch(taskType){
			case HttpListener.FETCH_BOOK_COLLECTION:
				BookCollectionXmlParser xmlcollectoinParser = new BookCollectionXmlParser();
				List<BookCollectionEntry> collections = xmlcollectoinParser.parse(is);
				return collections;
			case HttpListener.FETCH_BOOK_COMMENTS:
				BookCommentXmlParser xmlcommentParser = new BookCommentXmlParser();
				List<BookCommentEntry> comments = xmlcommentParser.parse(is);
				return comments;
			default:
				return null;
			}
			
//			
//			if(taskType == HttpListener.FETCH_BOOK_COLLECTION){
//				BookCollectionXmlParser xmlParser = new BookCollectionXmlParser();
//				List<BookCollectionEntry> entries = xmlParser.parse(is);
//				
//				return entries;
////				// 获取书籍收藏 列表
////				
////				StringBuilder resb = new StringBuilder();
////				for(BookCollectionEntry entry : entries){
////					resb.append(entry + "\n\n");
////				}
////				return resb.toString();
//			}else if(taskType == HttpListener.FETCH_BOOK_COMMENTS){
//				BookCommentXmlParser xmlParser = new BookCommentXmlParser();
//				List<BookCommentEntry> entries = xmlParser.parse(is);
//				return entries;
//				// 获取书籍评论
//				
////				StringBuilder resb = new StringBuilder();
////				for(BookCommentEntry entry : entries){
////					resb.append(entry + "\n\n");
////				}
////				return resb.toString();
//			}else{
//				return "error: no such task type";
//			}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		
		return null;
	}

}
