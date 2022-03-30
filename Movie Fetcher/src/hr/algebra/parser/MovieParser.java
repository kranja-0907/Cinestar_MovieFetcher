/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.parser;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import hr.algebra.model.enumerations.TagType;
import hr.algebra.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Leon Kranjcevic
 */
public class MovieParser {

    private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=1";
    //private static final String RSS_URL = "https://www.blitz-cinestar.hr/rss.aspx?najava=2";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";
    private static final String DELIMITER = ",";
    private static final String REGEX = "\\<.*?>";

    private static void handlePicture(Movie movie, String url) {
        //neki url slike imamo

        //1. osigurati ekstenziju ako je nema ocemo .jpg
        try {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.length() > 4) {
                ext = EXT;
            }

            String picturename = UUID.randomUUID() + ext; // 1123124.jpg
            String path = DIR + File.separator + picturename; // assets/1123124.jpg

            FileUtils.copyFromUrl(url, path);

            movie.setPicturePath(path);

        } catch (IOException ex) {
            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
        }

        //2. kreirat file na filesystemu u koji ide ta slika
        //3. skidam sliku
    }
    
    private static Person getPerson(String data) {
        String[] personInfo = data.trim().split(" ", 2);
        switch(personInfo.length){
            case 1:
                return new Person(personInfo[0], "");
            case 2:
                return new Person(personInfo[0], personInfo[1]);
        }
        throw new IllegalArgumentException("Data could not be handled");
    }

    public MovieParser() {
    }

    public static List<Movie> parse() throws IOException, XMLStreamException, Exception {
        List<Movie> movies = new ArrayList<>();

        //1. otvori konekciju na RSS_URL
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);
        //2. otvori stream nad konekcijom
        try (InputStream is = con.getInputStream()) {
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            StartElement startElement = null;
            Repository repository = RepositoryFactory.getRepository();
            Movie movie = null;
            Optional<TagType> tagType = Optional.empty();
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        String data = characters.getData().trim();
                        if (tagType.isPresent()) {
                            switch (tagType.get()) { //item, title, link, description...
                                case ITEM:
                                    movie = new Movie();
                                    movies.add(movie);
                                    break;
                                case HRTITLE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setTitle(data);
                                    }
                                    break;
                                case PUB_DATE:
                                    //Mon, 17 May 2021 11:40:08 GMT
                                    if (movie != null && !data.isEmpty()) {
                                        LocalDateTime publishedDate = LocalDateTime.parse(data, DateTimeFormatter.RFC_1123_DATE_TIME);
                                        movie.setPublishedDate(publishedDate);
                                    }
                                    break;
                                case DESCRIPTION:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDescription(removeHtml(data));
                                    }
                                    break;
                                case ENTITLE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setEngTitle(data);
                                    }
                                    break;
                                case DIRECTOR:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDirector(data);
                                    }
                                    break;
                                case ACTOR:
                                    if (movie != null && !data.isEmpty()) {
                                        String[] peopleInfo = data.split(DELIMITER);
                                        for (String personInfo : peopleInfo) {
                                            int id = repository.createPerson(getPerson(personInfo));
                                        }
                                        movie.setActors(data);
                                    }
                                    break;
                                case DURATION:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setDuration(Integer.parseInt(data));
                                    }
                                    break;
                                case GENRE:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setGenres(data);
                                    }
                                    break;
                                case PICTURE:
                                    if (movie != null && movie.getPicturePath() == null) {
                                        handlePicture(movie, data);
                                    }
                                    break;
                                case LINK:
                                    if (movie != null && !data.isEmpty()) {
                                        movie.setLink(data);
                                    }
                                    break;
                            }
                        }
                        break;
                }
            }
        }
        //3. kreirati XML parser
        //4. parsiranje
        System.out.println(movies);
        return movies;
    }

    private static String removeHtml(String data) {
        return data.replaceAll(REGEX, "");
    }
}
