package com.master.univt.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Bookshelves;
import com.master.univt.Constants;
import com.master.univt.R;
import com.master.univt.entities.ParameterTag;
import com.master.univt.model.User;
import com.master.univt.support.GlobalApplication;
import com.master.univt.support.http.Search;
import com.master.univt.support.http.UserLibrary;
import com.master.univt.utils.GridBookshelfListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The user courses fragment.
 *
 * @author Gabriela Zaharieva
 */
public class BookshelvesFragment extends Fragment {

    private static final String LOG_TAG = BookshelvesFragment.class.getSimpleName();
    public Context context;
    private Bookshelves bookshelvesApi;
    private View progressView;
    // interface via which we communicate to hosting Activity

    private View rootView;
    private GridView bookshelvesGridView;
    /**
     * The grid view adapter.
     */
    private GridBookshelfListAdapter gridViewAdapter;


    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        context = getActivity();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bookshelves, container, false);
        bookshelvesGridView = (GridView) rootView.findViewById(R.id.grid_view);
        progressView = rootView.findViewById(R.id.progress);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        if (gridViewAdapter == null) {
            new QueryTask().execute();
            progressView.setVisibility(View.VISIBLE);
        } else {
            bookshelvesGridView.setAdapter(gridViewAdapter);
        }
        bookshelvesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bookshelf bookshelf = gridViewAdapter.getItem(position);
                BooksFragment booksFragment = new BooksFragment();
                Bundle booksBundle = new Bundle();
                try {
                    booksBundle.putString(Constants.BOOKSHELF, Search.JSON_FACTORY.toString(bookshelf));
                } catch (IOException ex) {
                    Log.e(LOG_TAG, "", ex);
                }
                booksFragment.setArguments(booksBundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, booksFragment, ParameterTag.FRAGMENT_COURSE)
                        .addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private class QueryTask extends AsyncTask<String, Void, Bookshelves> {

        @Override
        protected Bookshelves doInBackground(String... params) {
            return UserLibrary.getBookshelves();
        }

        @Override
        protected void onPostExecute(Bookshelves bookshelves) {

            if (bookshelves != null && bookshelves.getItems() != null) {
                bookshelvesApi = bookshelves;
            } else {
                User loggedInUser = GlobalApplication.getInstance().getLoggedInUser();
                try {
                    Log.d(LOG_TAG, "Get local bookshelves");
                    String bookshelvesString = loggedInUser.getBookshelvesString();
                    if(bookshelvesString != null) {
                        Bookshelves userBookshelves = Search.JSON_FACTORY.fromString(bookshelvesString, Bookshelves.class);
                        bookshelvesApi = userBookshelves;
                    }
                } catch (IOException e) {
                    Log.e(LOG_TAG, "", e);
                }
            }
            if (bookshelvesApi != null && bookshelvesApi.getItems() != null) {
                List<Bookshelf> publicBookshelves = new ArrayList<>();
                for (Bookshelf shelf : bookshelvesApi.getItems()) {
                    if (shelf.getAccess().equalsIgnoreCase("public") || shelf.getVolumeCount() > 0) {
                        publicBookshelves.add(shelf);
                    }
                }

                gridViewAdapter = new GridBookshelfListAdapter(getActivity(), 0, 0, publicBookshelves);
                bookshelvesGridView.setAdapter(gridViewAdapter);
            }else{
                
            }
            progressView.setVisibility(View.GONE);
        }
    }
}
