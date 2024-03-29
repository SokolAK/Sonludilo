package pl.sokolak.sonludilo.tabs.albums;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.SamplesLoader;
import pl.sokolak.sonludilo.Utils;
import pl.sokolak.sonludilo.tabs.tracks.Track;
import pl.sokolak.sonludilo.tabs.tracks.TracksRepository;

public class AlbumsRepository {

    private final Context context;

    public AlbumsRepository(Context context) {
        this.context = context;
    }

    public List<Album> getAll(String selection, List<String> sort) {

        List<Album> albumsList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
//        final String album_id = MediaStore.Audio.Albums.ALBUM_ID;
        final String album_name = MediaStore.Audio.Albums.ALBUM;
        final String artist = MediaStore.Audio.Albums.ARTIST;
        final String firstYear = MediaStore.Audio.Albums.FIRST_YEAR;
        final String lastYear = MediaStore.Audio.Albums.LAST_YEAR;
        final String no_tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;

        String sortLine = sort.stream()
                .map(p -> {
                    switch (p) {
                        case "artist":
                            return artist;
                        case "album":
                            return album_name;
                        case "year":
                            return firstYear;
                        default:
                            return "";
                    }
                })
                .filter(p -> !p.isEmpty())
                .collect(Collectors.joining(", "));

        final String[] columns = {_id, album_name, artist, firstYear, lastYear, no_tracks};
        Cursor cursor = cr.query(uri, columns, selection, null, sortLine);

        while (cursor.moveToNext()) {
            String year = getYear(cursor.getInt(3), cursor.getInt(4));
            albumsList.add(new Album(cursor.getString(0),
                    cursor.getString(1),
                    getArtist(cursor.getString(0)),
                    year,
                    cursor.getString(5)
            ));
        }

        cursor.close();

        albumsList.add(SamplesLoader.getSampleAlbum());
        return albumsList;
    }

    private String getYear(int minYear, int maxYear) {
        String year;
        if (minYear == maxYear) {
            year = String.valueOf(maxYear);
        } else {
            year = minYear + "-" + maxYear;
        }
        if (year.equals("0"))
            year = "-";

        return year;
    }

    private String getArtist(String albumId) {
        TracksRepository tracksRepository = new TracksRepository(context);
        List<Track> tracks = tracksRepository.getAll("album_id = " + albumId, List.of(""));
        Set<String> artists = tracks.stream()
                .map(Track::getArtist)
                .collect(Collectors.toSet());

        if(artists.size() > 1) {
            return context.getString(R.string.various_artists);
        } else {
            return artists.iterator().next();
        }
    }
}
