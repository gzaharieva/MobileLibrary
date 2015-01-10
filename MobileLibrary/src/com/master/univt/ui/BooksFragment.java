package com.master.univt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.services.books.model.Bookshelf;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.master.univt.Constants;
import com.master.univt.R;
import com.master.univt.services.CommunicationService;
import com.master.univt.services.VolumesService;
import com.master.univt.support.http.Search;
import com.master.univt.utils.VolumeAdapter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */

public class BooksFragment extends Fragment implements CommunicationService<Volumes> {

    private final String LOG_TAG = BooksFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private TextView volumeLastUpdate;
    private TextView volumeCreated;
    private AbsListView booksListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter booksListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BooksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_list, container, false);
        booksListView = (AbsListView) view.findViewById(android.R.id.list);
        progressBar = (ProgressBar) view.findViewById(R.id.search_pb);
        volumeLastUpdate = (TextView) view.findViewById(R.id.volume_last_update);
        volumeCreated = (TextView) view.findViewById(R.id.volume_created);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        Volume volume = (Volume) booksListAdapter.getItem(position);
                        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                        try {
                            intent.putExtra("bookInfo", Search.JSON_FACTORY.toString(volume));
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        String bookInfo = getArguments().getString(Constants.BOOKSHELF);
        Bookshelf bookshelf;
        try {
            bookshelf = Search.JSON_FACTORY.fromString(bookInfo, Bookshelf.class);
            Log.d(LOG_TAG, "bookshelf"+bookshelf);

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String lastUpdated = df.format(bookshelf.getVolumesLastUpdated().getValue());
            String created = df.format(bookshelf.getCreated().getValue());
            volumeLastUpdate.setText(lastUpdated);
            volumeCreated.setText(created);


       //     if(booksListAdapter == null) {
                new VolumesService(this, getActivity()).execute(String.valueOf(bookshelf.getId()));
                progressBar.setVisibility(View.VISIBLE);
//            }else{
//                booksListView.setAdapter(booksListAdapter);
//            }


        }catch(IOException ex){
            Log.e(LOG_TAG, "", ex);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = booksListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onRequestCompleted(Volumes resultData) {
        progressBar.setVisibility(View.GONE);
        if (resultData != null) {
            booksListAdapter = new VolumeAdapter(getActivity(), resultData.getItems());
            (booksListView).setAdapter(booksListAdapter);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
