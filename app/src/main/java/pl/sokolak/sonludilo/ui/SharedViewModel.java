package pl.sokolak.sonludilo.ui;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Track>> currentTrackList = new MutableLiveData<>(new ArrayList<>());
    //private final MutableLiveData<Uri> currentTrackUri = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentVolume = new MutableLiveData<>();
    private final MutableLiveData<Track> currentTrack = new MutableLiveData<>(new Track());


    public void setCurrentTrackList(List<Track> currentTrackList) {
        if(currentTrackList != null && currentTrackList.size() > 0) {
            this.currentTrackList.setValue(currentTrackList);
        }
    }

    public LiveData<List<Track>> getCurrentTrackList() {
        return currentTrackList;
    }

    public LiveData<Track> getTrack(int id) {
        if (currentTrackList.getValue() != null) {
            if (id < currentTrackList.getValue().size()) {
                Track track = currentTrackList.getValue().get(id);
                currentTrack.setValue(track);
            }
        }
        return currentTrack;
    }

//    public void setCurrentTrackUri(Uri currentTrackUri) {
//        this.currentTrackUri.setValue(currentTrackUri);
//    }
//    public LiveData<Uri> getCurrentTrackUri() {
//        return currentTrackUri;
//    }

    public void setCurrentVolume(int currentVolume) {
        this.currentVolume.setValue(currentVolume);
    }

    public LiveData<Integer> getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentTrack(Track track) {
        this.currentTrack.setValue(track);
    }

    public LiveData<Track> getCurrentTrack() {
        return currentTrack;
    }
}

