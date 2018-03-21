package cz.uhk.umte.borikpa1.businessnews.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cz.uhk.umte.borikpa1.businessnews.R;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home,null, false);
        mainContent.addView(contentView, 0);
    }
}
