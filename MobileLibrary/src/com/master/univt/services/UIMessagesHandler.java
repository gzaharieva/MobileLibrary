package com.master.univt.services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.master.univt.R;

/**
 * Handles the application messages. f.e there is not internet connection or no connection to REST
 * API server. Handle progressing dialogs.
 * 
 * @author Gabriela Zaharieva
 */
public class UIMessagesHandler extends Handler
{

  private static UIMessagesHandler instance;
  private Activity context;
  private Builder alertBuilder;
  private ProgressDialog progressDialog;

  private UIMessagesHandler()
  {
  }

  public static UIMessagesHandler getInstance()
  {
    if (instance == null)
    {
      instance = new UIMessagesHandler();
    }
    return instance;
  }

  public void Initialize(final Activity context)
  {
    this.context = context;
    this.alertBuilder = new Builder(context);
    alertBuilder.setCancelable(false);
    progressDialog = new ProgressDialog(context);
    progressDialog.setCanceledOnTouchOutside(false);
  }

  public Context getContext()
  {
    return context;
  }

  @Override
  public void handleMessage(final Message message)
  {
    super.handleMessage(message);
    progressDialog.dismiss();

    switch (message.what)
    {
      case -1:
        /* */
        showToastMessage(context.getString(R.string.message_no_internet_connection));
        break;
      case 0:
        showToastMessage(context.getString(R.string.message_no_internet_connection));
        break;
      case 1:
        alertBuilder = new Builder(context);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage(context.getString(R.string.message_no_internet_connection));
        alertBuilder.setNeutralButton(context.getString(R.string.ok),
          new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(final DialogInterface dialog, final int which)
            {
              dialog.dismiss();
            }
          });
        try
        {
          if (!context.isFinishing())
          {
            alertBuilder.show();
          }
        }
        catch (Exception ex)
        {
          Log.e("LOG", "Unable to add window token android.os.BinderProxyis not valid; is your activity running?", ex);
          
        }
        break;
    }

  }

  public Builder getAlert()
  {
    alertBuilder = new Builder(context);
    alertBuilder.setCancelable(false);
    return alertBuilder;
  }

  public void setAlert(final String msg)
  {

    alertBuilder = new Builder(context);
    alertBuilder.setMessage(msg);
    alertBuilder.setNeutralButton(context.getString(R.string.ok), new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(final DialogInterface dialog, final int which)
      {
        dialog.dismiss();
      }
    });
    alertBuilder.setCancelable(false);
    try
    {
      if (!context.isFinishing())
      {
        alertBuilder.show();
      }
    }
    catch (Exception ex)
    {
      Log.e("LOG", "Unable to add window token android.os.BinderProxyis not valid; is your activity running?", ex);
      
    }
  }

  public void setAlert(final String title, final String message)
  {
    alertBuilder = new Builder(context);
    alertBuilder.setTitle(title).setMessage(message);
    alertBuilder.setNeutralButton(context.getString(R.string.ok), new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(final DialogInterface dialog, final int which)
      {
        dialog.dismiss();
      }
    });
    alertBuilder.setCancelable(false);
    try
    {
      if (!context.isFinishing())
      {
        alertBuilder.show();
      }
    }
    catch (Exception ex)
    {
      Log.e("LOG", "Unable to add window token android.os.BinderProxyis not valid; is your activity running?", ex);
      
    }
  }

  public void setAlert(final String title, final String message, final int positiveResId, final int negativeResId)
  {
    alertBuilder = new Builder(context);
    alertBuilder.setTitle(title).setMessage(message);
    alertBuilder.setPositiveButton(positiveResId, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(final DialogInterface dialog, final int which)
      {
        // TODO Find out a solution to handle the code
        // callAPIForBouhtContent(postParams);
      }
    });
    alertBuilder.setNegativeButton(negativeResId, new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(final DialogInterface dialog, final int which)
      {
        // do nothing
      }
    });
    alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
    try
    {
      if (!context.isFinishing())
      {
        alertBuilder.show();

      }
    }
    catch (Exception ex)
    {
      Log.e("LOG", "Unable to add window token android.os.BinderProxyis not valid; is your activity running?", ex);
    
    }
  }

  public void showToastMessage(final String message)
  {

    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  public void showToastMessagelong(final String message, final int durationInMilliSeconds)
  {
    if (context != null && !context.isFinishing())
    {
      Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);

      TextView toastTextView = (TextView) toast.getView().findViewById(android.R.id.message);
      if (toastTextView != null)
      {
        toastTextView.setGravity(Gravity.CENTER);
      }

       toast.show();
      
    }
  }
}
