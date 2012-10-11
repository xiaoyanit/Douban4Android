package org.czzz.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class SinaPlaceActivity extends Activity {

	TextView tv;
	Weibo weibo;
	SharedPreferences sp;
	String accessToken;
	ProgressDialog pd;
	
	LocationManager lm;
	MylocationListener locationListener;
	
	boolean hasLocated = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sina_place);

		tv = (TextView) findViewById(R.id.sina_result);
		
		weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(SinaOAuth.APP_KEY,
				SinaOAuth.APP_SECRET);
		weibo.setRedirectUrl(SinaOAuth.REDIRECT_URL);
		
		sp = this.getSharedPreferences("sina_token", 0);
		accessToken = sp.getString("access_token", "");

		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MylocationListener();

		Button sinaOAuth = (Button) findViewById(R.id.sina_oauth);
		sinaOAuth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 认证
				weibo.authorize(SinaPlaceActivity.this,
						new MyWeiboDialogListener());
			}

		});

		Button sinaPlace = (Button) findViewById(R.id.sina_place);
		sinaPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fetchCurrentLocation();
			}

		});
		
		Button gpsLocation = (Button)findViewById(R.id.gps_locaton);
		gpsLocation.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fetchCurrentLocation();
			}
			
		});
		

	}

	
	public void fetchCurrentLocation(){
		
		if(lm == null){
			lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		}
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3600*60*24, 100000000, new MylocationListener());
		
	}
	
	/**
	 * 用于位置定位的回调监听
	 * @author tinyao
	 *
	 */
	class MylocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			tv.setText("location:\n" + location.getLatitude() + ":" + location.getLongitude());
			
			
			pd = new ProgressDialog(SinaPlaceActivity.this);
			pd.setMessage("正在获取附近地点...");
			pd.show();
			
			List<NameValuePair> params = new LinkedList<NameValuePair>();
			params.add(new BasicNameValuePair("lat", "" + location.getLatitude()));
			params.add(new BasicNameValuePair("long", "" + location.getLongitude()));
			params.add(new BasicNameValuePair("range", "2000"));
			params.add(new BasicNameValuePair("count", "20"));
			params.add(new BasicNameValuePair("access_token", accessToken));
			
			HttpTaskListener placesListener = new HttpTaskListener(HttpListener.FETCH_SINA_PLACES);
			PlacesUtils.getPlacesNearby(SinaPlaceActivity.this, params, placesListener);

			lm.removeUpdates(locationListener);
			lm = null;
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * 用于新浪微博OAuth认证的回调监听器
	 * @author tinyao
	 *
	 */
	class MyWeiboDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			/***
			 * 保存token and expires_in
			 */

			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			
			sp = SinaPlaceActivity.this.getSharedPreferences("sina_token", 0);
			sp.edit().putString("access_token", token)
				.putString("expires_in", expires_in).commit();
			
			tv.setText("access_token : " + token + "  expires_in: "
					+ expires_in);
			AccessToken accessToken = new AccessToken(token,
					SinaOAuth.APP_SECRET);
			accessToken.setExpiresIn(expires_in);
			weibo.setAccessToken(accessToken);

		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

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
		
		@Override
		public void onTaskCompleted(Object data) {
			// TODO Auto-generated method stub
			switch(type){
			case HttpListener.FETCH_SINA_PLACES:
				
				String rrt = PlacesUtils.unicodeToString(String.valueOf(data));
				// 解析返回的json字符串
				try {
					ArrayList<Map<String,String>> list = PlacesUtils.parsePlacesNearby(rrt);
					
					tv.setText("places:");
					for(Map<String,String> map : list){
						tv.append("\n\n" + map);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if(pd != null){
					pd.dismiss();
				}
				
				break;
			}
			
		}

		@Override
		public void onTaskFailed(String data) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
