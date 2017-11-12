package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Johannes on 06.11.2017.
 *
 * This class loads a List of Books in a Background Thread (loadInBackground)
 *
 */

public class BookLoader extends AsyncTaskLoader<List<Book>>{
    private String Url;

    public BookLoader (Context context, String url){
        super(context);
        this.Url = url;
    }

    @Override
    protected void onStartLoading(){forceLoad();}


    @Override
    public List<Book> loadInBackground() {
        if (Url == null){
            return null;
        }else{
            List<Book> books = QueryUtils.fetchBookData(Url);
            return books;
        }
    }
}
