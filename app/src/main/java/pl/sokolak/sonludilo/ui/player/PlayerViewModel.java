package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.media.AudioManager;
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
import java.util.Iterator;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.TrackList;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private WeakReference<Context> weakContext;
    private PlayerModel playerModel;
    private GifDrawable gif;
    private PlayerModel.Status prevStatus;
    private final MutableLiveData<PlayerModel.Status> status = new MutableLiveData<>(PlayerModel.Status.STOPPED);
    //private final MutableLiveData<Track> currentTrack = new MutableLiveData<>();
    //private final MutableLiveData<List<Track>> currentTrackList = new MutableLiveData<>(new ArrayList<>());
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
        setCurrentId(id);
        //playerModel.setCurrentTrack(getCurrentTrack());
        //seekBar.setMax(getCurrentTrack().getDuration());




        updateGif();
//        trackList.getValue().setCurrentTrack(track);
//        playerModel.stop();
//        playerModel.setCurrentTrack(track);
//        if (track != null) {
//            seekBar.setMax(track.getDuration());
//            playerModel.play();
//        }
//        updateGif();
    }


    public int[] getPlayerTime() {
        return playerModel.getTime();
    }

    public PlayerModel.Status getPlayerStatus() {
        return playerModel.getStatus();
    }

    public void updateStatus(PlayerModel.Status status) {
        this.status.setValue(status);

        if(status == PlayerModel.Status.COMPLETED) {
            if (!isRepeatEnabled()) {
                setNextTrack();
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
        updateTrackList(id);
        clickTrackListItem(id);
    }

    public void setPrevTrack() {
        int id = getCurrentId();
        id = Math.max(id - 1, 0);
        updateTrackList(id);
        clickTrackListItem(id);
    }

    private void setTracks(List<Track> tracks) {
        updateTrackList(tracks);
    }

    public void addTrack(Track track) {
        getTracks().add(track);
        updateTrackList();
    }

    public void removeTrack(Track track) {
        getTracks().remove(track);
        if (getTracks().size() > 0) {
            if (getCurrentTrack().equals(track)) {
                setCurrentId(0);
            }
        } else {
            setCurrentId(-1);
        }
        updateTrackList();
    }


    public void clearTrackList() {
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
        currentId = tracks.contains(getCurrentTrack()) ? currentId : 0;
        TrackList newTrackList = new TrackList(tracks, currentId);
        trackList.setValue(newTrackList);
        playerModel.setCurrentTrack(getCurrentTrack());
    }

    public void sortTrackList(Comparator<Track> comp) {
        getTracks().sort(comp);
        updateTrackList();
    }

    public boolean isTrackOnList(Track track) {
        return trackList.getValue().isTrackOnList(track);
    }


    // seekBar control
    //==============================================================================================
    public void adjustSeekBar(SeekBar seekBar) {
        if (getCurrentTrack()!= null) {
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
        switch (getPlayerStatus()) {
            case PLAYING:
                startGif();
                break;
            case PAUSED:
            case STOPPED:
            case COMPLETED:
                stopGif();
                break;
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
