package pl.sokolak.sonludilo.ui;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Track>> currentTrackList = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentTrackNo = new MutableLiveData<>();
    private final MutableLiveData<Uri> currentTrackUri = new MutableLiveData<>();

    public void setCurrentTrackList(List<Track> currentTrackList) {
        this.currentTrackList.setValue(currentTrackList);
    }
    public LiveData<List<Track>> getCurrentTrackList() {
        return currentTrackList;
    }

    public void setCurrentTrackNo(int currentTrackNo) {
        this.currentTrackNo.setValue(currentTrackNo);
    }
    public LiveData<Integer> getCurrentTrackNo() {
        return currentTrackNo;
    }

    public void setCurrentTrackUri(Uri currentTrackUri) {
        this.currentTrackUri.setValue(currentTrackUri);
    }
    public LiveData<Uri> getCurrentTrackUri() {
        return currentTrackUri;
    }
}

