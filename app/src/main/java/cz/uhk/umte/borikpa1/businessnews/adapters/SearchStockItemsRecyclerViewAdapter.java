package cz.uhk.umte.borikpa1.businessnews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cz.uhk.umte.borikpa1.businessnews.R;
import cz.uhk.umte.borikpa1.businessnews.model.StockSymbol;
import de.hdodenhof.circleimageview.CircleImageView;

public class SearchStockItemsRecyclerViewAdapter extends RecyclerView.Adapter<SearchStockItemsRecyclerViewAdapter.SearchStocksViewHolder> {
    private List<StockSymbol> stockSymbolList;
    private final Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    private final OnItemClickListener listener;

    public SearchStockItemsRecyclerViewAdapter(Context context, List<StockSymbol> stockSymbolList, OnItemClickListener listener) {
        this.mContext = context;
        this.stockSymbolList = stockSymbolList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchStocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_found_item, null);
        SearchStocksViewHolder viewHolder = new SearchStocksViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchStocksViewHolder holder, int position) {
        StockSymbol stockSymbol= stockSymbolList.get(position);
        holder.stockSymbol.setText(stockSymbol.getSymbol());
        holder.stockCompany.setText(stockSymbol.getName());
        if (!TextUtils.isEmpty(stockSymbol.getLogoUrl())) {
            Picasso.get().load(stockSymbol.getLogoUrl())
                    .into(holder.stockLogo);
        } else {
            holder.stockLogo.setVisibility(View.GONE);
        }
        holder.bind(position,listener);
    }

    @Override
    public int getItemCount() {
        return (null != stockSymbolList ? stockSymbolList.size() : 0);
    }

    public class SearchStocksViewHolder extends RecyclerView.ViewHolder {
        protected TextView stockSymbol;
        protected TextView stockCompany;
        protected CircleImageView stockLogo;
        protected View itemView;

        public SearchStocksViewHolder(View itemView) {
            super(itemView);
            this.stockSymbol = itemView.findViewById(R.id.tvSymbolFound);
            this.stockCompany = itemView.findViewById(R.id.tvCompanyFound);
            this.stockLogo = itemView.findViewById(R.id.imageViewLogoFound);
            this.itemView = itemView;
        }
        public void bind(final int pos, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(pos));
        }
    }

    public void setStockSymbolList(List<StockSymbol> stockSymbolList) {
        this.stockSymbolList = stockSymbolList;
    }
}
