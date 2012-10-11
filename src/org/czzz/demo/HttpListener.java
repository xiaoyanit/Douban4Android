package org.czzz.demo;

public interface HttpListener {

	public static final int DOUBAN_OAUTH_CODE = 0;
	public static final int DOUBAN_OAUTH_JSON =1;
	public static final int FETCH_BOOK_INFO = 2;
	public static final int FETCH_BOOK_COVER = 3;
	public static final int FETCH_USER_INFO = 4;
	
	public static final int FETCH_BOOK_COLLECTION = 5;
	public static final int FETCH_BOOK_COMMENTS = 6;
	
	public static final int FETCH_USER_CONTACTS = 7;
	
	public static final int FETCH_SINA_PLACES = 8;
	
	public static final int SEARCH_BOOKS = 9;
	
	public void onTaskCompleted(Object data);
	
	public void onTaskFailed(String data);
	
}
