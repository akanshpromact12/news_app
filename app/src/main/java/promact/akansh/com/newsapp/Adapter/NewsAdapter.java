package promact.akansh.com.newsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import promact.akansh.com.newsapp.Model.ArticlesParams;
import promact.akansh.com.newsapp.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private Context context;
    private List<ArticlesParams> articles;

    public NewsAdapter(Context context, List<ArticlesParams> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_adapter, parent, false);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
        final ArticlesParams articlesParams = articles.get(position);

        Glide.with(context)
                .load(articlesParams.getUrlToImage())
                .into(holder.newsImage);
        holder.headline.setText(articlesParams.getTitle());
    }

    @Override
    public int getItemCount() {
        return articles.size();

    }

    static class NewsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView headline;
        ImageView newsImage;


        NewsAdapterViewHolder(View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.newsHeadline);
            newsImage = itemView.findViewById(R.id.newsImage);
        }
    }
}
