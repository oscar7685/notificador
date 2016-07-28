package com.casa.app.application.model;

/**
 * Created by Juan on 29/05/2016.
 */
public class Movie {
    private String title;
    private String genre;
    private String year;
    private String url;
    private boolean isSelected;
    private int visibility;
    private int codigo;

    public Movie() {
    }

    public Movie(String title, String genre, String year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.setUrl("http://www.google.com");
    }

    public Movie(int codigo, String title, String genre, String year, String url) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.codigo = codigo;
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
