package com.master.univt.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.master.univt.R;

public class PagerUtils
{

  public PagerUtils()
  {
  }

  public void addTab(final Context activity, final TabHost tabHost, final TabHost.TabSpec tabSpec)
  {
    tabSpec.setContent(new TabFactory(activity));
    tabHost.addTab(tabSpec);
  }

  public View makeTabIndicator(final String tabTitle, final Context context)
  {
    View view = null;
    if (context != null)
    {
      view = LayoutInflater.from(context).inflate(R.layout.tab_indicator_holo, null);

      TextView tabTitleTextView = (TextView) view.findViewById(R.id.title);
      tabTitleTextView.setText(tabTitle);
    }
    return view;
  }

  public void initialiseTabHost(final Context context, final TabHost tabHostView, final String... tabstitles)
  {
    tabHostView.setup();
    tabHostView.clearAllTabs();
    for (String title : tabstitles)
    {
      addTab(context, tabHostView, tabHostView.newTabSpec(title).setIndicator(makeTabIndicator(title, context)));
    }
    tabHostView.getTabWidget().setEnabled(true);
  }

  public int dpToPx(final Context context, final float dpMeasure)
  {
    int pixels = 0;
    if (context != null)
    {
      final float scale = context.getResources().getDisplayMetrics().density;
      pixels = (int) (dpMeasure * scale);
    }
    return pixels;
  }

    public class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * Constructor for tabs factory.
         *
         * @param context the context
         */
        public TabFactory(final Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(final String tag) {
            View view = new View(mContext);
            view.setMinimumWidth(0);
            view.setMinimumHeight(0);
            return view;
        }
    }

}
