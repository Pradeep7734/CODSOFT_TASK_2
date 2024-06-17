package com.example.quoto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.quoto.database.LikedQuotesModel;
import com.example.quoto.database.QuoteDao;
import com.example.quoto.database.QuoteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SavedQuotoScreen extends Fragment {

    private RecyclerView saved_quotes_recycler_view;
    private SavedQuotesRVAdapter adapter;
    List<LikedQuotesModel> quotes = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_quoto_screen, container, false);

        initializeViews(view);
        setupRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetSavedQuotesThread().start();
    }

    private void initializeViews(View view){
        saved_quotes_recycler_view = view.findViewById(R.id.saved_quotes_recycler_view);
    }

    private void setupRecyclerView() {
        adapter = new SavedQuotesRVAdapter(getContext(), quotes);
        saved_quotes_recycler_view.setAdapter(adapter);
    }

    class GetSavedQuotesThread extends Thread{
        public void run(){
            super.run();

            QuoteDatabase db = Room.databaseBuilder(
                            requireContext(),
                            QuoteDatabase.class,
                            "QuoteDB"
                    )
                    .fallbackToDestructiveMigration()
                    .build();

            QuoteDao quoteDao = db.quoteDao();
            quotes = quoteDao.getLikedQuotes();

            requireActivity().runOnUiThread(() -> {
                if (quotes != null && !quotes.isEmpty()) {
                    Collections.reverse(quotes);
                    adapter.updateQuotes(quotes);
                } else {
                    Toast.makeText(getContext(), "No saved quotes", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}