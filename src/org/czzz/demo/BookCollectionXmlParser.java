package org.czzz.demo;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BookCollectionXmlParser {

	private static final String ns = null;
	private BookCollectionEntry entry;
	// We don't use namespaces

	public List<BookCollectionEntry> parse(InputStream in) throws XmlPullParserException, IOException {
		
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
	
	private List<BookCollectionEntry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<BookCollectionEntry> entries = new ArrayList<BookCollectionEntry>();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

	// This class represents a single entry (post) in the XML feed.
	// It includes the data members "title," "link," and "summary."
	public static class Entry {
		public final String title;
		public final String updated;
		public final String link;

		private Entry(String title, String updated, String link) {
			this.title = title;
			this.updated = updated;
			this.link = link;
		}
	}
	
	// Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private BookCollectionEntry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        
    	entry = new BookCollectionEntry();
    	
    	parser.require(XmlPullParser.START_TAG, ns, "entry");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
            	entry.detail = readTag(parser, "title");
            } else if (name.equals("updated")) {
            	entry.updated = readTag(parser, "updated");
            } else if (name.equals("db:status")){
            	entry.status = readTag(parser, "db:status");
            } else if (name.equals("db:subject")){
            	Log.d("DEBUG", "XML: db:subject");
            	readSubTag(parser, "db:subject");
            } else {
                skip(parser);
            }
        }
        return entry;
    }

    // Processes title tags in the feed.
    private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return title;
    }
    
    private String readSubTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, tag);
    	while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getName();
            if(name.equals("title")){
            	entry.title = readTag(parser, "title");
            }else if(name.equals("link")){
            	readLink(parser);
            }else if(name.equals("db:attribute")){
            	readAttributeTag(parser, "db:attribute");
            }else{
            	skip(parser);
            }
    	}
		return null;
    }
    
    //Processes title tags in the feed.
    private void readAttributeTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
    	
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String mtag = parser.getName();
        String relType = parser.getAttributeValue(null, "name"); //属性
        if (mtag.equals(tag)) {
            if (relType.equals("isbn10")) {
                entry.isbn10 = readText(parser);
            } else if (relType.equals("isbn13")){
            	entry.isbn13 = readText(parser);
            } else if (relType.equals("author")){
            	if(entry.author == null){
        			entry.author = readText(parser);
        		}else{
        			entry.author += " / " + readText(parser);
        		}
            } else if (relType.equals("translator")){
            	if(entry.translator == null){
        			entry.translator = readText(parser);
        		}else{
        			entry.translator += " / " + readText(parser);
        		}
            } else if (relType.equals("publisher")){
            	entry.publisher = readText(parser);
            } else if(relType.equals("price")){
            	entry.price = readText(parser);
            } else if(relType.equals("pubdate")){
            	entry.pubdate = readText(parser);
            } else{
            	skip(parser);
            	return;
            }
            
        }
        
        parser.require(XmlPullParser.END_TAG, ns, tag);
    	
    } 
    
    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel"); //属性
        if (tag.equals("link")) {
            if (relType.equals("image")) {
                entry.image = parser.getAttributeValue(null, "href");
                parser.nextTag();
            } else if (relType.equals("alternate")){
            	entry.link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            } else if (relType.equals("mobile")){
            	entry.mobile_link = parser.getAttributeValue(null, "href");
                parser.nextTag();
            }else{
            	skip(parser);
            	return null;
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
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
