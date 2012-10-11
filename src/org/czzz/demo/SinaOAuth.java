package org.czzz.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;

import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;

public class SinaOAuth {
	
	public static final String SINA_OAUTH_CODE_BASE_URL = "https://api.weibo.com/oauth2/authorize";
	public static final String SINA_OAUTH_TOKEN_BASE_URL = "https://api.weibo.com/oauth2/access_token"; 
	
	public static final String APP_KEY = "931381862";
	
	public static final String APP_SECRET = "277aec0e9ef5a5bec4bc48dadc595916";
	
	public static final String REDIRECT_URL = "http://www.sina.com";
	
	Weibo weibo;
	
	/**
	 * 打开网页，豆瓣认证，回调context的onNewIntent方法
	 * @param context
	 */
	public void lauchforVerifyCode(Context context){
//		Uri uri = Uri.parse(
//				SINA_OAUTH_CODE_BASE_URL 
//				+ "?client_id=" + APP_KEY 
//				+ "&redirect_uri=" + REDIRECT_URL 
//				+ "&response_type=code");
//		Intent it = new Intent(Intent.ACTION_VIEW, uri);
//		context.startActivity(it);
		
		weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(APP_KEY, APP_SECRET);
		weibo.setRedirectUrl(REDIRECT_URL);
		// 认证
//		weibo.authorize((Activity)context, new WeiboDialogListener());
		
	}
	
	/**
	 * 通过第一步的code，获取accessToken
	 * @param code 第一步认证的code
	 * @param listener AsynTask回调监听:listener的type为1
	 */
	public void fetchAccessToken(String code, HttpListener listener) {

		List<NameValuePair> params = null;
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("client_id",
				APP_KEY));
		params.add(new BasicNameValuePair("client_secret", APP_SECRET));
		params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URL));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", code));

		new HttpPostTask(listener).execute(SINA_OAUTH_TOKEN_BASE_URL, params);
	}

}
