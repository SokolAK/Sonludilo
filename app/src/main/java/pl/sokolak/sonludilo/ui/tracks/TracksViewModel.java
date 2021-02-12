package pl.sokolak.sonludilo.ui.tracks;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TracksViewModel extends ViewModel {

    private final MutableLiveData<List<String>> mTracks = new MutableLiveData<>();
    private List<Track> trackList;

    public TracksViewModel(Context context) {
        TracksRepository tracksRepository = new TracksRepository(context);
        //List<String> tracks = tracksRepository.getAll();
        trackList = tracksRepository.getAll();

        List<String> list = new ArrayList<>();
        for(Track track : trackList) {
            list.add(track.toMultiLineString(context));
        }

        mTracks.setValue(list);
    }

    public LiveData<List<String>> getList() {
        return mTracks;
    }

    public List<Track> getTrackList() {
        return trackList;
    }
}
