package pl.sokolak.sonludilo.ui;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Track>> currentTrackList = new MutableLiveData<>();
    private final MutableLiveData<Uri> currentTrackUri = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentVolume = new MutableLiveData<>();

    public void setCurrentTrackList(List<Track> currentTrackList) {
        this.currentTrackList.setValue(currentTrackList);
    }
    public LiveData<List<Track>> getCurrentTrackList() {
        return currentTrackList;
    }

    public void setCurrentTrackUri(Uri currentTrackUri) {
        this.currentTrackUri.setValue(currentTrackUri);
    }
    public LiveData<Uri> getCurrentTrackUri() {
        return currentTrackUri;
    }

    public void setCurrentVolume(int currentVolume) {
        this.currentVolume.postValue(currentVolume);
    }
    public LiveData<Integer> getCurrentVolume() {
        return currentVolume;
    }
}

