package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.lifecycle.ViewModel;

import com.rachitgoyal.segmented.SegmentedProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    private final PlayerModel playerModel;
    private boolean isSeekBarProgressTouched = false;
    //private WeakReference<ImageView> gifView;
    private final GifDrawable gif;
    //private final MutableLiveData<Integer> mCurrentTrackNumber = new MutableLiveData<>();

    public PlayerViewModel(Context context, View root) {
        weakContext = new WeakReference<>(context);
        //private final WeakReference<ImageView> weakGifView;
        //WeakReference<View> weakRoot = new WeakReference<>(root);
        playerModel = PlayerModel.INSTANCE;
        playerModel.setContext(context);
        ImageView gifView = root.findViewById(R.id.tape_image);
        gif = (GifDrawable) gifView.getDrawable();
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
        Track currentTrack = trackList.get(currentTrackNo);
        playerModel.setCurrentTrack(currentTrack);
        playerModel.play();
    }

    public void trackListItemClicked(Track track, SeekBar seekBar) {
        //mCurrentTrackNumber.setValue(position);
        //Track currentTrack = trackList.get(currentTrackNo);
        playerModel.stop();
        playerModel.setCurrentTrack(track);
        if(track != null) {
            seekBar.setMax(track.getDuration());
            playerModel.play();
        }
        updateGif();
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

    public void updateGif() {
        switch (getPlayerStatus()) {
            case PLAYING:
                startGif();
                break;
            case PAUSED:
            case STOPPED:
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

    public void initSeekBarProgress(Track track, SeekBar seekBar) {
        if (track != null) {
            seekBar.setMax(track.getDuration());
        }
    }

    public void initVolumeBarProgress(SegmentedProgressBar volumeBar) {
        AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setEnabledDivisions(getVolumeSegments(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
    }
}
