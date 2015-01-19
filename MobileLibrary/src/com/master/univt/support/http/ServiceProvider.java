package com.master.univt.support.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.master.univt.services.RequestResponse;
import com.master.univt.services.UIMessagesHandler;
import com.master.univt.support.GlobalApplication;


/**
 * Services provider for HTTP GET, POST requests.
 * 
 * @author Gabriela Zaharieva
 */
public class ServiceProvider extends Service
{

  private static final String LOG_TAG = ServiceProvider.class.getSimpleName();
  /** The server call time out. */
  public final int HTTP_TIMEOUT = 60 * 1000;
  private static ServiceProvider instance;
  public DefaultHttpClient httpClient = null;

  private UIMessagesHandler uiMessageHandler;

  private ServiceProvider()
  {
  }

  public static ServiceProvider getInstance()
  {
    if (instance == null)
    {
      instance = new ServiceProvider();
    }
    return instance;
  }

  public DefaultHttpClient getHttpClient()
  {
    if (httpClient == null)
    {
    	httpClient = new DefaultHttpClient();

        ClientConnectionManager mgr = httpClient.getConnectionManager();

//      httpClient = new DefaultHttpClient();
      final HttpParams params = httpClient.getParams();
      params.setParameter("http.protocol.content-charset", HTTP.UTF_8);
      HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
      HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
      ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
      
      httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params,

              mgr.getSchemeRegistry()), params);
    }
    try
    {
      uiMessageHandler = UIMessagesHandler.getInstance();
    }
    catch (Exception ex)
    {
      Log.e(LOG_TAG, "Can not initialize Message handler. " + ex.getMessage());
    }
    return httpClient;
  }
  

  

  public static boolean isNetworkConnected(final Context context)
  {
    boolean isConnected = false;
    if (context != null)
    {
      ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

      if (activeNetwork == null)
      {
        return false;
      }

      isConnected = activeNetwork.isConnectedOrConnecting();
      // boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }
    return isConnected;
  }

  public RequestResponse executeHttpPost(final String url,
    final Map<String, Object> postParameters) throws Exception
  {
    RequestResponse result = null;
      DefaultHttpClient client  = new DefaultHttpClient();

      HttpPost request = new HttpPost(url);
//      request.addHeader("Cache-Control", "no-cache");
//      request.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
    int statusCode = 0;
    HttpEntity httpEntity = null;
    if (isNetworkConnected())
    {
      try
      {
        if (postParameters != null)
        {
//          List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//          for (Map.Entry<String, Object> cursor : postParameters.entrySet())
//          {
//            postParams.add(new BasicNameValuePair(cursor.getKey(), cursor.getValue() != null ? cursor.getValue().toString()
//              : null));
//          }
         // UrlEncodedFormEntity urlEncodedForm = new UrlEncodedFormEntity(postParams);
          //urlEncodedForm.setContentEncoding(HTTP.UTF_8);
//            urlEncodedForm.setContentType("application/json");
        //    urlEncodedForm.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        //    request.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");



           // request.setEntity(urlEncodedForm);
//            Log.d(LOG_TAG, "request:"+ request.getEntity().getContent());
        }

        HttpResponse response = client.execute(request);
        statusCode = response.getStatusLine().getStatusCode();

        httpEntity = response.getEntity();
        InputStream inStream = httpEntity.getContent();
        String resultStream = null;
        resultStream = getConvertedStreamResponse(inStream);

        result = new RequestResponse(statusCode, resultStream);
      }
      catch (Exception ex)
      {
        /*-
        long contentLength = 0;
        String contentType = null;
        if (httpEntity != null)
        {
          contentLength = httpEntity.getContentLength();
          contentType = httpEntity.getContentType().getValue();
        }
          ex = new Exception( String.format(
          "An error has occured while requesting the server API. URL: %s  Status code:%d Content Type value: %s Content length: %d"
          , url, statusCode, contentType, contentLength), ex); Crashlytics.logException(ex);
         */
        Log.e(LOG_TAG, "Service Exception.", ex);
        result = null;
      }
    }
    // else if (uiMessageHandler != null)
    // {
    // Message msg = uiMessageHandler.obtainMessage(0);
    // uiMessageHandler.sendMessage(msg);
    // }

    return result;
  }

  private boolean isNetworkConnected()
  {
    return isNetworkConnected(GlobalApplication.getInstance().getApplicationContext());
  }


  public RequestResponse executeHttpGet(final String apiVersion, final String url) throws Exception
  {
    String newUrl = apiVersion.concat(url);
    RequestResponse result = null;
    DefaultHttpClient client  = new DefaultHttpClient();

    HttpGet request = new HttpGet();
    request.setURI(new URI(newUrl.toString().replace(" ", "%20")));
//    request.setHeader(new BasicHeader("Prama", "no-cache"));
//    request.setHeader("Cache-Control", "no-cache");
    int statusCode = 0;
    HttpEntity httpEntity = null;
    if (isNetworkConnected())
    {
      try
      {
        HttpResponse response = client.execute(request);
//        response.getEntity().consumeContent();
        statusCode = response.getStatusLine().getStatusCode();
        httpEntity = response.getEntity();
        InputStream entityContent = httpEntity.getContent();
        String resultStream = getConvertedStreamResponse(entityContent);
        result = new RequestResponse(statusCode, resultStream);
      }
      catch (IOException ex)
      {
        Log.e(LOG_TAG, "Can not execute the API Call." + ex.getLocalizedMessage());
      }
      catch (IllegalStateException ex)
      {
        Log.e(LOG_TAG, "Can not execute the API Call." + ex.getLocalizedMessage());
      }
      catch (Exception ex)
      {
        /*-
        long contentLength = 0;
        String contentType = null;
        if (httpEntity != null)
        {
          contentLength = httpEntity.getContentLength();
          contentType = httpEntity.getContentType().getValue();
        }
        ex =
          new Exception(
            String.format(
              "An error has occured while requesting the server API. URL: %s  Status code:%d Content Type value: %s Content length: %d",
              newUrl, statusCode, contentType, contentLength), ex);
        Crashlytics.logException(ex);
         */
        Log.e(LOG_TAG, "Exception.", ex);
        result = null;
      }
    }
    else if (uiMessageHandler != null)
    {
      Message message = uiMessageHandler.obtainMessage(0);
      uiMessageHandler.sendMessage(message);
    }

    return result;
  }

  public static String getConvertedStreamResponse(final InputStream inputStream) throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder stringBuilder = new StringBuilder();
    String result;
    String line = null;
    try
    {
      while ((line = reader.readLine()) != null)
      {
        stringBuilder.append(line + "\n");
      }
      result = stringBuilder.toString();
    }
    finally
    {
      try
      {
        inputStream.close();
      }
      catch (IOException ex)
      {
        Log.e(LOG_TAG, "I/O exception.", ex);
      }
    }
    return result;
  }

  @Override
  public IBinder onBind(final Intent arg0)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
