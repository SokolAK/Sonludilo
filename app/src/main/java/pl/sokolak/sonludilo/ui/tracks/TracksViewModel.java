package pl.sokolak.sonludilo.ui.tracks;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TracksViewModel extends ViewModel {

    private Context context;

    private MutableLiveData<List<String>> mTracks = new MutableLiveData<>();

    public TracksViewModel(Context context) {
        this.context = context;
        TracksRepository tracksRepository = new TracksRepository(context);
        List<String> tracks = tracksRepository.getAll();
        mTracks.setValue(tracks);
    }

    public LiveData<List<String>> getList() {
        return mTracks;
    }
}
