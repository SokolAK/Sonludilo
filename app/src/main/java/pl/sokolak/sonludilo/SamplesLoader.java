package pl.sokolak.sonludilo;

import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import pl.sokolak.sonludilo.tabs.albums.Album;
import pl.sokolak.sonludilo.tabs.tracks.Track;


public class SamplesLoader {
    private static final String path = "android.resource://pl.sokolak.sonludilo/raw/";
    private static final String albumName = "Sonludilo - 1261 Clan";
    private static final String year = "2021";

    public static List<Track> getSampleTracks() {
        List<Track> trackList = new ArrayList<>();

        trackList.add(new Track(Uri.parse(path + "sample1"),
                1, "2 Souls ft. Nara", "Lonely", albumName, year, 14000));
        trackList.add(new Track(Uri.parse(path + "sample2"),
                2, "Tobu", "Turn It Up ", albumName, year, 20000));
        trackList.add(new Track(Uri.parse(path + "sample3"),
                3, "Syn Cole", "Gizmo", albumName, year, 17000));

        return  trackList;
    }

    public static Album getSampleAlbum() {
        return new Album("Sonludilo",  albumName, "Various Artists", year, String.valueOf(getSampleTracks().size()));
    }
}
