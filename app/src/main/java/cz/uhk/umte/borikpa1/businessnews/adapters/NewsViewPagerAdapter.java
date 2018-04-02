package cz.uhk.umte.borikpa1.businessnews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cz.uhk.umte.borikpa1.businessnews.fragments.NewsFragment;

public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {

    NewsFragment newsFragment;
    int numOfTabs;
    public NewsViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                newsFragment = new NewsFragment().newInstance("https://www.cnbc.com/id/10001147/device/rss/rss.html");
                return newsFragment;
            case 1:
                newsFragment = new NewsFragment().newInstance("https://www.cnbc.com/id/20910258/device/rss/rss.html");
                return newsFragment;
            case 2:
                newsFragment = new NewsFragment().newInstance("https://www.cnbc.com/id/10000664/device/rss/rss.html");
                return newsFragment;
            case 3:
                newsFragment= new NewsFragment().newInstance("http://feeds.bbci.co.uk/news/world/rss.xml");
                return newsFragment;
            case 4:
                newsFragment = new NewsFragment().newInstance("http://feeds.bbci.co.uk/news/technology/rss.xml");
                return newsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
