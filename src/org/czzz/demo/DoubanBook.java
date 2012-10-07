package org.czzz.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DoubanBook {

	public static final String ID = "id";
	public static final String ISBN10 = "isbn10";
	public static final String ISBN13 = "isbn13";
	public static final String TITLE = "title";
	public static final String ORIGIN_TITLE = "origin_title";
	public static final String ALT_TITLE = "alt_title";
	public static final String SUBTITLE = "subtitle";
	public static final String URL = "url";
	public static final String ALT = "alt";
	public static final String IMAGE = "image";
	public static final String AUTHOR = "author";
	public static final String TRANSLATOR = "translator";
	public static final String PUBLISHER = "publisher";
	public static final String PUBDATE = "pubdate";
	public static final String RATING = "rating";
	public static final String TAGS = "tags";
	public static final String BINDING = "binding";
	public static final String PRICE = "price";
	public static final String PAGES = "pages";
	public static final String AUTHOR_INTRO = "author_intro";
	public static final String SUMMARY = "summary";

	public String id, isbn10, isbn13, title, origin_title, alt_title, subtitle;
	public String url, alt, image, small_img, large_img, medium_img, price;
	public String author, translator, publisher, pubdate, author_intro,
			summary;
	public String rateMax, rateNum, rateAverage, rateMin, binding, pages;
	public ArrayList<HashMap<String, String>> tags = new ArrayList<HashMap<String, String>>();
	
	public List<BookCommentEntry> comments;		//单独获取

	protected void init(JSONObject json) {
		
		try {
			id = json.getString("id");
			isbn10 = json.getString("isbn10");
			isbn13 = json.getString("isbn13");
			title = json.getString("title");
			origin_title = json.getString("origin_title");
			alt_title = json.getString("alt_title");
			subtitle = json.getString("subtitle");
			url = json.getString("url");
			alt = json.getString("alt");
			image = json.getString("image");
			// get images ...
			JSONObject imagesJson = json.getJSONObject("images");
			small_img = imagesJson.getString("small");
			large_img = imagesJson.getString("large");
			medium_img = imagesJson.getString("medium");

			JSONArray authorArray = json.getJSONArray("author");
			for (int i = 0; i < authorArray.length(); i++) {
				if(i == 0) 
					author = "" + authorArray.get(i);
				else 
					author = author + " / " + authorArray.get(i);
			}

			JSONArray transArray = json.getJSONArray("translator");
			translator = "";
			for (int i = 0; i < transArray.length(); i++) {
				translator = translator + transArray.get(i) + " / ";
			}

			publisher = json.getString("publisher");
			pubdate = json.getString("pubdate");

			JSONObject rateJson = json.getJSONObject("rating");
			rateMax = rateJson.getString("max");
			rateMin = rateJson.getString("min");
			rateNum = rateJson.getString("numRaters");
			rateAverage = rateJson.getString("average");

			JSONArray tagArray = json.getJSONArray("tags");
			for (int i = 0; i < tagArray.length(); i++) {
				String name = tagArray.getJSONObject(i).getString("name");
				String count = tagArray.getJSONObject(i).getString("count");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("count", count);
				map.put("name", name);
				tags.add(map);
			}

			binding = json.getString("binding");
			price = json.getString("price");
			pages = json.getString("pages");

			author_intro = json.getString("author_intro");
			summary = json.getString("summary");

		} catch (JSONException jsone) {
			Log.e("EXCEPTION", "JSON FAIL --- " + jsone.getMessage());
		}

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		StringBuilder sb = new StringBuilder();

		sb.append("id=" + id + "\n")
				.append("isbn10=" + isbn10 + "\n")
				.append("isbn13=" + isbn13 + "\n")
				.append("title=" + title + "\n")
				.append("origin_title=" + origin_title + "\n")
				.append("alt_title=" + alt_title + "\n")
				.append("subtitle=" + subtitle + "\n")
				.append("url=" + url + "\n")
				.append("alt=" + alt + "\n")
				.append("image=" + image + "\n")
				.append("image=" + image + "\n")
				.append("small_img=" + small_img + "\n")
				.append("large_img=" + large_img + "\n")
				.append("medium_img=" + medium_img + "\n")
				.append("author=" + author + "\n")
				.append("translator=" + translator + "\n")
				.append("publisher=" + publisher + "\n")
				.append("pubdate=" + pubdate + "\n")
				.append("ratings=" + rateMax + "/" + rateNum + "/"
						+ rateAverage + "/" + rateMin + "\n")
				.append("tags=" + tags.toString() + "\n")
				.append("binding=" + binding + "\n")
				.append("price=" + price + "\n")
				.append("pages=" + pages + "\n")
				.append("author_intro=" + author_intro + "\n")
				.append("summary=" + summary);

		return sb.toString();
		
	}

}
