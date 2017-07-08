package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    private static final String LOG_TAG = BookAdapter.class.getName();

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView authors = convertView.findViewById(R.id.list_authors);
        String authorString = currentBook.getAuthors();
        authors.setText(authorString);

        TextView title = convertView.findViewById(R.id.list_title);
        String titleString = currentBook.getTitle();
        title.setText(titleString);

        return convertView;
    }
}
