package org.czzz.demo;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DoubanUser {
	
	public String id, uid;
	public String name,avatar;
	public String created, loc_id, loc_name;
	public String alt, desc;
	public String relation;
	public String signature;
	
	public List<BookCollectionEntry> collections;	// 藏书
	
	protected void init(JSONObject json) {
		try {
			id = json.getString("id");
			uid = json.getString("uid");
			name = json.getString("name");
			avatar = json.getString("avatar");
			created = json.getString("created");
			if(json.has("loc_id"))
				loc_id = json.getString("loc_id");
			if(json.has("loc_name"))
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
	 * @param uid
	 * @param listener
	 */
	public static void fetchUserInfo(String uid, HttpListener listener){
		String url = "https://api.douban.com/v2/user/" + uid;
		new HttpGetTask(listener).execute(url);
	}
	
	/**
	 * 存在accesstoken时，获取用户信息，含有relation
	 * @param uid
	 * @param userInfoListener
	 * @param accessToken
	 */
	public static void fetchUserInfo(String uid,
			HttpListener listener, String accessToken) {
		// TODO Auto-generated method stub
		String url = "https://api.douban.com/v2/user/" + uid;
		new HttpGetTask(listener).execute(url,accessToken);
	}
	
	public static void fetchUserContacts(String uid, HttpListener listener, int type){
		String url = "http://api.douban.com/people/" + uid + "/contacts";
		new XmlDownloadTask(listener, type).execute(url);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();

		sb.append("{id=" + id + "\n")
			.append("uid=" + uid + "\n")
			.append("name=" + name + "\n")
			.append("avatar=" + avatar + "\n")
			.append("created=" + created + "\n")
			.append("loc_id=" + loc_id + "\n")
			.append("loc_name=" + loc_name + "\n")
			.append("alt=" + alt + "\n")
			.append("signature=" + signature + "\n") 
			.append("desc=" + desc + "\n")
			.append("relation=" + relation + "}\n");
		
		return sb.toString();
	}

	public List<BookCollectionEntry> getCollections() {
		return collections;
	}

	public void setCollections(List<BookCollectionEntry> collections) {
		this.collections = collections;
	}

	
	
	
}
