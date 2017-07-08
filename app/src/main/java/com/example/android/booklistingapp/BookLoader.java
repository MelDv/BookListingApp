package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private static final String LOG_TAG = BookLoader.class.getName();
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground");
        if (mUrl == null) {
            return null;
        }
        List<Book> books = BookUtils.extractBooks(mUrl);
        return books;
    }
}
