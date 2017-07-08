package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class BookUtils {
    public static final String LOG_TAG = BookUtils.class.getName();

    private BookUtils() {

    }

    public static List<Book> extractBooks(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
    }

    private static List<Book> extractFeatureFromJson(String bookJSON) {
        List<Book> books = new ArrayList<>();

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        try {
            JSONObject json = new JSONObject(bookJSON);
            JSONArray bookArray = json.getJSONArray("items");
            int totalItems = bookArray.getInt(1);
            StringBuilder authorString = new StringBuilder();

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                String selfLink = currentBook.getString("selfLink");
                String title = currentBook.getString("title");
                String subtitle = currentBook.getString("subtitle");
                JSONArray authors = currentBook.getJSONArray("authors");
                for (int j = 0; i < authors.length(); j++) {
                    JSONObject author = authors.getJSONObject(j);
                    authorString.append(author);
                    if (j < authors.length() - 1) {
                        authorString.append(" ,");
                    }
                }
                Book book = new Book(totalItems, title, subtitle, authorString.toString(), selfLink);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON result", e);
        }
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
