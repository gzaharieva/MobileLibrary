package com.master.univt;

import static com.master.univt.Constants.IMAGES;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.nostra13.universalimageloader.utils.L;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

/**
 * The user courses fragment.
 * 
 * @author Gabriela Zaharieva
 */
public class BookshelvesFragment extends Fragment
{
	private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";
  public Context context;
  // interface via which we communicate to hosting Activity
  
  private View rootView;
    private GridView coursesGridView;
    /** The grid view adapter. */
    private GridCourseListAdapter gridViewAdapter;


  @Override
  public void onAttach(final Activity activity)
  {
    super.onAttach(activity);
    context = getActivity();
  }

  @Override
  public void onSaveInstanceState(final Bundle outState)
  {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
  {
    rootView = inflater.inflate(R.layout.ac_home, container, false);
      coursesGridView = (GridView) rootView.findViewById(R.id.grid_view);

      setHasOptionsMenu(true);
    return rootView;
  }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {


    }
}
