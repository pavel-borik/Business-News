package cz.uhk.umte.borikpa1.businessnews.adapters;

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

public class NewsItemsRecyclerViewAdapter extends RecyclerView.Adapter<NewsItemsRecyclerViewAdapter.NewsViewHolder> {
    private List<NewsItem> newsItemList;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
    private final OnItemClickListener listener;

    public NewsItemsRecyclerViewAdapter(List<NewsItem> newsItemList, OnItemClickListener listener) {
        this.newsItemList = newsItemList;
        this.listener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news_item, null);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder newsViewHolder, int pos) {
        NewsItem newsItem= newsItemList.get(pos);
            //Render image using Picasso library
            if (!TextUtils.isEmpty(newsItem.getThumbnailUrl())) {
                Picasso.get().load(newsItem.getThumbnailUrl())
                        //.error(R.drawable.placeholder)
                        //.placeholder(R.drawable.placeholder)
                        .into(newsViewHolder.imageView);
            } else {
                newsViewHolder.imageView.setVisibility(View.GONE);
            }

        newsViewHolder.title.setText(newsItem.getTitle());
        newsViewHolder.description.setText(newsItem.getDescription());
        newsViewHolder.date.setText(newsItem.getPubDate());
        newsViewHolder.bind(pos,listener);
    }

    @Override
    public int getItemCount() {
        return (null != newsItemList ? newsItemList.size() : 0);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected TextView description;
        protected TextView date;
        protected View itemView;

        public NewsViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.newsItemThumbnail);
            this.title = (TextView) view.findViewById(R.id.newsItemTitle);
            this.description = (TextView) view.findViewById(R.id.newsItemDescription);
            this.date = (TextView) view.findViewById(R.id.newsItemDate);
            this.itemView = view;
        }

        public void bind(final int pos, final NewsItemsRecyclerViewAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(pos));
        }
    }

    public void setNewsItemList(List<NewsItem> newsItemList) {
        this.newsItemList = newsItemList;
    }
}