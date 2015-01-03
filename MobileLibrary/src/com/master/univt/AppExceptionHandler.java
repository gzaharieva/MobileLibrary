package com.master.univt;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

//import com.crashlytics.android.Crashlytics;

/**
 * Handles the unexpected exception during the application usage. There is possibilities to restart
 * the current view or start the application from his start point.
 * 
 * @author gzaharieva
 */
public class AppExceptionHandler implements Thread.UncaughtExceptionHandler
{
  private final String LOG_TAG = AppExceptionHandler.class.getSimpleName();
  private final Context context;
  private final Class<?> appClass;

  public AppExceptionHandler(final Context context, final Class<?> appClass)
  {
    this.context = context;
    this.appClass = appClass;
  }

  @Override
  public void uncaughtException(final Thread thread, final Throwable exception)
  {
    StringWriter stackTrace = new StringWriter();
    exception.printStackTrace(new PrintWriter(stackTrace));

    Log.e(LOG_TAG, "", exception);
    //Crashlytics.logException(exception);

    Intent intent = new Intent(context, appClass);
    String s = stackTrace.toString();
    // you can use this String to know what caused the exception and in
    // which Activity
    intent.putExtra("uncaughtException", "Exception is: " + stackTrace.toString());
    intent.putExtra("stacktrace", s);
    // context.startActivity(intent);
    // for restarting the Activity
    // Process.killProcess();

    System.exit(0);
  }
}
