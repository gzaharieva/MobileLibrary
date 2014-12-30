package com.master.univt.support.util;

import android.preference.PreferenceManager;

import com.master.univt.support.GlobalApplication;
import com.master.univt.ui.search.SearchSetingFragment;

/**
 * Created by LQG on 2014/12/10.
 */
public class SearchSetting {
    private String keywordsType, download, filter, maxResults, printType, projection, orderBy;
    private static SearchSetting ourInstance = new SearchSetting();

    private SearchSetting() {
    }

    public static SearchSetting getInstance() {
        return ourInstance;
    }

    public String getMaxResults() {
        if (maxResults == null)
            maxResults = getMaxResultsFromPre();
        return maxResults;
    }

    public static String getMaxResultsFromPre() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance()).getString(SearchSetingFragment.MAXRESULTS, "20");
    }

    public String getKeywordsType() {
        if (keywordsType == null)
            keywordsType = getKeywordsTypeFromPre();
        return keywordsType;
    }

    public static String getKeywordsTypeFromPre() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance()).getString(SearchSetingFragment.KEYWORDSTYPE, "intitle");
    }

    public String getDownload() {
        if (download == null)
            download = getDownloadFromPre();
        return download;
    }

    public static String getDownloadFromPre() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance()).getBoolean(SearchSetingFragment.DOWNLOAD, false) ? "epub" : null;
    }

    public String getFilter() {
        if (filter == null)
            filter = getFilterFromPre();
        return filter;
    }

    public static String getFilterFromPre() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance()).getString(SearchSetingFragment.FILTER, "partial");
    }

    public String getPrintType() {
        if (printType == null)
            printType = getPrintTypeFromPre();
        return printType;
    }

    public static String getPrintTypeFromPre() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance()).getString(SearchSetingFragment.PRINTTYPE, "all");
    }

    public String getProjection() {
        if (projection == null)
            projection = getProjectionFromPre();
        return projection;
    }

    public static String getProjectionFromPre() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance()).getString(SearchSetingFragment.PROJECTION, "full");
    }

    public String getOrderBy() {
        if (orderBy == null)
            orderBy = getOrderByFromPer();
        return orderBy;
    }

    public static String getOrderByFromPer() {
        return PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance()).getString(SearchSetingFragment.ORDERBY, "relevance");
    }

    public void setKeywordsType(String keywordsType) {
        this.keywordsType = keywordsType;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
