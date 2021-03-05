package pl.sokolak.sonludilo;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;


public class TrackList {
    private List<Track> tracks = new ArrayList<>();
    private int currentId = -1;

    public TrackList() {
    }

    public TrackList(List<Track> tracks, int currentId) {
        this.tracks = tracks;
        this.currentId = currentId;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Track getCurrentTrack() {
        return getTrack(currentId);
    }


    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public Track getTrack(int id) {
        if(id < 0 || id >= tracks.size()) {
            return null;
        } else {
            return tracks.get(id);
        }
    }

    public boolean isTrackOnList(Track track) {
        return tracks.contains(track);
    }

    public void setCurrentTrack(Track track) {
        currentId = tracks.indexOf(track);
    }
}
