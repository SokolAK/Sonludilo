package pl.sokolak.sonludilo.ui.tracks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    public String toMultiLineString(Context context) {
        String string = addStringItem(context.getString(R.string.artist), artist) +
                addStringItem(context.getString(R.string.title), title) +
                addStringItem(context.getString(R.string.album), album) +
                addStringItem(context.getString(R.string.year), year) +
                addStringItem(context.getString(R.string.time), Utils.formatTime(duration));
        return string.trim();
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

    public int getDuration() {
        return duration;
    }
}
