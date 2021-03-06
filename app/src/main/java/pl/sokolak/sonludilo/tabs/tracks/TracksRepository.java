package pl.sokolak.sonludilo.tabs.tracks;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pl.sokolak.sonludilo.SamplesLoader;
import pl.sokolak.sonludilo.Utils;

public class TracksRepository {

    private final Context context;

    public TracksRepository(Context context) {
        this.context = context;
    }

    public List<Track> getAll(String selection, List<String> sort) {

        List<Track> trackList = new ArrayList<>();
        final String track_id = MediaStore.Audio.Media._ID;
        final String track_no = MediaStore.Audio.Media.TRACK;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String duration = MediaStore.Audio.Media.DURATION;
        final String album = MediaStore.Audio.Media.ALBUM;
        final String composer = MediaStore.Audio.Media.COMPOSER;
        final String year = MediaStore.Audio.Media.YEAR;
        final String path = MediaStore.Audio.Media.DATA;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        String sortLine = sort.stream()
                .map(p -> {
                    switch (p) {
                        case "track_no":
                            return track_no;
                        case "artist":
                            return artist;
                        case "album":
                            return album;
                        case "track":
                            return track_name;
                        default:
                            return "";
                    }
                })
                .filter(p -> !p.isEmpty())
                .collect(Collectors.joining(", "));

        final String[] columns = {track_id, track_no, artist, track_name, album, year, duration, path, composer};
        Cursor cursor = cr.query(uri, columns, selection, null, sortLine);

        while (cursor.moveToNext()) {
            Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getInt(0));
            trackList.add(new Track(contentUri,
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6)));
        }

        if(selection == null) {
            trackList.addAll(SamplesLoader.getSampleTracks());
        }

        cursor.close();

        return trackList;
    }


}
