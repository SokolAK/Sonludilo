package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    //private final WeakReference<ImageView> weakGifView;
    private final WeakReference<View> weakRoot;
    private final PlayerModel playerModel;
    private Track currentTrack;
    //private final MutableLiveData<Integer> mCurrentTrackNumber = new MutableLiveData<>();

    public PlayerViewModel(Context context, View root) {
        weakContext = new WeakReference<>(context);
        this.weakRoot = new WeakReference<>(root);
        playerModel = PlayerModel.INSTANCE;
        playerModel.setContext(context);
        //mCurrentTrackNumber.setValue(0);
        updateGif();
        updateVolumeBar();

    }

    public void setCurrentTrack(int currentTrackNo, List<Track> trackList) {
        currentTrack = trackList.get(currentTrackNo);
    }

    public void trackListItemClicked(int currentTrackNo, List<Track> trackList) {
        //mCurrentTrackNumber.setValue(position);
        //Track currentTrack = trackList.get(currentTrackNo);

        playerModel.stop();
        currentTrack = trackList.get(currentTrackNo);
        playerModel.setCurrentTrack(currentTrack);
        playerModel.play();

        updateGif();
    }

//    public MutableLiveData<Integer> getmCurrentTrackNumber() {
//        return mCurrentTrackNumber;
//    }

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
            updateVolumeBar();
        }
    }

    public void bVolDownClicked() {
        if (weakContext != null) {
            AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            updateVolumeBar();
        }
    }

    private void updateVolumeBar() {
        if (weakContext != null) {
            AudioManager audioManager = (AudioManager) weakContext.get().getSystemService(Context.AUDIO_SERVICE);
            ProgressBar volumeBar = weakRoot.get().findViewById(R.id.volume_bar);
            volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    }

    public PlayerModel.Status getStatus() {
        return playerModel.getStatus();
    }

    private void updateGif() {
        switch (playerModel.getStatus()) {
            case PLAYING:
                startGif();
                break;
            case PAUSED:
                stopGif();
                break;
            case STOPPED:
                stopGif();
                break;
        }
    }

    private void startGif() {
        ImageView gifView = weakRoot.get().findViewById(R.id.image_view);
        GifDrawable gif = (GifDrawable) gifView.getDrawable();
        gif.start();
    }

    private void stopGif() {
        ImageView gifView = weakRoot.get().findViewById(R.id.image_view);
        GifDrawable gif = (GifDrawable) gifView.getDrawable();
        gif.stop();
    }
}
