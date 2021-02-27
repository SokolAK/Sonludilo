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
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    private final PlayerModel playerModel;
    private final boolean isSeekBarProgressTouched = false;
    private final GifDrawable gif;
    private PlayerModel.Status prevStatus;
    private final MutableLiveData<PlayerModel.Status> status = new MutableLiveData<>(PlayerModel.Status.STOPPED);

    public PlayerViewModel(Context context, View root) {
        weakContext = new WeakReference<>(context);
        playerModel = PlayerModel.INSTANCE;
        playerModel.setContext(context);
        playerModel.setViewModel(this);
        ImageView gifView = root.findViewById(R.id.tape_image);
        gif = (GifDrawable) gifView.getDrawable();
    }

    public void trackListItemClicked(Track track, SeekBar seekBar) {
        playerModel.stop();
        playerModel.setCurrentTrack(track);
        if (track != null) {
            seekBar.setMax(track.getDuration());
            playerModel.play();
        }
        updateGif();
    }

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

    public int[] getPlayerTime() {
        return playerModel.getTime();
    }

    public PlayerModel.Status getPlayerStatus() {
        return playerModel.getStatus();
    }

    public void updateStatus(PlayerModel.Status status) {
        this.status.setValue(status);
        updateGif();
    }

    public LiveData<PlayerModel.Status> getStatus() {
        return status;
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


    public void initSeekBarProgress(Track track, SeekBar seekBar) {
        if (track != null) {
            seekBar.setMax(track.getDuration());
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

    private void startGif() {
        gif.start();
    }

    private void stopGif() {
        gif.stop();
    }
}
