package pl.sokolak.sonludilo.ui.albums;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;

public class Album {
    private String id;
    private String artist;
    private String title;
    private String year;
    private String noTracks;
    private List<Track> tracks;

    public Album(String id, String title, String artist, String year, String noTracks, List<Track> tracks) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.noTracks = noTracks;
        this.tracks = tracks;
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
