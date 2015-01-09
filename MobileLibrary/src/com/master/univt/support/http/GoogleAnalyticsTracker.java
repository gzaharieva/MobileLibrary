//package com.master.univt.support.http;
//
//import android.content.Context;
//
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Fields;
//import com.google.analytics.tracking.android.MapBuilder;
//import com.google.analytics.tracking.android.Tracker;
//
///**
// * Google analytics tracker.
// *
// * @author Gabriela Zaharieva
// */
//public class GoogleAnalyticsTracker
//{
//  /** The easy google analytics tracker. */
//  private final Tracker googleAnlyticsTracker;
//
//  /**
//   * Initialization of the Google analytics tracker.
//   *
//   * @param context the context of the tracker
//   */
//  public GoogleAnalyticsTracker(final Context context)
//  {
//    googleAnlyticsTracker = EasyTracker.getInstance(context);
//  }
//
//  /**
//   * Tracks view/screens on start.
//   *
//   * @param screenName the name of the screen.
//   */
//  public void trackView(final String screenName)
//  {
//    googleAnlyticsTracker.set(Fields.SCREEN_NAME, screenName);
//    googleAnlyticsTracker.send(MapBuilder.createAppView().build());
//  }
//
//  public void trackTransaction(final int transactionID, final String affiliation, final Double revenue, final Double tax,
//    final Double shipping, final String currencyCode)
//  {
//    googleAnlyticsTracker.send(MapBuilder.createTransaction(String.valueOf(transactionID), affiliation, revenue, tax,
//      shipping, currencyCode).build());
//  }
//
//  public void trackItem(final int transactionID, final String itemName, final String itemSKU, final String itemCategory,
//    final Double itemPrice, final Long itemQuantity, final String currencyCode)
//  {
//    googleAnlyticsTracker.send(MapBuilder.createItem(String.valueOf(transactionID), itemName, itemSKU, itemCategory,
//      itemPrice, itemQuantity, currencyCode).build());
//  }
//
//  /**
//   * Tracks event with action and label.
//   *
//   * @param category the category of the event
//   * @param action the action of the event
//   * @param label the label for the event
//   */
//  public void trackEvent(final String category, final String action, final String label)
//  {
//    googleAnlyticsTracker.send(MapBuilder.createEvent(category, action, label, null).build());
//  }
//}
