package org.czzz.demo;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DoubanUser {
	
	public String id, uid;
	public String name,avatar;
	public String created, loc_id, loc_name;
	public String alt, desc;
	public String relation;
	
	protected void init(JSONObject json) {
		try {
			id = json.getString("id");
			uid = json.getString("uid");
			name = json.getString("name");
			avatar = json.getString("avatar");
			created = json.getString("created");
			loc_id = json.getString("loc_id");
			loc_name = json.getString("loc_name");
			alt = json.getString("alt");
			desc = json.getString("desc");
			relation = json.getString("relation");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("EXCEPTION", "JSON FAIL --- " + e.getMessage());
		}
	}
	
	public void parse4User(String jsonStr){
		JSONObject json = null;
		try {
			json = new JSONObject(jsonStr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(json!=null) {
			init(json);
		}
	}
	
	/**
	 * 不存在accesstoken时，获取用户信息，不含有relation
	 * @param url
	 * @param listener
	 */
	public static void fetchUserInfo(String url, HttpListener listener){
		new HttpGetTask(listener).execute(url);
	}
	
	/**
	 * 存在accesstoken时，获取用户信息，含有relation
	 * @param url
	 * @param userInfoListener
	 * @param accessToken
	 */
	public static void fetchUserInfo(String url,
			HttpListener listener, String accessToken) {
		// TODO Auto-generated method stub
		new HttpGetTask(listener).execute(url,accessToken);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();

		sb.append("id=" + id + "\n")
			.append("uid=" + uid + "\n")
			.append("name=" + name + "\n")
			.append("avatar=" + avatar + "\n")
			.append("created=" + created + "\n")
			.append("loc_id=" + loc_id + "\n")
			.append("loc_name=" + loc_name + "\n")
			.append("alt=" + alt + "\n")
			.append("desc=" + desc + "\n")
			.append("relation=" + relation + "\n");
		
		return sb.toString();
	}

	
}
