package pl.sokolak.sonludilo.ui.tracks;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;

public class Track {
    private Uri uri;
    private int no;
    private String artist;
    private String title;
    private String album;
    private String year;

    public Track(Uri uri, int no, String artist, String title, String album, String year) {
        this.uri = uri;
        this.no = no;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.year = year;
    }

    @NotNull
    public String toString() {
        return no + ". " + artist + " - " + title;
    }

    public String toMultiLineString(Context context) {
        return addStringItem(context.getString(R.string.artist), artist) +
                addStringItem(context.getString(R.string.title), title) +
                addStringItem(context.getString(R.string.album), album) +
                addStringItem(context.getString(R.string.year), year);
    }

    private String addStringItem(String prefix, String item) {
        if (item != null && !item.isEmpty() && !item.equals("<unknown>")) {
            return prefix + ": " + item.trim() + "\n";
        }
        return "";
    }

    public Uri getUri() {
        return uri;
    }
}
