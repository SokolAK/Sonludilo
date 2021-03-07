package pl.sokolak.sonludilo.tabs.tracks;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.Utils;

public class TracksViewModel extends ViewModel {
    private final MutableLiveData<List<List<String>>> trackListString = new MutableLiveData<>();
    private final MutableLiveData<List<Track>> trackList = new MutableLiveData<>();
    private WeakReference<Context> weakContext;

    public void setContext(Context context) {
        weakContext = new WeakReference<>(context);
    }

    public void readTrackList(String selection, List<String> sort) {
        Context context = weakContext.get();
        TracksRepository tracksRepository = new TracksRepository(context);
        trackList.setValue(tracksRepository.getAll(selection, sort));
    }

    public void setTrackListString() {
        List<List<String>> list = new ArrayList<>();
        if (Utils.isNotEmpty(trackList.getValue())) {
            Context context = weakContext.get();
            for (Track track : trackList.getValue()) {
                String artist = context.getString(R.string.artist) + ": " + track.getArtist();
                String title = context.getString(R.string.title) + ": " + track.getTitle();
                String album = context.getString(R.string.album) + ": " + track.getAlbum() +
                        (track.getYear().isEmpty() ? "" : " [" + track.getYear() + "]");
                list.add(List.of(artist, title, album));
            }
        }
        trackListString.setValue(list);
    }

    public LiveData<List<Track>> getTrackList() {
        return trackList;
    }

    public LiveData<List<List<String>>> getTrackListString() {
        return trackListString;
    }
}
