package com.example.quoto.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LikedQuotesModel {

    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "quote")
    public String quote;

    @ColumnInfo(name = "author")
    public String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LikedQuotesModel(String id, String quote, String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }
}
