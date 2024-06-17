package com.example.quoto;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.quoto.database.LikedQuotesModel;
import com.example.quoto.database.QOTDModel;
import com.example.quoto.database.QuoteDao;
import com.example.quoto.database.QuoteDatabase;

import java.util.UUID;


public class QuotoMainScreen extends Fragment {
    private TextView quote_tv, author_tv;
    private ImageView like_quoto, share_quoto;
    private String quote, author;

    private boolean isQuoteLiked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quoto_main_screen, container, false);

        initializeViews(view);
        updateQuote();
        like_quoto.setOnClickListener(v -> like_quote());
        share_quoto.setOnClickListener(v -> share_quote());

        return view;
    }

    private void initializeViews(View view) {
        quote_tv = view.findViewById(R.id.quote_tv);
        author_tv = view.findViewById(R.id.author_tv);
        like_quoto = view.findViewById(R.id.like_quoto);
        share_quoto = view.findViewById(R.id.share_quoto);
    }

    public void updateQuote() {
        new DBThread().start();
    }

    private void like_quote(){
        String liked_quote = quote_tv.getText().toString();
        String liked_author = author_tv.getText().toString();
        String id = generateUUID(liked_quote, liked_author);
        if (!isQuoteLiked) {
            like_quoto.setImageResource(R.drawable.liked);
            isQuoteLiked = true;
            new AddLikedQuoteThread(id, liked_quote, liked_author).start();
        } else {
            like_quoto.setImageResource(R.drawable.like);
            isQuoteLiked = false;
            new RemoveLikedQuoteThread(id).start();
        }
    }
    private void share_quote(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String sharing_quote = quote_tv.getText().toString()+"\n\n"+author_tv.getText().toString();
        intent.putExtra(Intent.EXTRA_TEXT, sharing_quote);
        Intent.createChooser(intent, "Quote");
        startActivity(intent);
    }

    class DBThread extends Thread {
        public void run() {
            super.run();

            QuoteDatabase db = Room.databaseBuilder(
                            requireContext(),
                            QuoteDatabase.class,
                            "QuoteDB"
                    )
                    .fallbackToDestructiveMigration()
                    .build();

            QuoteDao quoteDao = db.quoteDao();
            QOTDModel qotdModel = quoteDao.getQuote();
            if (qotdModel != null) {
                quote = qotdModel.getQuote();
                author = qotdModel.getAuthor();
                requireActivity().runOnUiThread(() -> {
                    quote_tv.setText(quote);
                    author_tv.setText(author);
                });
            }
        }
    }


    class AddLikedQuoteThread extends Thread {

        String id, quote, author;

        AddLikedQuoteThread(String id, String quote, String author) {
            this.id = id;
            this.quote = quote;
            this.author = author;
        }

        public void run() {
            super.run();

            QuoteDatabase db = Room.databaseBuilder(
                            requireContext(),
                            QuoteDatabase.class,
                            "QuoteDB"
                    )
                    .fallbackToDestructiveMigration()
                    .build();

            QuoteDao quoteDao = db.quoteDao();
            LikedQuotesModel likedQuotesModel = new LikedQuotesModel(this.id, this.quote, this.author);
            quoteDao.insertLikedQuote(likedQuotesModel);
        }
    }

    class RemoveLikedQuoteThread extends Thread {

        String id;

        RemoveLikedQuoteThread(String id) {
            this.id = id;
        }

        public void run() {
            super.run();

            QuoteDatabase db = Room.databaseBuilder(
                            requireContext(),
                            QuoteDatabase.class,
                            "QuoteDB"
                    )
                    .fallbackToDestructiveMigration()
                    .build();

            QuoteDao quoteDao = db.quoteDao();
            quoteDao.deleteLikedQuote(id);
        }
    }

    private String generateUUID(String... variables) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String variable : variables) {
            stringBuilder.append(variable);
        }
        String combinedString = stringBuilder.toString();
        UUID uuid = UUID.nameUUIDFromBytes(combinedString.getBytes());
        return uuid.toString();
    }
}