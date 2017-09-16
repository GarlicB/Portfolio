package com.main;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class DefaultSetting {
	
private final String SERVER_ADDRESS = "http://cvsale.dothome.co.kr:80/Books";
	
	public final ArrayList<String> getXmlDataList(String filename, String str) {
		String rss = SERVER_ADDRESS + "/";
		ArrayList<String> ret = new ArrayList<String>();

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			URL server = new URL(rss + filename);
			InputStream is = server.openStream();
			xpp.setInput(is, "UTF-8");

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(str)) {
						ret.add(xpp.nextText());
					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}

		return ret;
	}
	
	

}
