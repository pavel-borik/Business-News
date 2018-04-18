package cz.uhk.umte.borikpa1.businessnews.utils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.model.NewsItem;

public class XmlDownloadHelper {

    public static List<NewsItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        NewsItemsXmlParser newsItemsXmlParser = new NewsItemsXmlParser();
        List<NewsItem> newsItems;

        try {
            stream = downloadUrl(urlString);
            newsItems = newsItemsXmlParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return newsItems;
    }

    private static InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        int statusCode = conn.getResponseCode();
        if (statusCode == 200);
        return conn.getInputStream();
    }
}
