package cz.uhk.umte.borikpa1.businessnews.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.adapters.NewsViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentNewsFragment extends Fragment {


    public ParentNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_parent_news, container, false);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.newsCategoriesTabLayout2);
        tabLayout.addTab(tabLayout.newTab().setText("Business"));
        tabLayout.addTab(tabLayout.newTab().setText("Economy"));
        tabLayout.addTab(tabLayout.newTab().setText("Finance"));
        tabLayout.addTab(tabLayout.newTab().setText("World"));
        tabLayout.addTab(tabLayout.newTab().setText("Technology"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.newsCategoriesViewPager2);
        final NewsViewPagerAdapter newsViewPagerAdapter = new NewsViewPagerAdapter(getFragmentManager(), tabLayout.getTabCount());

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
        return view;
    }

}
