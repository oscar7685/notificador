package com.casa.app.application;

/**
 * Created by Oscar on 28/05/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.casa.app.application.R;
import com.casa.app.application.adapter.MoviesAdapter;
import com.casa.app.application.database.NotificacionSQLiteHelper;
import com.casa.app.application.model.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MovieFragment extends  Fragment implements SearchView.OnQueryTextListener{
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private CheckBox chk;
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Movie> filteredModelList = filter(movieList, newText);
        mAdapter.setFilter(filteredModelList);
        return true;
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_select_all).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Movie movie = movieList.get(position);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getUrl()));
                            startActivity(browserIntent);
                            }


                                @Override
                        public void onLongClick(View view, int position) {

                                    }
                    }));


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = viewHolder.getAdapterPosition();
                        Movie movie = movieList.get(position);
                        movieList.remove(position);
                        eliminarMovie(movie.getCodigo());
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyDataSetChanged();
                        getActivity().setTitle("Notificaciones ("+movieList.size()+")");

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        mAdapter = new MoviesAdapter(movieList);
        mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        // Inflate the layout for this fragment
        return rootView;
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }
    private void prepareMovieData() {
        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        NotificacionSQLiteHelper Ntdbh =    new NotificacionSQLiteHelper(getActivity(), "DBNotificiones", null, 1);
        SQLiteDatabase db = Ntdbh.getWritableDatabase();
        Cursor fila = db.rawQuery("select codigo , nombre, genero, year, url from Notificacion", null);
        fila.moveToFirst();
        while(!fila.isAfterLast()) {
            Movie m = new Movie(fila.getInt(0), fila.getString(1), fila.getString(2), fila.getString(3),fila.getString(4));
            fila.moveToNext();
            movieList.add(m);
            mAdapter.notifyDataSetChanged();
        }
        getActivity().setTitle("Notificaciones ("+movieList.size()+")");
        fila.close();
        db.close();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void eliminarMovie(int codigoMovie){
     try{   // Abrimos una conexión
        NotificacionSQLiteHelper Ntdbh =    new NotificacionSQLiteHelper(getActivity(), "DBNotificiones", null, 1);
        SQLiteDatabase db = Ntdbh.getWritableDatabase();
        // Consultamos los datos
        int cant = db.delete("Notificacion", "codigo=" + codigoMovie, null);
        db.close();
        // Cerramos la conexion

    } catch (Exception ex) {
         System.out.println("Algo ha ido mal, capturamos la excepcion");

    }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        prepareMovieData();
        recyclerView.setAdapter(mAdapter);
    }


    private List<Movie> filter(List<Movie> models, String query) {
        query = query.toLowerCase();

        final List<Movie> filteredModelList = new ArrayList<>();
        for (Movie model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.action_search);

        final MenuItem item2 = menu.findItem(R.id.action_settings);

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                m.findItem(R.id.action_delete).setVisible(true);
                m.findItem(R.id.action_select_all).setVisible(true);
                mAdapter.visibility = View.VISIBLE;
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        final MenuItem item3 = menu.findItem(R.id.action_delete);
        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<Movie> movieList2 = new ArrayList<Movie>();
                for (int i = 0; i < movieList.size() ; i++) {
                    if(movieList.get(i).isSelected()){
                        eliminarMovie(movieList.get(i).getCodigo());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                getActivity().setTitle("Notificaciones ("+movieList.size()+")");

                m.findItem(R.id.action_delete).setVisible(false);
                m.findItem(R.id.action_select_all).setVisible(false);
                mAdapter.visibility = View.GONE;
                mAdapter.notifyDataSetChanged();
                return true;
            }
        });

        final MenuItem item4 = menu.findItem(R.id.action_select_all);
        item4.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for (int i = 0; i < movieList.size() ; i++) {
                        movieList.get(i).setSelected(true);
                }
                mAdapter.notifyDataSetChanged();
               return true;
            }
        });

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(movieList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MovieFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MovieFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

       @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}