package cz.uhk.umte.borikpa1.businessnews.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.model.NewsItem;

public class NewsItemsXmlParser {
    private static final String ns = null;
    private DateFormat targetDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
    private List<String> dateFormatStrings = Arrays.asList("EEE, dd MMM yyyy HH:mm zzz", "EEE, dd MMM yyyy HH:mm:ss zzz");

    public List<NewsItem> parse(InputStream in) throws XmlPullParserException, IOException {
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

    private List<NewsItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<NewsItem> newsItems = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            parser.require(XmlPullParser.START_TAG, ns, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();

                if (name.equals("item")) {
                    newsItems.add(readItem(parser));
                } else {
                    skip(parser);
                }
            }
        }
        Collections.sort(newsItems);
        return newsItems;
    }

    private NewsItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String link = null;
        String pubDate = null;
        String thumbnailUrl = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    title = readTitle(parser);
                    break;
                case "description":
                    description = readDescription(parser);
                    break;
                case "link":
                    link = readLink(parser);
                    break;
                case "pubDate":
                    pubDate = readPubDate(parser);
                    break;
                case "media:thumbnail":
                    thumbnailUrl = readThumbnailUrl(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new NewsItem(title, description, link, pubDate, thumbnailUrl);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes summary tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        description = org.apache.commons.lang3.StringEscapeUtils.unescapeXml(description);
        return description;
    }

    private String  readPubDate (XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);

        for (String formatString : dateFormatStrings)
        {
            try
            {
                Date d = new SimpleDateFormat(formatString).parse(pubDate);
                pubDate = targetDateFormat.format(d);
            }
            catch (ParseException e) {}
        }

        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }

    private String readThumbnailUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        String thumbnailUrl = "";
        parser.require(XmlPullParser.START_TAG, ns, "media:thumbnail");
        String tag = parser.getName();
        if (tag.equals("media:thumbnail")) {
                thumbnailUrl = parser.getAttributeValue(ns, "url");
                parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG,ns, "media:thumbnail");
        return thumbnailUrl;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

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
