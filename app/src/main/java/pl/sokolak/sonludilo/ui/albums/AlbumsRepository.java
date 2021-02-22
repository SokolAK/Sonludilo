package pl.sokolak.sonludilo.ui.albums;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;

public class AlbumsRepository {

    private final Context context;

    public AlbumsRepository(Context context) {
        this.context = context;
    }

    public List<Album> getAll() {

        List<Album> albumsList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
//        final String album_id = MediaStore.Audio.Albums.ALBUM_ID;
        final String album_name =MediaStore.Audio.Albums.ALBUM;
        final String artist = MediaStore.Audio.Albums.ARTIST;
        final String firstYear = MediaStore.Audio.Albums.FIRST_YEAR;
        final String lastYear = MediaStore.Audio.Albums.LAST_YEAR;
        final String no_tracks = MediaStore.Audio.Albums.NUMBER_OF_SONGS;
        final String[] columns = {_id, album_name, artist, firstYear, lastYear, no_tracks};
        Cursor cursor = cr.query(uri, columns, null,null, null);


        while (cursor.moveToNext()) {

//            System.out.println(cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
//            Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getInt(0));
            String year = getYear(cursor.getInt(3), cursor.getInt(4));
            albumsList.add(new Album(cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    year,
                    cursor.getString(5),
                    new ArrayList<>()));
        }

        cursor.close();

        return albumsList;
    }

    private String getYear(int minYear, int maxYear) {
        String year;
        if (minYear == maxYear) {
            year = String.valueOf(maxYear);
        } else {
            year = minYear + "-" + maxYear;
        }
        if(year.equals("0"))
            year = "-";

        return year;
    }

}
