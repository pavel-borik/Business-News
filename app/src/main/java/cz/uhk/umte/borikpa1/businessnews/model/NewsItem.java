package cz.uhk.umte.borikpa1.businessnews.model;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsItem implements Comparable<NewsItem> {

    public String title;
    public String description;
    public String link;
    public String pubDate;
    public String thumbnailUrl;

    public NewsItem() {
    }

    public NewsItem(String title, String description, String link, String pubDate, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int compareTo(@NonNull NewsItem o) {
        DateFormat targetDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
        try {
            Date dThis = targetDateFormat.parse(getPubDate());
            Date dObj = targetDateFormat.parse(o.getPubDate());
            return dObj.compareTo(dThis);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return 0;
    }
}
