package pl.sokolak.sonludilo.ui;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pl.sokolak.sonludilo.ui.tracks.Track;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Track> selected = new MutableLiveData<>();

    public void select(Track track) {
        selected.setValue(track);
    }

    public LiveData<Track> getSelected() {
        return selected;
    }
}

