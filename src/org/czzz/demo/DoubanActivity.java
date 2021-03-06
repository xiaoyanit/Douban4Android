package org.czzz.demo;

import java.util.ArrayList;
import java.util.List;

import org.czzz.demo.zxing.CaptureActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 
 * @author tinyao
 *
 */

public class DoubanActivity extends Activity{

	private EditText isbnEdt;
	private EditText uidEdt;
	private TextView bookInfoTv;
	private ImageView bookCover;
	
	private DoubanBook book;
	
	private ProgressDialog pd;
	
	private DoubanOAuth dbOAuth;
	
	private DoubanUser user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.douban);
		
		isbnEdt = (EditText)findViewById(R.id.edt_book_isbn);
		uidEdt = (EditText)findViewById(R.id.edt_user_id);
		bookInfoTv = (TextView)findViewById(R.id.book_info);
		bookCover = (ImageView)findViewById(R.id.book_cover);
		
		dbOAuth = new DoubanOAuth(this);
		user = new DoubanUser();
		
		Button fetchInfoBtn = (Button)findViewById(R.id.btn_fetch_book);
		Button dbOAuthBtn = (Button)findViewById(R.id.btn_douban_oauth);
		Button dbUserBtn = (Button)findViewById(R.id.btn_douban_user);
		Button bookColBtn = (Button)findViewById(R.id.btn_book_collect);
		Button bookComBtn = (Button)findViewById(R.id.btn_book_comm);
		Button contactBtn = (Button)findViewById(R.id.btn_user_contact);
		Button scanQRBtn = (Button)findViewById(R.id.btn_scan_qrcode);
		Button searchBtn = (Button)findViewById(R.id.btn_key_search);
		
		dbOAuthBtn.setOnClickListener(btnlistener);
		dbUserBtn.setOnClickListener(btnlistener);
		contactBtn.setOnClickListener(btnlistener);
		bookColBtn.setOnClickListener(btnlistener);
		bookComBtn.setOnClickListener(btnlistener);
		fetchInfoBtn.setOnClickListener(btnlistener);
		scanQRBtn.setOnClickListener(btnlistener);
		searchBtn.setOnClickListener(btnlistener);
		
	}

	
	public View.OnClickListener btnlistener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(!NetUtils.isNetworkOk(DoubanActivity.this)) return;
			
			switch(v.getId()){
			case R.id.btn_douban_oauth:
				dbOAuth.lauchforVerifyCode(DoubanActivity.this);
				break;
			case R.id.btn_douban_user:
				fetchUserInfo(uidEdt.getText().toString());
				break;
			case R.id.btn_user_contact:
				fetchUserContacts(uidEdt.getText().toString());
				break;
			case R.id.btn_fetch_book:
				fetchBookInfo(isbnEdt.getText().toString());
				break;
			case R.id.btn_book_comm:
				fetchBookComments(isbnEdt.getText().toString());
				break;
			case R.id.btn_book_collect:
				fetchBookCollection(uidEdt.getText().toString());
				break;
			case R.id.btn_scan_qrcode:
				Intent i = new Intent(DoubanActivity.this, CaptureActivity.class);
				startActivityForResult(i,0);
				break;
			case R.id.btn_key_search:
				EditText edt = (EditText)findViewById(R.id.edt_book_key);
				searchBooks(edt.getText().toString());
				break;
			}
		}
	};
	
	
	/**
	 * 根据isbn从豆瓣获取书籍文字信息
	 * @param isbn
	 */
	protected void fetchBookInfo(String isbn) {
		// TODO Auto-generated method stub
		pd = new ProgressDialog(this);
		pd.setMessage("正在从豆瓣获取图书信息...");
		pd.show();
		HttpTaskListener bookInfoListener = new HttpTaskListener(HttpListener.FETCH_BOOK_INFO);
		DoubanBookUtils.fetchBookInfo(isbn, bookInfoListener);
	}
	
	/**
	 * 根据url从豆瓣获取书籍封面
	 * @param url
	 */
	protected void fetchBookCover(String url) {
		// TODO Auto-generated method stub
		HttpTaskListener bookCoverListener = new HttpTaskListener(HttpListener.FETCH_BOOK_COVER);
		DoubanBookUtils.fetchBookCover(url, bookCoverListener);
	}

	/**
	 * 根据userId获取豆瓣用户信息
	 * @param userid
	 */
	protected void fetchUserInfo(String userid){
		pd = new ProgressDialog(this);
		pd.setMessage("正在获取用户信息...");
		pd.show();
		HttpTaskListener userInfoListener = new HttpTaskListener(HttpListener.FETCH_USER_INFO);
		if(dbOAuth.getAccessToken().equals(""))
			DoubanUser.fetchUserInfo(userid, userInfoListener);
		else
			DoubanUser.fetchUserInfo(userid, userInfoListener, dbOAuth.getAccessToken());
	}
	
	/**
	 * 根据userId获取书籍收藏
	 * @param userid
	 */
	protected void fetchBookCollection(String userid){
		pd = new ProgressDialog(this);
		pd.setMessage("正在获取用户藏书...");
		pd.show();
		HttpTaskListener listener = new HttpTaskListener(HttpListener.FETCH_BOOK_COLLECTION);
		DoubanBookUtils.fetchBookCollection(listener, listener.type, userid);
	}
	
	/**
	 * 根据isbn获取书籍评论
	 * @param isbn
	 */
	protected void fetchBookComments(String isbn) {
		// TODO Auto-generated method stub
		pd = new ProgressDialog(this);
		pd.setMessage("正在从豆瓣获取书籍评论...");
		pd.show();
		HttpTaskListener listener = new HttpTaskListener(HttpListener.FETCH_BOOK_COMMENTS);
		DoubanBookUtils.fetchBookComments(listener, listener.type, isbn);
	}

	/**
	 * fetch catacts of the user
	 * @param uid
	 */
	protected void fetchUserContacts(String uid){
		pd = new ProgressDialog(this);
		pd.setMessage("正在获取用户关注列表...");
		pd.show();
		HttpTaskListener listener = new HttpTaskListener(HttpListener.FETCH_USER_CONTACTS);
		DoubanUser.fetchUserContacts(uid, listener, listener.type);
	}
	
	protected void searchBooks(String keyword){
		pd = new ProgressDialog(this);
		pd.setMessage("正在从豆瓣搜索相关图书...");
		pd.show();
		HttpTaskListener booksearchListener = new HttpTaskListener(HttpListener.SEARCH_BOOKS);
		DoubanBookUtils.searchBooks(keyword, booksearchListener);
	}
	
	/**
	 * 豆瓣认证完成后，获取返回的code
	 * @param intent
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d("DEBUG", "onNewIntent---");
		//在这里处理获取返回的code参数
		Uri uri = intent.getData();
		String code = uri.getQueryParameter("code");
		bookInfoTv.append("code: " + code + "\n");
		HttpTaskListener taskListener = new HttpTaskListener(HttpListener.DOUBAN_OAUTH_JSON);
		dbOAuth.fetchAccessToken(code, taskListener);
	}
	
	
	
	/**
	 * Http请求监听器，用于处理HttpAsyncTask中的响应事件
	 * @author tinyao
	 *
	 */
	private class HttpTaskListener implements HttpListener{

		int type;
		
		public HttpTaskListener(int type1){
			this.type = type1;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void onTaskCompleted(Object data) {
			// TODO Auto-generated method stub
			switch(type){
			case HttpListener.DOUBAN_OAUTH_JSON: // 获取到access token等json
				// 解析json，获取accessToken
				dbOAuth.parseJson4OAuth(String.valueOf(data));
				bookInfoTv.append("token: " + dbOAuth.getAccessToken() 
						+ "\nuser_id: " + dbOAuth.getDoubanUserId());
				break;
			case HttpListener.FETCH_BOOK_INFO: // 获取到书籍信息
				if(pd != null) pd.dismiss();
				if(data == null){
					Toast.makeText(DoubanActivity.this, "book not found", Toast.LENGTH_SHORT).show();
					break;
				}
				book = DoubanBookUtils.parseBookInfo(String.valueOf(data));
				bookInfoTv.setText("book:\n" + book);
				fetchBookCover(book.image);
				break;
			case HttpListener.FETCH_BOOK_COVER:	// 获取到书籍封面
				bookCover.setImageBitmap((Bitmap)data);
				break;
			case HttpListener.FETCH_USER_INFO: // 获取用户信息
				if(pd != null) pd.dismiss();
				user = new DoubanUser();
				// 解析成功 boolean
				if(user.parse4User(String.valueOf(data)))
					bookInfoTv.setText("user:\n" + user);
				else
					Toast.makeText(DoubanActivity.this, "error: user not found!", Toast.LENGTH_SHORT).show();
				break;
			case HttpListener.FETCH_BOOK_COLLECTION:	//获取用户书籍收藏, 用户属性之一
				if(pd != null) pd.dismiss();
				StringBuilder collectionsBuilder = new StringBuilder();
				if(data == null){
					Toast.makeText(DoubanActivity.this, "error: user not found !", Toast.LENGTH_SHORT).show();
					break;
				}
				for(BookCollectionEntry entry : (List<BookCollectionEntry>)data){
					collectionsBuilder.append(entry + "\n\n");
				}
				bookInfoTv.setText("book-collections:\n" + collectionsBuilder.toString());
				break;
			case HttpListener.FETCH_BOOK_COMMENTS:		//获取书籍评论
				if(pd != null) pd.dismiss();
				StringBuilder commentsBuilder = new StringBuilder();
				if(data == null){
					Toast.makeText(DoubanActivity.this, "error: book not found !", Toast.LENGTH_SHORT).show();
					break;
				}
				for(BookCommentEntry entry : (List<BookCommentEntry>)data){
					commentsBuilder.append(entry + "\n\n");
				}
				bookInfoTv.setText("book-comments:\n" + commentsBuilder.toString());
				break;
			case HttpListener.FETCH_USER_CONTACTS:
				if(pd != null) pd.dismiss();
				StringBuilder contactsBuilder = new StringBuilder();
				if(data == null){
					Toast.makeText(DoubanActivity.this, "error: user not found !", Toast.LENGTH_SHORT).show();
					break;
				}
				for(DoubanUser us : (List<DoubanUser>)data){
					contactsBuilder.append(us + "\n\n");
				}
				bookInfoTv.setText("contacts:\n" + contactsBuilder.toString());
				break;
			case HttpListener.SEARCH_BOOKS:
				if(data == null){
					Toast.makeText(DoubanActivity.this, "book not found", Toast.LENGTH_SHORT).show();
					break;
				}
				
				bookInfoTv.setText("search books: ");
				
				try {
					JSONObject json = new JSONObject(String.valueOf(data));
					bookInfoTv.append("count: " + json.getString("count")
							+ "\t\t total: " + json.getString("total"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ArrayList<DoubanBook> list = DoubanBookUtils.parseSearchBooks(String.valueOf(data));
				if(pd != null) pd.dismiss();
				
				for(DoubanBook book : list){
					bookInfoTv.append("\n\n==========================\n" + book);
				}
				
//				bookInfoTv.setText("book:\n" + data);
				break;
			}
		}

		@Override
		public void onTaskFailed(String data) {
			// TODO Auto-generated method stub
			Toast.makeText(DoubanActivity.this, "error: " + data, Toast.LENGTH_SHORT).show();
		}
		
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(resultCode == RESULT_OK){
			String codeFormat = data.getStringExtra("bracode_format");
			String codeText = data.getStringExtra("bracode_text");
			
			if(codeFormat.contains("EAN")){
				isbnEdt.setText(codeText);
				fetchBookInfo(codeText);
			}else{
				Toast.makeText(this, "not isbn for book", Toast.LENGTH_SHORT).show();
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	
}
