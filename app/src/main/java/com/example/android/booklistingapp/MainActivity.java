package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private String url_books;
    private TextView emptyStateTextView;
    private BookAdapter adapter;
    private boolean isConnected = false;
    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyStateTextView = (TextView) findViewById(R.id.empty);
        final ListView list = (ListView) findViewById(R.id.list);
        list.setEmptyView(emptyStateTextView);

        adapter = new BookAdapter(this, new ArrayList<Book>());
        list.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.search_button);
        final EditText editText = (EditText) findViewById(R.id.search_field);

        loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            emptyStateTextView.setText(R.string.no_connection);
        } else {
            isConnected = true;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                adapter.notifyDataSetChanged();

                url_books = "https://www.googleapis.com/books/v1/volumes?q=" + editText.getText().toString() + "&maxResults=10";
                Log.i(LOG_TAG, url_books);

                if (isConnected) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    loadingIndicator.setVisibility(View.VISIBLE);
                }
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book book = adapter.getItem(position);
                String url = book.getReaderLink();

                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                urlIntent.setData(Uri.parse(url));
                startActivity(urlIntent);
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader");
        if (url_books != null) {
            return new BookLoader(this, url_books);
        }
        Log.i(LOG_TAG, "RETURNING NULL");
        return null;
    }

    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        Log.i(LOG_TAG, "onLoadFinished");
        adapter.clear();
        adapter.notifyDataSetChanged();

        url_books = "";

        ProgressBar pb = (ProgressBar) findViewById(R.id.loading_spinner);
        pb.setVisibility(View.GONE);
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        } else {
            emptyStateTextView.setText(R.string.no_books);
        }
    }

    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }
}