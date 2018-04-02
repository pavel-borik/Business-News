package cz.uhk.umte.borikpa1.businessnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.fragments.HomeFragment;
import cz.uhk.umte.borikpa1.businessnews.fragments.StockDataFragment;
import cz.uhk.umte.borikpa1.businessnews.fragments.ParentNewsFragment;


/**
 * Created by pavelb on 21. 3. 2018.
 */

public class BaseActivity extends AppCompatActivity{

    protected DrawerLayout mDrawerLayout;
    protected FrameLayout mainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mainContent = findViewById(R.id.main_content);
        NavigationView navigationView = findViewById(R.id.nav_view);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, new HomeFragment())
                .commit();

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    Intent intent = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
//                            intent = new Intent(BaseActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(0, 0);
                            HomeFragment homeFragment = new HomeFragment();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, homeFragment)
                                    .commit();
                            break;
                        case R.id.nav_news:
//                            intent = new Intent(BaseActivity.this, NewsActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(0, 0);
                            ParentNewsFragment parentNewsFragment = new ParentNewsFragment();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, parentNewsFragment)
                                    .commit();
                            break;

                        case R.id.nav_stocks:
//                            intent = new Intent(BaseActivity.this, NewsActivity.class);
//                            startActivity(intent);
//                            overridePendingTransition(0, 0);
                            StockDataFragment stockDataFragment = new StockDataFragment();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, stockDataFragment)
                                    .commit();
                            break;
                    }
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
