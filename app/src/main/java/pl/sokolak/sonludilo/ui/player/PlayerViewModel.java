package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import android.widget.SeekBar;

import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    //private final WeakReference<ImageView> weakGifView;
    private final WeakReference<View> weakRoot;
    private final PlayerModel playerModel;
    private Track currentTrack;
    private boolean isSeekBarProgressTouched = false;
    //private final MutableLiveData<Integer> mCurrentTrackNumber = new MutableLiveData<>();

    public PlayerViewModel(Context context, View root) {
        weakContext = new WeakReference<>(context);
        this.weakRoot = new WeakReference<>(root);
        playerModel = PlayerModel.INSTANCE;
        playerModel.setContext(context);
        //sharedViewModel = new ViewModelProvider(context).get(SharedViewModel.class);
        //mCurrentTrackNumber.setValue(0);
        //controlTimeUpdate(true);
    }

//    public void setCurrentTrack(int currentTrackNo, List<Track> trackList) {
//        currentTrack = trackList.get(currentTrackNo);
//    }

    public void trackListItemClicked(int currentTrackNo, List<Track> trackList) {
        //mCurrentTrackNumber.setValue(position);
        //Track currentTrack = trackList.get(currentTrackNo);

        playerModel.stop();
        currentTrack = trackList.get(currentTrackNo);
        playerModel.setCurrentTrack(currentTrack);
        playerModel.play();
    }

//    public MutableLiveData<Integer> getmCurrentTrackNumber() {
//        return mCurrentTrackNumber;
//    }

    public PlayerModel.Status getPlayerStatus() {
        return playerModel.getStatus();
    }

    public boolean isRepeatEnabled() {
        return playerModel.isRepeatEnabled();
    }

    public void setRepeatEnabled(boolean state) {
        playerModel.setRepeatEnabled(state);
    }

    public int[] getPlayerTime() {
        return playerModel.getTime();
    }

    public void bPlayClicked() {
        playerModel.play();
    }

    public void bPauseClicked() {
        playerModel.pause();
    }

    public void bStopClicked() {
        playerModel.stop();
    }

    public void bVolUpClicked() {
        if (weakContext != null) {
            AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public void bVolDownClicked() {
        if (weakContext != null) {
            AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public void seekBarChanged(int time) {
        playerModel.setTime(time);
    }

    public void setSeekBarProgress(SeekBar seekBar, int time) {
        if(!isSeekBarProgressTouched) {
            seekBar.setProgress(time);
        }
    }

    public void releaseSeekBarProgress() {
        isSeekBarProgressTouched = false;
    }

    public void touchSeekBarProgress() {
        isSeekBarProgressTouched = true;
    }

    public List<Integer> getVolumeSegments(int volume) {
        List<Integer> volumeSegments = new ArrayList<>();
        for (int i = 0; i < volume; ++i) {
            volumeSegments.add(i);
        }
        return volumeSegments;
    }
}
