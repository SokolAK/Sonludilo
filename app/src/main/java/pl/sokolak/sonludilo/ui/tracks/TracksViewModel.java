package pl.sokolak.sonludilo.ui.tracks;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;

public class TracksViewModel extends ViewModel {

    private final MutableLiveData<List<List<String>>> mTracks = new MutableLiveData<>();
    private final List<Track> trackList;

    public TracksViewModel(Context context) {
        TracksRepository tracksRepository = new TracksRepository(context);
        trackList = tracksRepository.getAll(null);

        List<List<String>> list = new ArrayList<>();
        for (Track track : trackList) {
            //list.add(track.toMultiLineString(context));
            String artist = context.getString(R.string.artist) + ": " + track.getArtist();
            String title = context.getString(R.string.title) + ": " + track.getTitle();
            String album = context.getString(R.string.album) + ": " + track.getAlbum() +
                    (track.getYear().isEmpty() ? "" : " [" + track.getYear() + "]");
            list.add(List.of(artist, title, album));
        }

        mTracks.setValue(list);
    }

    public LiveData<List<List<String>>> getList() {
        return mTracks;
    }

    public List<Track> getTrackList() {
        return trackList;
    }
}
