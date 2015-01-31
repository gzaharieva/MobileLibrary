package com.master.univt.support.http;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Bookshelves;
import com.google.api.services.books.model.Volumes;
import com.master.univt.Constants;
import com.master.univt.configs.BuildConfig;
import com.master.univt.model.DBHelper;
import com.master.univt.model.User;
import com.master.univt.model.UserModel;
import com.master.univt.services.SharedPreferencedSingleton;
import com.master.univt.support.GlobalApplication;
import com.master.univt.support.util.LogUtil;

import java.io.IOException;
import java.util.List;

public class UserLibrary {

    private static final String LOG_TAG = UserLibrary.class.getSimpleName();
    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    public static String apiKey = "AIzaSyAjVPOlinPjKr4v4V3Iu1CEQlp61OGETh8";

    private static Books books;
    private String token;

    static {
//        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        books = new Books.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
              //  request.getHeaders().setAuthorization(GoogleHeaders.getGoogleLoginValue(authToken));
            }
        }).setApplicationName("APP").build();
        
    }

    public static Bookshelves getBookshelves() {
        Bookshelves result = null;
        try {
            SharedPreferencedSingleton s = SharedPreferencedSingleton.getInstance();
            String token = s.getString(Constants.PREFS_OAUTH_TOKEN, "");
            String params = String.format("refresh_token=%s&client_id=%s&grant_type=refresh_token&redirect_uri=urn:ietf:wg:oauth:2.0:oob", token, BuildConfig.CLIENT_ID);

            String refreshToken = RefreshTokenService.getRefreshToken(params);
            Log.d("LOG", "refreshToken:" + refreshToken);
            Books.Mylibrary.Bookshelves.List shelves =  books.mylibrary().bookshelves().list()//.setOauthToken("117463411595501910870")
                    .setKey(apiKey).setOauthToken(refreshToken);
            result =  shelves.execute();
            
            insertOrReplace(result);
        } catch (GoogleJsonResponseException e) {
            LogUtil.d("There was a service error: " + e.getDetails().getCode() + " : " + e.getMessage());
        } catch (IOException e) {
            LogUtil.d("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            LogUtil.d(t);
        }

        return result;
    }

    private static void insertOrReplace(Bookshelves result) {
        try {
            String bookshelvesString = Search.JSON_FACTORY.toString(result);
            User loggedInUser = GlobalApplication.getInstance().getLoggedInUser();
            loggedInUser.setBookshelvesString(bookshelvesString);
            UserModel userModel = new UserModel(DBHelper.getInstance());
            userModel.saveUser(loggedInUser);
        } catch (IOException e) {
           Log.e(LOG_TAG, "", e);
        }

    }

    private static void insertOrReplace(String shelf, Volumes result) {
        if(result != null && !result.isEmpty()) {
            try {
                String volumensString = Search.JSON_FACTORY.toString(result);
                User loggedInUser = GlobalApplication.getInstance().getLoggedInUser();

                loggedInUser.getBooks().put(shelf, volumensString);

                UserModel userModel = new UserModel(DBHelper.getInstance());
                userModel.saveUser(loggedInUser);
            } catch (IOException e) {
                Log.e(LOG_TAG, "", e);
            }
        }

    }
    public static Volumes getBookshelfVolumes(String shelf) {
        Volumes result = null;
        try {

            SharedPreferencedSingleton s = SharedPreferencedSingleton.getInstance();
            String token = s.getString(Constants.PREFS_OAUTH_TOKEN, "");
            String params = String.format("refresh_token=%s&client_id=%s&grant_type=refresh_token&redirect_uri=urn:ietf:wg:oauth:2.0:oob", token, BuildConfig.CLIENT_ID);

            String refreshToken = RefreshTokenService.getRefreshToken(params);
            Log.d("LOG", "refreshToken:" + refreshToken);
            Books.Mylibrary.Bookshelves.Volumes.List shelves =  books.mylibrary().bookshelves().volumes().list(shelf)
                    .setKey(apiKey).setOauthToken(refreshToken);
            result =  shelves.execute();

            insertOrReplace(shelf, result);
        } catch (GoogleJsonResponseException e) {
            LogUtil.d("There was a service error: " + e.getDetails().getCode() + " : " + e.getMessage());
        } catch (IOException e) {
            LogUtil.d("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Exception t) {
            LogUtil.d("Ex:", t);
        }
       // Log.d("LOG", ""+result.getItems());
        return result;
    }

    public static Boolean addVolumes(String shelf, String volume) {

        Boolean result = Boolean.TRUE;
          try {

              SharedPreferencedSingleton s = SharedPreferencedSingleton.getInstance();
              String token = s.getString(Constants.PREFS_OAUTH_TOKEN, "");
              String params = String.format("refresh_token=%s&client_id=%s&grant_type=refresh_token&redirect_uri=urn:ietf:wg:oauth:2.0:oob", token, BuildConfig.CLIENT_ID);

              String refreshToken = RefreshTokenService.getRefreshToken(params);
              Log.d("LOG", "refreshToken:" + refreshToken);
            Books.Mylibrary.Bookshelves.AddVolume shelves =  books.mylibrary().bookshelves().addVolume(shelf, volume).setShelf(shelf).setVolumeId(volume)
                    .setKey(apiKey).setOauthToken(refreshToken);
             shelves.execute();
        } catch (GoogleJsonResponseException e) {
            LogUtil.d("There was a service error: " + e.getDetails().getCode() + " : " + e.getMessage());
              result = Boolean.FALSE;
        } catch (IOException e) {
            LogUtil.d("There was an IO error: " + e.getCause() + " : " + e.getMessage());
              result = Boolean.FALSE;
        } catch (Exception t) {
            LogUtil.d("Ex:", t);
              result = Boolean.FALSE;
        }
        return result;
    }


    public static Boolean removeVolumes(String shelf, String volume) {
        Boolean result = Boolean.TRUE;
        try {
            SharedPreferencedSingleton s = SharedPreferencedSingleton.getInstance();
            String token = s.getString(Constants.PREFS_OAUTH_TOKEN, "");
            String params = String.format("refresh_token=%s&client_id=933905793255-uddtumneu4cd1pr2le7cil6kmbpqkdt2.apps.googleusercontent.com&grant_type=refresh_token&redirect_uri=urn:ietf:wg:oauth:2.0:oob", token);

            String refreshToken = RefreshTokenService.getRefreshToken(params);
            Log.d("LOG", "refreshToken:" + refreshToken);
           
            Log.d("LOG",shelf+ "-" + volume);
            Books.Mylibrary.Bookshelves.RemoveVolume shelves =  books.mylibrary().bookshelves().removeVolume(shelf, volume).setShelf(shelf).setVolumeId(volume)
                    .setKey(apiKey).setOauthToken(refreshToken);
            shelves.execute();
        } catch (GoogleJsonResponseException e) {
            LogUtil.d("There was a service error: " + e.getDetails().getCode() + " : " + e.getMessage());
            result = Boolean.FALSE;
        } catch (IOException e) {
            LogUtil.d("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            result = Boolean.FALSE;
        } catch (Exception t) {
            LogUtil.d("Ex:", t);
            result = Boolean.FALSE;
        }
        return result;
    }
}
