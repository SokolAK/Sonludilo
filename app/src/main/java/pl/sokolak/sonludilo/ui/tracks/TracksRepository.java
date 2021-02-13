package pl.sokolak.sonludilo.ui.tracks;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class TracksRepository {

    private final Context context;

    public TracksRepository(Context context) {
        this.context = context;
    }

    public List<Track> getAll() {

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
        final String[] columns = {track_id, track_no, artist, track_name, album, duration, path, year, composer};
        Cursor cursor = cr.query(uri, columns, null, null, null);

        while (cursor.moveToNext()) {
//            StringBuilder stringBuilder = new StringBuilder();
//
//            stringBuilder.append(addStringItem(context.getString(R.string.artist), cursor.getString(2)));
//            stringBuilder.append(addStringItem(context.getString(R.string.title), cursor.getString(3)));
//            stringBuilder.append(addStringItem(context.getString(R.string.album), cursor.getString(4)));
//            stringBuilder.append(addStringItem(context.getString(R.string.year), cursor.getString(7)));
//
//            list.add(stringBuilder.toString());
            //System.out.println(cursor.getString(0) + " --- " + cursor.getString(6));

            Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getInt(0));
            trackList.add(new Track(contentUri,
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(7)));
        }

        //Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 187537);
        //MediaPlayer mediaPlayer = MediaPlayer.create(context, contentUri);
        //mediaPlayer.start();

        cursor.close();

        return trackList;
    }


}
