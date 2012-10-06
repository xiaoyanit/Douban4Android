package org.czzz.demo;

public class BookCollectionEntry {

	public String detail;
	public String updated;
	public String status;
	
	public String title;
	public String image;
	public String mobile_link;
	public String link;
	
	public String isbn10;
	public String isbn13;
	public String author;
	public String translator;
	public String price;
	public String publisher;
	public String pubdate;
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(detail == null){
			return "BookCollection@null";
		}
		
		return "{detail=" + detail + "\n"
				+ "updated=" + updated + "\n"
				+ "status=" + status + "\n"
				+ "title=" + title + "\n"
				+ "isbn10=" + isbn10 + "\n"
				+ "isbn13=" + isbn13 + "\n"
				+ "author=" + author + "\n"
				+ "translator=" + translator + "\n"
				+ "publisher=" + publisher + "\n"
				+ "pubdate=" + pubdate + "\n"
				+ "price=" + price + "\n"
				+ "link=" + link + "\n"
				+ "moile_link=" + mobile_link + "\n"
				+ "image=" + image + "\n}";
	}
	
	
}
