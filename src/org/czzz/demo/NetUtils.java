package org.czzz.demo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetUtils {

	/**
	 * 检查网络是否连接可用
	 * @return
	 */
	public static boolean isNetworkOk(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			Toast.makeText(context, "Network is disconnected", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
}
