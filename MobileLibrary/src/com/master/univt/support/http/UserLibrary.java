package com.master.univt.support.http;

import android.content.SharedPreferences;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.Books;
import com.master.univt.support.util.LogUtil;
import com.master.univt.support.util.SearchSetting;

import java.io.IOException;

public class UserLibrary {

    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    public static String apiKey = "AIzaSyAjVPOlinPjKr4v4V3Iu1CEQlp61OGETh8";

    private static Books books;

    static {
        books = new Books.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("APP").build();
    }

    public static com.google.api.services.books.model.Bookshelves searchVolumes(String queryTerm) {
        try {
            SearchSetting searchSetting = SearchSetting.getInstance();
            Books.Mylibrary search;
            search = books.mylibrary();
            Books.Mylibrary.Bookshelves.List shelfs = search.bookshelves().list().setOauthToken("117463411595501910870");
            return shelfs.execute();
        } catch (GoogleJsonResponseException e) {
            LogUtil.d("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (IOException e) {
            LogUtil.d("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            LogUtil.d(t);
        }

        return null;
    }

}
