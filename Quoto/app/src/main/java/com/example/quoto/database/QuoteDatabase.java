package com.example.quoto.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {QOTDModel.class, LikedQuotesModel.class}, version = 2)
public abstract class QuoteDatabase extends RoomDatabase {

    public abstract QuoteDao quoteDao();
}
