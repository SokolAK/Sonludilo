package pl.sokolak.sonludilo.tabs.tracks;

import android.annotation.SuppressLint;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import pl.sokolak.sonludilo.Utils;

public class Track {
    private Uri uri;
    private int no;
    private String artist;
    private String title;
    private String album;
    private String year;
    private int duration;

    public Track() {
    }

    public Track(Uri uri, int no, String artist, String title, String album, String year, int duration) {
        this.uri = uri;
        this.no = no;
        this.artist = Utils.notNull(artist);
        this.title = Utils.notNull(title);
        this.album = Utils.notNull(album);
        this.year = Utils.notNull(year);
        this.duration = duration;
    }

    @SuppressLint("DefaultLocale")
    @NotNull
    public String toString() {
        return (no > 0 ? no + ". " : "") + artist + " - " + title + " (" + Utils.formatTime(duration) + ")";
    }

    public String toMultiLineStringShort() {
        return (no > 0 ? no + ". " : "") +
                title + " (" + Utils.formatTime(duration) + ")\n" +
                artist +
                (!album.isEmpty() ? " - " + album : "") +
                (!year.isEmpty() ? " [" + year + "]" : "");
    }

    public Uri getUri() {
        return uri;
    }

    public int getNo() {
        return no;
    }

    public int getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return uri.equals(track.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }
}
