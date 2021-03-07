package pl.sokolak.sonludilo.tabs.player;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rachitgoyal.segmented.SegmentedProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.TrackList;
import pl.sokolak.sonludilo.tabs.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private WeakReference<Context> weakContext;
    private PlayerModel playerModel;
    private GifDrawable gif;
    private PlayerModel.Status prevStatus;
    private final MutableLiveData<PlayerModel.Status> status = new MutableLiveData<>(PlayerModel.Status.STOPPED);
    private final MutableLiveData<Integer> currentVolume = new MutableLiveData<>();
    private final MutableLiveData<TrackList> trackList = new MutableLiveData<>(new TrackList());

    public PlayerViewModel() {
    }

    public void init(Context context, View root) {
        weakContext = new WeakReference<>(context);
        playerModel = PlayerModel.INSTANCE;
        playerModel.setContext(context);
        playerModel.setViewModel(this);
        ImageView gifView = root.findViewById(R.id.tape_image);
        gif = (GifDrawable) gifView.getDrawable();
    }

//    public PlayerViewModel(Context context, View root) {
//        weakContext = new WeakReference<>(context);
//        playerModel = PlayerModel.INSTANCE;
//        playerModel.setContext(context);
//        playerModel.setViewModel(this);
//        ImageView gifView = root.findViewById(R.id.tape_image);
//        gif = (GifDrawable) gifView.getDrawable();
//    }

    public void clickTrackListItem(int id) {
        if (id != getCurrentId() && id >= 0) {
            updateTrackList(id);
            switch (status.getValue()) {
                case PLAYING:
                case COMPLETED:
                    playerModel.play();
                    break;
                case PAUSED:
                    playerModel.pause();
                    break;
                case STOPPED:
                    playerModel.stop();
                    break;
            }
        }
    }


    public int[] getPlayerTime() {
        return playerModel.getTime();
    }

    public PlayerModel.Status getPlayerStatus() {
        return playerModel.getStatus();
    }

    public void updateStatus(PlayerModel.Status status) {
        this.status.setValue(status);

        if (status == PlayerModel.Status.COMPLETED) {
            if (!isRepeatEnabled()) {
                setNextTrack();
            } else {
                playerModel.stop();
                playerModel.play();
            }
        }

        updateGif();
    }

    public boolean isPlaying() {
        return playerModel.getStatus() == PlayerModel.Status.PLAYING || playerModel.getStatus() == PlayerModel.Status.COMPLETED;
    }

    public boolean isRepeatEnabled() {
        return playerModel.isRepeatEnabled();
    }

    public void setRepeatEnabled(boolean state) {
        playerModel.setRepeatEnabled(state);
    }


    // buttons clicked
    //==============================================================================================
    public void bPlayClicked() {
        playerModel.play();
        updateGif();
    }

    public void bPauseClicked() {
        playerModel.pause();
        updateGif();
    }

    public void bStopClicked() {
        playerModel.stop();
        updateGif();
    }

    public void bVolUpClicked() {
        if (weakContext != null) {
            AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            setCurrentVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    }

    public void bVolDownClicked() {
        if (weakContext != null) {
            AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            setCurrentVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    }

    // trackList manipulation
    //==============================================================================================
    public LiveData<TrackList> getTrackList() {
        return trackList;
    }

    public Track getCurrentTrack() {
        return trackList.getValue().getCurrentTrack();
    }

    public int getCurrentId() {
        return trackList.getValue().getCurrentId();
    }

    private void setCurrentId(int id) {
        trackList.getValue().setCurrentId(id);
    }

    public List<Track> getTracks() {
        return trackList.getValue().getTracks();
    }

    public Track getTrack(int id) {
        return trackList.getValue().getTrack(id);
    }


    public void setNextTrack() {
        int id = getCurrentId();
        id = Math.min(id + 1, getTracks().size() - 1);
        clickTrackListItem(id);
        //updateTrackList();
    }

    public void setPrevTrack() {
        int id = getCurrentId();
        id = Math.max(id - 1, 0);
        //updateTrackList(id);
        clickTrackListItem(id);
    }

    private void setTracks(List<Track> tracks) {
        updateTrackList(tracks);
    }

    public void addTrack(Track track) {
        List<Track> newTracks = new ArrayList<>(getTracks());
        newTracks.add(track);
        updateTrackList(newTracks);
    }

    public void removeTrack(Track track) {
        List<Track> newTracks = new ArrayList<>(getTracks());
        newTracks.remove(track);

        Track currentTrack = getCurrentTrack();
        if (!currentTrack.equals(track)) {
            updateTrackList(newTracks, newTracks.indexOf(currentTrack));
        } else {
            int id = getTracks().indexOf(currentTrack);
            id = id >= newTracks.size() ? newTracks.size() - 1 : id;
            setTracks(newTracks);
            updateTrackList(newTracks, id);
        }
        if (getTracks().size() == 0) {
            clearTrackList();
        }
    }

    public void removeTrack(int id) {
        removeTrack(getTrack(id));
    }


    public void clearTrackList() {
        bStopClicked();
        getTracks().clear();
        updateTrackList();
    }

    private void updateTrackList() {
        updateTrackList(getTracks(), getCurrentId());
    }

    public void updateTrackList(List<Track> tracks) {
        updateTrackList(tracks, getCurrentId());
    }

    private void updateTrackList(int currentId) {
        updateTrackList(getTracks(), currentId);
    }

    private void updateTrackList(List<Track> tracks, int currentId) {
        currentId = determineCurrentId(tracks, currentId);
        TrackList newTrackList = new TrackList(tracks, currentId);
        boolean reload = shouldReload(newTrackList);
        trackList.setValue(newTrackList);
        if (reload) {
            reload();
        }
    }

    private int determineCurrentId(List<Track> tracks, int currentId) {
        if (tracks.contains(getCurrentTrack())) {
            if (currentId == getCurrentId()) {
                return tracks.indexOf(getCurrentTrack());
            } else {
                return currentId;
            }
        } else {
            return 0;
        }
    }

    private boolean shouldReload(TrackList newTrackList) {
        return getCurrentTrack() == null ||
                !getCurrentTrack().equals(newTrackList.getCurrentTrack()) ||
                getTracks().size() == 0;
    }

    private void reload() {
        playerModel.setCurrentTrack(getCurrentTrack());
        if (status.getValue() == PlayerModel.Status.PLAYING) {
            playerModel.play();
        }
    }

    public void sortTrackList(Comparator<Track> comp) {
        List<Track> newTracks = new ArrayList<>(getTracks());
        newTracks.sort(comp);
        int newId = newTracks.indexOf(getCurrentTrack());
        updateTrackList(newTracks, newId);
    }

    public boolean isTrackOnCurrentList(Track track) {
        return trackList.getValue().isTrackOnList(track);
    }


    // seekBar control
    //==============================================================================================
    public void adjustSeekBar(SeekBar seekBar) {
        if (getCurrentTrack() != null) {
            seekBar.setMax(getCurrentTrack().getDuration());
        }
    }

    public void seekBarChanged(int time) {
        playerModel.setTime(time);
    }

    public void setSeekBarProgress(SeekBar seekBar, int time) {
        seekBar.setProgress(time);
    }

    public void releaseSeekBarProgress() {
        switch (prevStatus) {
            case PLAYING:
                playerModel.play();
                break;
            case PAUSED:
                playerModel.pause();
                break;
            case STOPPED:
            case COMPLETED:
                playerModel.stop();
                break;
        }
        updateGif();
    }

    public void touchSeekBarProgress() {
        prevStatus = getPlayerStatus();
        playerModel.pause();
        updateGif();
    }


    // volume control
    //==============================================================================================
    public void setCurrentVolume(int currentVolume) {
        this.currentVolume.postValue(currentVolume);
    }

    public LiveData<Integer> getCurrentVolume() {
        return currentVolume;
    }

    public void initVolumeBarProgress(SegmentedProgressBar volumeBar) {
        AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setEnabledDivisions(getVolumeSegments(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
    }

    public List<Integer> getVolumeSegments(int volume) {
        List<Integer> volumeSegments = new ArrayList<>();
        for (int i = 0; i < volume; ++i) {
            volumeSegments.add(i);
        }
        return volumeSegments;
    }

    // gif control
    //==============================================================================================
    public void updateGif() {
        if (status.getValue() == PlayerModel.Status.PLAYING) {
            startGif();
        } else {
            stopGif();
        }
    }

    public void gifClicked() {
        if (isPlaying()) {
            bPauseClicked();
        } else {
            bPlayClicked();
        }
    }

    private void startGif() {
        gif.start();
    }

    private void stopGif() {
        gif.stop();
    }

}
