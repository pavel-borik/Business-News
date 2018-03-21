package cz.uhk.umte.borikpa1.businessnews.activities;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.adapters.NewsViewPagerAdapter;
import cz.uhk.umte.borikpa1.businessnews.adapters.NewsItemsRecyclerViewAdapter;

public class NewsActivity extends BaseActivity {

    private NewsItemsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_news,null, false);
        mainContent.addView(contentView, 0);
        //setContentView(contentView);

        TabLayout tabLayout = (TabLayout) contentView.findViewById(R.id.newsCategoriesTabLayout);
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
