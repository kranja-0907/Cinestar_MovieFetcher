/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Leon Kranjcevic
 */
public class MovieTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Id", "Title", "Director", "Actors", "Duration in minutes", "Genres", "Description", "Eng title", "Picture path", "Published date"};

    private List<Movie> movies;

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        fireTableDataChanged(); //refresh
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 4:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        //return Movie.class.getDeclaredFields().length - 1;
        return 10;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return movies.get(rowIndex).getId();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                return movies.get(rowIndex).getDirector();
            case 3:
                return movies.get(rowIndex).getActors();
            case 4:
                return movies.get(rowIndex).getDuration();
            case 5:
                return movies.get(rowIndex).getGenres();
            case 6:
                return movies.get(rowIndex).getDescription();
            case 7:
                return movies.get(rowIndex).getEngTitle();
            case 8:
                return movies.get(rowIndex).getPicturePath();
            case 9:
                return movies.get(rowIndex).getPublishedDate().format(Movie.DATE_FORMAT);
        }
        throw new RuntimeException("No such column");
    }
}
