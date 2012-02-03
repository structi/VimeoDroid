package com.makotosan.vimeodroid.vimeo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class CollectionItem<T extends VimeoObject> implements VimeoObject {
	// Fields
	private Class<T> classObj = null;
	private ArrayList<T> itemList;
	private int onThisPage;
	private int page;
	private int perPage;
	private String tag;
	private int total;

	// Constructor
	public CollectionItem(Class<T> c, String tagName) {
		this.tag = tagName;
		this.classObj = c;
	}

	// Getters/Setters
	public List<T> getItems(){
		return itemList;
	}

	public int getOnThisPage() {
		return onThisPage;
	}
	public int getPage() {
		return page;
	}

	public int getPerPage() {
		return perPage;
	}

	@Override
	public String getTag() {
		return this.tag;
	}

	public int getTotal() {
		return total;
	}

	// Public Methods
	@Override
	public void initialize(XmlPullParser xpp) throws XmlPullParserException, IOException {
		int eventType = xpp.getEventType();
		String tagName = xpp.getName();
		try {
			//if (eventType != XmlPullParser.START_TAG || !this.getTag().equals(this.tag)) {
				// Must be the starting tag <video>
			//	return;
			//}

			// Initialize other classes
			this.itemList = new ArrayList<T>();
			T item = classObj.newInstance();

			// Enumerate through the tags.
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					tagName = xpp.getName();
					if (this.getTag().equals(tagName)) {
						this.onThisPage = Integer.parseInt(xpp.getAttributeValue(XmlPullParser.NO_NAMESPACE, "on_this_page"));
						this.page = Integer.parseInt(xpp.getAttributeValue(XmlPullParser.NO_NAMESPACE, "page"));
						this.perPage = Integer.parseInt(xpp.getAttributeValue(XmlPullParser.NO_NAMESPACE, "perpage"));
						this.total = Integer.parseInt(xpp.getAttributeValue(XmlPullParser.NO_NAMESPACE, "total"));
						break;
					}
					if (item.getTag().equals(tagName)) {
						item = classObj.newInstance();
						item.initialize(xpp);
						this.itemList.add(item);
						break;
					}
					
					break;
				case XmlPullParser.END_TAG:
					tagName = "";
					if (xpp.getName().equals(this.getTag())) {
						// All done, exit
						return;
					}
					break;
				}
				eventType = xpp.next();
			}
		} catch (Exception ex) {
			// Log.e(Video.TAG, ex.getMessage(), ex);
		}
	}

}
