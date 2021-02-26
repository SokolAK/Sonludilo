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
    private final MutableLiveData<Integer> currentVolume = new MutableLiveData<>();
    private final MutableLiveData<Track> currentTrack = new MutableLiveData<>(null);

    public void setCurrentTrackList(List<Track> currentTrackList) {
        if(currentTrackList != null) {
            this.currentTrackList.setValue(currentTrackList);
        }
    }

    public void addTrack(Track track) {
        if(!currentTrackList.getValue().contains(track)) {
            currentTrackList.getValue().add(track);
        }
    }

    public void removeTrack(Track track) {
        currentTrackList.getValue().remove(track);
        notifyObservers();
    }

    public void removeTrack(int id) {
        currentTrackList.getValue().remove(id);
        notifyObservers();
    }

    public void clearTrackList() {
        currentTrackList.getValue().clear();
        notifyObservers();
    }

    public int getTrackPosition(Track track) {
        return currentTrackList.getValue().indexOf(track);
    }

    public boolean isCurrentTrackPosition(int id) {
        if(currentTrack.getValue() != null) {
            return currentTrack.getValue().equals(currentTrackList.getValue().get(id));
        }
        return false;
    }

    public boolean checkIfTrackOnList(Track track) {
        return currentTrackList.getValue().contains(track);
    }

    public LiveData<List<Track>> getCurrentTrackList() {
        return currentTrackList;
    }

    public Track getTrack(int id) {
        if (id < currentTrackList.getValue().size()) {
            return currentTrackList.getValue().get(id);
        }
        return null;
    }

    public void setCurrentTrack(Track track) {
        this.currentTrack.setValue(track);
    }

    public LiveData<Track> getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentVolume(int currentVolume) {
        this.currentVolume.postValue(currentVolume);
    }

    public LiveData<Integer> getCurrentVolume() {
        return currentVolume;
    }

    private void notifyObservers() {
        currentTrackList.setValue(currentTrackList.getValue());
    }
}

