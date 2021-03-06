package cz.uhk.umte.borikpa1.businessnews.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.StockItem;

public class StockItemsRecyclerViewAdapter extends RecyclerView.Adapter<StockItemsRecyclerViewAdapter.StocksViewHolder>{
    private List<StockItem> stockItemList;
    private final Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private final OnItemClickListener listener;

    public StockItemsRecyclerViewAdapter(Context context, List<StockItem> stockItemList, OnItemClickListener listener) {
        this.mContext = context;
        this.stockItemList = stockItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stock_item, null);
        StocksViewHolder viewHolder = new StocksViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder holder, int position) {
       StockItem stockItem= stockItemList.get(position);
       holder.stockSymbol.setText(stockItem.getSymbol());
       holder.stockCompany.setText(stockItem.getCompanyName());
       holder.stockDate.setText(stockItem.getLatestTime());
       try {
           holder.stockPrice.setText(String.valueOf((double) Math.round(stockItem.getLatestPrice() * 100) / 100));
           if(stockItem.getChange() < 0) {
               holder.stockChange.setTextColor(ContextCompat.getColor(mContext, R.color.colorTrendingDown));
               holder.stockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_down_black_24dp,0,0,0);

           } else {
               holder.stockChange.setTextColor(ContextCompat.getColor(mContext, R.color.colorTrendingUp));
               holder.stockChange.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_up_black_24dp,0,0,0);

           }
           holder.stockChange.setText(String.valueOf((double) Math.round(stockItem.getChange() * 100) / 100));
       }
       catch (NullPointerException e) {
           e.printStackTrace();
       }
       holder.bind(position,listener);
    }

    @Override
    public int getItemCount() {
        return (null != stockItemList ? stockItemList.size() : 0);
    }

    public class StocksViewHolder extends RecyclerView.ViewHolder {
        protected TextView stockSymbol;
        protected TextView stockCompany;
        protected TextView stockPrice;
        protected TextView stockDate;
        protected TextView stockChange;
        protected View itemView;
        public RelativeLayout viewForeground;

        public StocksViewHolder(View itemView) {
            super(itemView);
            this.stockSymbol = itemView.findViewById(R.id.tvStockSymbol);
            this.stockCompany = itemView.findViewById(R.id.tvStockCompanyName);
            this.stockPrice = itemView.findViewById(R.id.tvStockPrice);
            this.stockDate = itemView.findViewById(R.id.tvStockDate);
            this.stockChange = itemView.findViewById(R.id.tvStockChange);
            this.itemView = itemView;
            this.viewForeground = itemView.findViewById(R.id.stockitem_row_view_foreground);
        }
        public void bind(final int pos, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(pos));
        }
    }

    public void setStockItemList(List<StockItem> stockItemList) {
        this.stockItemList = stockItemList;
    }

    public void removeItem(int pos) {
        stockItemList.remove(pos);
        notifyItemRemoved(pos);
    }

}
