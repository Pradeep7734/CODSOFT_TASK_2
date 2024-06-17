package com.example.quoto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quoto.database.LikedQuotesModel;

import java.util.List;

public class SavedQuotesRVAdapter extends RecyclerView.Adapter<SavedQuotesRVAdapter.SavedQuotesViewHolder> {

    Context context;
    List<LikedQuotesModel> quotes;

    public SavedQuotesRVAdapter(Context context, List<LikedQuotesModel> quotes) {
        this.context = context;
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public SavedQuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.saved_quote_view, parent, false);
        return new SavedQuotesRVAdapter.SavedQuotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedQuotesViewHolder holder, int position) {
        holder.quote.setText(quotes.get(position).getQuote());
        holder.author.setText(quotes.get(position).getAuthor());
        holder.share_quoto_share_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sharing_quote = holder.quote.getText().toString()+"\n\n"+holder.author.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT, sharing_quote);
                Intent.createChooser(intent, "Quote");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateQuotes(List<LikedQuotesModel> newQuotes) {
        quotes.clear();
        quotes.addAll(newQuotes);
        notifyDataSetChanged();
    }

    public static class SavedQuotesViewHolder extends RecyclerView.ViewHolder{

        TextView quote, author;
        ImageView share_quoto_share_view;
        public SavedQuotesViewHolder(@NonNull View itemView) {
            super(itemView);
            quote = itemView.findViewById(R.id.save_quote_quote_tv);
            author = itemView.findViewById(R.id.save_quote_author_tv);
            share_quoto_share_view = itemView.findViewById(R.id.share_quoto_share_view);
        }
    }
}
