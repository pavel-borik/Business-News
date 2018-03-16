package cz.uhk.umte.borikpa1.businessnews;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import cz.uhk.umte.borikpa1.businessnews.adapters.NewsViewPagerAdapter;
import cz.uhk.umte.borikpa1.businessnews.adapters.NewsItemsRecyclerViewAdapter;

public class NewsActivity extends FragmentActivity {

    private NewsItemsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.newsCategoriesTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        tabLayout.addTab(tabLayout.newTab().setText("Economy"));
        tabLayout.addTab(tabLayout.newTab().setText("Finance"));
        tabLayout.addTab(tabLayout.newTab().setText("World"));
        tabLayout.addTab(tabLayout.newTab().setText("Technology"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.newsCategoriesViewPager);
        final NewsViewPagerAdapter newsViewPagerAdapter = new NewsViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(newsViewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}
