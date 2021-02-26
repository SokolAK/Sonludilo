package pl.sokolak.sonludilo.ui.tracks;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;

public class TracksViewModel extends ViewModel {
    private final MutableLiveData<List<List<String>>> trackListString = new MutableLiveData<>();
    private final MutableLiveData<List<Track>> trackList = new MutableLiveData<>();;

    public TracksViewModel(Context context) {
        TracksRepository tracksRepository = new TracksRepository(context);
        trackList.setValue(tracksRepository.getAll(null));

        List<List<String>> list = new ArrayList<>();
        for (Track track : trackList.getValue()) {
            //list.add(track.toMultiLineString(context));
            String artist = context.getString(R.string.artist) + ": " + track.getArtist();
            String title = context.getString(R.string.title) + ": " + track.getTitle();
            String album = context.getString(R.string.album) + ": " + track.getAlbum() +
                    (track.getYear().isEmpty() ? "" : " [" + track.getYear() + "]");
            list.add(List.of(artist, title, album));
        }

        trackListString.setValue(list);
    }

    public LiveData<List<List<String>>> getTrackListString() {
        return trackListString;
    }

    public LiveData<List<Track>> getTrackList() {
        return trackList;
    }
}
