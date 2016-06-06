package com.casa.app.application.adapter;

/**
 * Created by Juan on 29/05/2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.casa.app.application.R;
import com.casa.app.application.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    public int visibility = View.GONE;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;
        public CheckBox selected;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            selected = (CheckBox) view.findViewById(R.id.chkSelected);
        }
    }


    public MoviesAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    public void setFilter(List<Movie> countryModels) {
        this.moviesList = new ArrayList<>();
        this.moviesList.addAll(countryModels);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre()+" - "+ movie.getYear() );
        holder.selected.setChecked(moviesList.get(position).isSelected());
        holder.selected.setVisibility(visibility);

        holder.selected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Movie m = (Movie) moviesList.get(position);
                m.setSelected(cb.isChecked());
                moviesList.get(position).setSelected(cb.isChecked());
            }
        });



    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
