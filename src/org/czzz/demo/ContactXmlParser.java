package org.czzz.demo;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactXmlParser {

	private static final String ns = null;
	private DoubanUser entry;
	// We don't use namespaces

	public List<DoubanUser> parse(InputStream in) throws XmlPullParserException, IOException {
		
		Log.d("DEBUG", "start to parse");
		
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}
	
	private List<DoubanUser> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<DoubanUser> entries = new ArrayList<DoubanUser>();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
            	Log.d("DEBUG", "start to parse entry");
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private DoubanUser readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        
    	entry = new DoubanUser();
    	
    	parser.require(XmlPullParser.START_TAG, ns, "entry");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
            	String id_link = readTag(parser, "id");
            	entry.id = id_link.substring(id_link.lastIndexOf("/") + 1);
            } else if (name.equals("title")) {
            	entry.name = readTag(parser, "title");
            } else if (name.equals("db:uid")) {
            	entry.uid = readTag(parser, "db:uid");
            } else if (name.equals("db:signature")){
            	entry.signature = readTag(parser, "db:signature");
            } else if (name.equals("content")){
            	entry.desc = readTag(parser, "content");
            } else if (name.equals("link")){
            	readLink(parser);
            } else {
                skip(parser);
            }
        }
        return entry;
    }

    /**
     * Read the text in a tag 
     * @param parser
     * @param tag
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return title;
    }
    
    /**
     * Read the link with different attribute value
     * @param parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    private void readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel"); //属性
        if (tag.equals("link")) {
            if (relType.equals("icon")) {
                entry.avatar = parser.getAttributeValue(null, "href");
                parser.nextTag();
            } else{
            	skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
