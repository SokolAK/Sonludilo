package pl.sokolak.sonludilo.ui.tracks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.Utils;

public class Track {
    private Uri uri;
    private String no;
    private String artist;
    private String title;
    private String album;
    private String year;
    private int duration;

    public Track() {
    }

    public Track(Uri uri, String no, String artist, String title, String album, String year, int duration) {
        this.uri = uri;
        this.no = Utils.notNull(no);
        this.artist = Utils.notNull(artist);
        this.title = Utils.notNull(title);
        this.album = Utils.notNull(album);
        this.year = Utils.notNull(year);
        this.duration = duration;
    }

    @SuppressLint("DefaultLocale")
    @NotNull
    public String toString() {
        return no + ". " + artist + " - " + title + " (" + Utils.formatTime(duration) + ")";
    }

    public String toMultiLineStringShort() {
        return no + ". " + title + " (" + Utils.formatTime(duration) + ")\n" +
                artist +
                (!album.isEmpty() ? " - " + album : "") +
                (!year.isEmpty() ? " [" + year +"]" : "");
    }

    public Uri getUri() {
        return uri;
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
