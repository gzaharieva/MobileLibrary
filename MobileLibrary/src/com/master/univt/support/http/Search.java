package com.master.univt.support.http;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volumes;
import com.master.univt.support.util.LogUtil;
import com.master.univt.support.util.SearchSetting;

import java.io.IOException;

public class Search {

    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    public static String apiKey = "AIzaSyAjVPOlinPjKr4v4V3Iu1CEQlp61OGETh8";

    private static Books books;

    static {
        books = new Books.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("YOURAPPLICATIONNAME").build();
    }

    public static Volumes searchVolumes(String queryTerm) {
        try {
            SearchSetting searchSetting = SearchSetting.getInstance();
            Books.Volumes.List search = books.volumes().list(searchSetting.getKeywordsType() + ":" + queryTerm)
                    //Books.Volumes.List search = books.volumes().list(queryTerm)
                    .setFields("items(volumeInfo,userInfo)")
                    .setKey(apiKey)
                    .setDownload(searchSetting.getDownload())
                    .setFilter(searchSetting.getFilter())
                    .setMaxResults(Long.valueOf(searchSetting.getMaxResults()))
                    .setPrintType(searchSetting.getPrintType())
                    .setProjection(searchSetting.getProjection())
                    .setOrderBy(searchSetting.getOrderBy());
            return search.execute();
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
