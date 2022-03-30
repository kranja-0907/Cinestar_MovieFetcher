/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Leon Kranjcevic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "publishedDate", "description", "engTitle", "director", "actors", "duration", "genres", "picturePath", "link"})
public class Movie {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @XmlAttribute
    private int id;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "publisheddate")
    @XmlJavaTypeAdapter(MovieDateAdapter.class)
    private LocalDateTime publishedDate;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "originaltitle")
    private String engTitle;
    @XmlElement(name = "director")
    private String director;
    @XmlElement(name = "actor")
    private String actors; //Lista ili ne...
    @XmlElement(name = "duration")
    private int duration;
    @XmlElement(name = "genres")
    private String genres;
    @XmlElement(name = "picturepath")
    private String picturePath;
    @XmlElement(name = "link")
    private String link;

    public Movie() {
    }

    public Movie(int id, String title, LocalDateTime publishedDate, String description, String engTitle, String director, String actors, int duration, String genres, String picturePath, String link) {
        this(title, publishedDate, description, engTitle, director, actors, duration, genres, picturePath, link);
        this.id = id;
    }

    public Movie(String title, LocalDateTime publishedDate, String description, String engTitle, String director, String actors, int duration, String genres, String picturePath, String link) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.description = description;
        this.engTitle = engTitle;
        this.director = director;
        this.actors = actors;
        this.duration = duration;
        this.genres = genres;
        this.picturePath = picturePath;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEngTitle() {
        return engTitle;
    }

    public void setEngTitle(String engTitle) {
        this.engTitle = engTitle;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return id + " - " + title;
    }

}
