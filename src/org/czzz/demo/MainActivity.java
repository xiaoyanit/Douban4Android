package org.czzz.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnTxt, btnImg;
	TextView webTxt;
	ImageView webImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnTxt = (Button) findViewById(R.id.fetch_txt);
		btnImg = (Button) this.findViewById(R.id.fetch_img);
		webTxt = (TextView) findViewById(R.id.web_txt);
		webImg = (ImageView) findViewById(R.id.web_img);

		btnTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(isNetworkOk())
//					fetchWebText("http://www.uxuan365.com");
			}

		});

		btnImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(isNetworkOk())
//					fetchWebImage("http://uxuan-pic.stor.sinaapp.com/header-bg.png");
			}

		});
		
		Button doubanBtn = (Button)findViewById(R.id.to_douban_act);
		doubanBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, DoubanActivity.class);
				startActivity(i);
			}
			
		});

	}

	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
				webTxt.setText("out: " + String.valueOf(msg.obj));
				break;
			case 1:
				webImg.setImageBitmap((Bitmap)(msg.obj));
				break;
			}
			super.handleMessage(msg);
		}
		 
	};
	

	public boolean isNetworkOk() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			Toast.makeText(this, "Network is disconnected", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
