package pl.sokolak.sonludilo.ui.tracks;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;

public class Track {
    private Uri uri;
    private String artist;
    private String title;
    private String album;
    private String year;

    public Track(Uri uri, String artist, String title, String album, String year) {
        this.uri = uri;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.year = year;
    }

    public String toMultiLineString(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(addStringItem(context.getString(R.string.artist), artist));
        stringBuilder.append(addStringItem(context.getString(R.string.title), title));
        stringBuilder.append(addStringItem(context.getString(R.string.album), album));
        stringBuilder.append(addStringItem(context.getString(R.string.year), year));
        return stringBuilder.toString();
    }

    private String addStringItem(String prefix, String item) {
        if(item != null && !item.isEmpty() && !item.equals("<unknown>")) {
            return prefix + ": " + item.trim() + "\n";
        }
        return "";
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
