package promact.akansh.com.newsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import promact.akansh.com.newsapp.Model.ArticlesParams;
import promact.akansh.com.newsapp.R;
import promact.akansh.com.newsapp.WebView.WebActivity;

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
    public void onBindViewHolder(@NonNull final NewsAdapterViewHolder holder, int position) {
        final ArticlesParams articlesParams = articles.get(position);

        Glide.with(context)
                .load(articlesParams.getUrlToImage())
                .into(holder.newsImage);
        holder.relLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(context.getString(R.string.url), articlesParams.getUrl());
                /*String url = articlesParams.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));*/
                context.startActivity(intent);
            }
        });
        holder.headline.setText(articlesParams.getTitle());
        holder.newsDesc.setVisibility(View.GONE);
        holder.newsDesc.setText(articlesParams.getDescription());
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri shareUri = getLocalBitmapUri(holder.newsImage);
                if (shareUri != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareUri);
                    shareIntent.setType("image/*");
                    context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
                }
            }
        });
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.viewMore.getText().toString().equals(context.getString(R.string.read_story))) {
                    holder.viewMore.setText(context.getString(R.string.show_less));
                    holder.newsDesc.setVisibility(View.VISIBLE);
                } else {
                    holder.viewMore.setText(context.getString(R.string.read_story));
                    holder.newsDesc.setVisibility(View.GONE);
                }
            }
        });
        String dateString = articlesParams.getPublishedAt();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
        Date date;
        String convertedDate = "";
        String currentDate = "";
        String currentTime = "";
        String convertedTime = "";
        try {
            date = dateFormat.parse(dateString);

            convertedDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(date);
            currentDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());
            convertedTime = new SimpleDateFormat("MMMM dd, yyyy - HH:mm", Locale.getDefault()).format(date);
            currentTime = new SimpleDateFormat("MMMM dd, yyyy - HH:mm", Locale.getDefault()).format(new Date());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String author = "";
        String dateStr;
        if (articlesParams.getAuthor() != null)
            author = "Author: " + articlesParams.getAuthor();
        else
            holder.author.setVisibility(View.GONE);

        if (convertedDate.equals(currentDate)) {
            int current = Integer.parseInt(currentTime.split(" - ")[1].split(":")[0]);
            int specific = Integer.parseInt(convertedTime.split(" - ")[1].split(":")[0]);
            if (current < specific)
                dateStr = (specific - current) + " hours ago";
            else if (current > specific)
                dateStr = (current - specific) + " hours ago";
            else
                dateStr = "Just now";
        } else {
            dateStr = convertedDate;
        }
        holder.date.setText(dateStr);
        holder.author.setText(author);
    }

    Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class NewsAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView headline, newsDesc, viewMore, date, author;
        ImageView newsImage, share;
        RelativeLayout relLayout;

        NewsAdapterViewHolder(View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.newsHeadline);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsDesc = itemView.findViewById(R.id.newsDesc);
            date = itemView.findViewById(R.id.date);
            author = itemView.findViewById(R.id.author);
            viewMore = itemView.findViewById(R.id.readStory);
            relLayout = itemView.findViewById(R.id.mainLayout);
            share = itemView.findViewById(R.id.shareIcon);
        }
    }
}
