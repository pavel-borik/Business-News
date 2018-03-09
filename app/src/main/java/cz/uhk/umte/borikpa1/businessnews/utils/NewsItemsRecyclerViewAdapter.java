package cz.uhk.umte.borikpa1.businessnews.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.NewsItem;

public class NewsItemsRecyclerViewAdapter extends RecyclerView.Adapter<NewsItemsRecyclerViewAdapter.CustomViewHolder> {
    private List<NewsItem> newsItemList;

    public NewsItemsRecyclerViewAdapter(Context context, List<NewsItem> newsItemList) {
        this.newsItemList = newsItemList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        NewsItem newsItem= newsItemList.get(i);

        //Render image using Picasso library
        if (!TextUtils.isEmpty(newsItem.getThumbnailUrl())) {
            Picasso.get().load(newsItem.getThumbnailUrl())
                    //.error(R.drawable.placeholder)
                    //.placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);
        }

        //Setting text view title
        customViewHolder.title.setText(newsItem.getTitle());
        customViewHolder.description.setText(newsItem.getDescription());
        customViewHolder.date.setText(newsItem.getPubDate());

    }

    @Override
    public int getItemCount() {
        return (null != newsItemList ? newsItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected TextView description;
        protected TextView author;
        protected TextView date;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.newsItemThumbnail);
            this.title = (TextView) view.findViewById(R.id.newsItemTitle);
            this.description = (TextView) view.findViewById(R.id.newsItemDescription);
            this.date = (TextView) view.findViewById(R.id.newsItemDate);

        }
    }
}