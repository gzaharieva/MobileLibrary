package com.lqg.googlebook.ui.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;


import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.lqg.googlebook.R;
import com.lqg.googlebook.support.http.Search;
import com.lqg.googlebook.ui.detail.BookDetailActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by LQG on 2014/12/4.
 */
@EFragment(R.layout.fragment_main)
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    @ViewById(R.id.search_pb)
    ProgressBar pb;

    private List<Volume> searchResultList = new ArrayList<>();
    private SearchResultAdapter adapter;
    private QueryTask task;

    @ViewById(R.id.listview)
    ListView listView;

    @AfterViews
    void init() {
        adapter = new SearchResultAdapter(this, searchResultList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        if (task != null)
            task.cancel(true);

        pb.setVisibility(View.VISIBLE);
        task = new QueryTask();
        task.execute(query);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @ItemClick(R.id.listview)
    void onItemClick(Volume item) {
        Intent intent = new Intent(getActivity(), BookDetailActivity_.class);
        try {
            intent.putExtra("bookInfo", Search.JSON_FACTORY.toString(item));
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class QueryTask extends AsyncTask<String, Void, Volumes> {

        @Override
        protected Volumes doInBackground(String... params) {
            return Search.searchVolumes(params[0]);
        }

        @Override
        protected void onPostExecute(Volumes searchListResponse) {
            pb.setVisibility(View.INVISIBLE);
            if (searchListResponse == null)
                return;

            onQuery(searchListResponse);
        }
    }

    private boolean onQuery(Volumes result) {
        if (result == null || result.getItems() == null)
            return false;

        searchResultList.clear();
        searchResultList.addAll(result.getItems());
        adapter.notifyDataSetChanged();
        return true;
    }

}
