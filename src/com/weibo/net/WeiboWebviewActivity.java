package com.weibo.net;

import org.czzz.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

/***
 * 自定义webview(Oauth认证)
 * 
 * @author zhangjia
 * 
 */
public class WeiboWebviewActivity extends Activity implements WeiboDialogListener {
	private final String TAG = "jjhappyforever";
	private WebView mWebView;
	private WeiboDialogListener mListener;
	private LinearLayout linearLayout;
	
	private String mUrl;

	public void InitWebView() {
		
		mWebView = (WebView) findViewById(R.id.webv);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WeiboWebViewClient());
		mWebView.loadUrl(mUrl);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListener = this;
		setContentView(R.layout.web_view);
		
		Intent i = getIntent();
		mUrl = i.getStringExtra("url");
		
		InitWebView();

	}

	/***
	 * WebViewClient
	 * 
	 * @author zhangjia
	 * 
	 */
	private class WeiboWebViewClient extends WebViewClient {

		/***
		 * 地址改变都会调用
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirect URL: " + url);
			if (url.startsWith(Weibo.getInstance().getRedirectUrl())) {
				handleRedirectUrl(view, url);
				return true;
			}
			// launch non-dialog URLs in a full browser
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			mListener.onError(new DialogError(description, errorCode,
					failingUrl));
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "onPageStarted URL: " + url);
			// google issue. shouldOverrideUrlLoading not executed
			/**
			 * 点击授权，url正确
			 */
			if (url.startsWith(Weibo.getInstance().getRedirectUrl())) {
				handleRedirectUrl(view, url);
				view.stopLoading();

				return;
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.d(TAG, "onPageFinished URL: " + url);
			super.onPageFinished(view, url);

			linearLayout.setVisibility(View.GONE);
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
		}

	}

	private void handleRedirectUrl(WebView view, String url) {
		Bundle values = Utility.parseUrl(url);
		String error = values.getString("error");
		String error_code = values.getString("error_code");
		// 授权成功
		if (error == null && error_code == null) {
			mListener.onComplete(values);
			// 拒绝失败等
		} else if (error.equals("access_denied")) {
			mListener.onCancel();
		} else {
			// 异常
			mListener.onWeiboException(new WeiboException(error, Integer
					.parseInt(error_code)));
		}
	}

	@Override
	public void onComplete(Bundle values) {
		/***
		 * 在这里要save the access_token
		 */
		String token = values.getString("access_token");

//		SharedPreferences preferences = getSharedPreferences(MainActivity.file,
//				0);
//		Editor editor = preferences.edit();
//		editor.putString("token", token);
//		editor.commit();

		AccessToken accessToken = new AccessToken(token, Weibo.getAppSecret());
		Weibo.getInstance().setAccessToken(accessToken);
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onWeiboException(WeiboException e) {
		e.printStackTrace();
	}

	@Override
	public void onError(DialogError e) {
		e.printStackTrace();
	}

	@Override
	public void onCancel() {
		finish();
	}

}
