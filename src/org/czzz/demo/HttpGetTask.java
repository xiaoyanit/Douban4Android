package org.czzz.demo;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class HttpGetTask extends AsyncTask<String, String, String> {

	HttpListener taskListener;

	public HttpGetTask(HttpListener taskListener) {
		this.taskListener = taskListener;
	}

	@Override
	protected String doInBackground(String... datas) {
		// TODO Auto-generated method stub
		HttpGet httpGet = new HttpGet(String.valueOf(datas[0]));
		HttpResponse getResponse;
		String result = "";
		
		if(datas.length > 1){
			httpGet.addHeader("Authorization", "Bearer " + datas[1]);
		}
		
		try {
			getResponse = new DefaultHttpClient().execute(httpGet);
			HttpEntity resEntityGet = getResponse.getEntity();
			if(resEntityGet != null){
				result = EntityUtils.toString(resEntityGet);
				return result;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		taskListener.onTaskCompleted(result);
		super.onPostExecute(result);
	}
	
	

}
