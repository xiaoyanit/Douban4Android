package org.czzz.demo;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class HttpPostTask extends AsyncTask<Object, String, String>{

	HttpListener taskListener;
	
	public HttpPostTask(HttpListener taskListener){
		this.taskListener = taskListener;
	}
	
	@Override
	protected String doInBackground(Object... data) {
		// TODO Auto-generated method stub
		
		String url = String.valueOf(data[0]);
		@SuppressWarnings("unchecked")
		List<NameValuePair> params = (List<NameValuePair>) data[1];
		
		HttpPost httpRequest = new HttpPost(url);
		HttpResponse httpResponse;
		
		try{
			//发出HTTP request 
            httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
            Log.d("DEBUG", "--- " + httpRequest.getURI());
            //取得HTTP response 
            httpResponse=new DefaultHttpClient().execute(httpRequest);
            if(httpResponse.getStatusLine().getStatusCode()==200){
            	return "200\t" + EntityUtils.toString(httpResponse.getEntity());
            }else{
            	return httpResponse.getStatusLine().getStatusCode()
            			+ "\t" + EntityUtils.toString(httpResponse.getEntity());
            }
            
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		String[] res = result.split("\t");
		if(res[0].equals("200"))
			taskListener.onTaskCompleted(res[1]);
		else
			taskListener.onTaskFailed(res[1]);
	}
	
	

}
