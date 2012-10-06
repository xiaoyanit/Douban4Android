package org.czzz.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * 豆瓣OAuth登录 参数及方法
 * @author tinyao
 *
 */
public class DoubanOAuth {

	public static final String DOUBAN_OAUTH_TOKEN_BASE_URL = "https://www.douban.com/service/auth2/token";
	public static final String DOUBAN_OAUTH_CODE_BASE_URL = "https://www.douban.com/service/auth2/auth";
	
	public static final String DOUBAN_PREF = "douban_pref";
	
	public static final String APP_KEY = "0a02a555b6e4eafc1e07f00124aec0f4";
	public static final String APP_SECRET = "986d4d0182357cca";
	public static final String REDIRECT_URL = "czzz://callback";
	
	private String accessToken = "";
	private String doubanUserId = "";
	private String expiresIn = "";
	private String refreshToken = "";
	
	Context context;
	SharedPreferences sp;
	
	public DoubanOAuth(Context con){
		this.context = con;
		sp = con.getSharedPreferences(DOUBAN_PREF, 0);
		accessToken = sp.getString("access_token", "");
		doubanUserId = sp.getString("user_id", "");
		expiresIn = sp.getString("expires_in", "");
		refreshToken = sp.getString("refresh_token", "");
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getDoubanUserId() {
		return doubanUserId;
	}

	public void setDoubanUserId(String doubanUserId) {
		this.doubanUserId = doubanUserId;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * 打开网页，豆瓣认证，回调context的onNewIntent方法
	 * @param context
	 */
	public void lauchforVerifyCode(Context context){
		Uri uri = Uri.parse(
				DOUBAN_OAUTH_CODE_BASE_URL 
				+ "?client_id=" + APP_KEY 
				+ "&redirect_uri=" + REDIRECT_URL 
				+ "&response_type=code");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
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

		new HttpPostTask(listener).execute(DOUBAN_OAUTH_TOKEN_BASE_URL, params);
	}

	/**
	 * 从json中解析出accessToken等信息，并存入sharedpreferences
	 * @param json
	 */
	public void parseJson4OAuth(String json){
		
		try {
			JSONObject js = new JSONObject(json);
			
			accessToken = js.getString("access_token");
			doubanUserId = js.getString("douban_user_id");
			expiresIn = js.getString("expires_in");
			refreshToken = js.getString("refresh_token");
			
			sp = context.getSharedPreferences(DOUBAN_PREF, 0);
			sp.edit().putString("access_token", accessToken)
					.putString("user_id", doubanUserId)
					.putString("expires_in", expiresIn)
					.putString("refresh_token", refreshToken).commit();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
