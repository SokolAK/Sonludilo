package pl.sokolak.sonludilo.ui.albums;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;
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

    public String toMultiLineString(Context context) {
        return addStringItem(context.getString(R.string.artist), artist) +
                addStringItem(context.getString(R.string.title), title) +
                addStringItem(context.getString(R.string.number_of_tracks), noTracks) +
                addStringItem(context.getString(R.string.year), year);
    }

    private String addStringItem(String prefix, String item) {
        if(item != null && !item.isEmpty()) {
            return prefix + ": " + item.trim() + "\n";
        }
        return "";
    }
}
