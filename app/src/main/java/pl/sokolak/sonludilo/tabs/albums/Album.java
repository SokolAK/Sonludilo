package pl.sokolak.sonludilo.tabs.albums;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pl.sokolak.sonludilo.tabs.tracks.Track;

public class Album {
    private final String id;
    private final String artist;
    private final String title;
    private final String year;
    private final String noTracks;

    public Album(String id, String title, String artist, String year, String noTracks) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.noTracks = noTracks;
    }

    public String getId() {
        return id;
    }

    @NotNull
    public String toString() {
        return artist + " - " + title + " [" + year + "]";
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getNoTracks() {
        return noTracks;
    }
}
