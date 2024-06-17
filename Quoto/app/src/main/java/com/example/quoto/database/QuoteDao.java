package com.example.quoto.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuoteDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQOTD(QOTDModel qotdModel);


    @Query("SELECT id, quote, author FROM QOTDModel WHERE id = 1")
    QOTDModel getQuote();


    @Insert
    void insertLikedQuote(LikedQuotesModel likedQuotesModel);

    @Query("DELETE FROM LikedQuotesModel WHERE id = :id")
    void deleteLikedQuote(String id);

    @Query("SELECT * FROM LikedQuotesModel")
    List<LikedQuotesModel> getLikedQuotes();
}
