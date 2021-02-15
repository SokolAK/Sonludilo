package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.albums.Album;
import pl.sokolak.sonludilo.ui.albums.AlbumsRepository;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    private final ImageView gifView;
    private final PlayerModel playerModel;
    private final MutableLiveData<Integer> mCurrentTrackNumber = new MutableLiveData<>();

    public PlayerViewModel(Context context, ImageView gifView) {
        weakContext = new WeakReference<>(context);
        this.gifView = gifView;
        playerModel = PlayerModel.INSTANCE;
        playerModel.setContext(context);
        mCurrentTrackNumber.setValue(0);
        updateGif();
    }

    public void trackListItemClicked(int position, List<Track> trackList) {
        mCurrentTrackNumber.setValue(position);
        Track currentTrack = trackList.get(position);

        playerModel.stop();
        playerModel.setCurrentTrack(currentTrack);
        playerModel.play();

        updateGif();
    }

    public MutableLiveData<Integer> getmCurrentTrackNumber() {
        return mCurrentTrackNumber;
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
        GifDrawable gif = (GifDrawable) gifView.getDrawable();
        gif.start();
    }

    private void stopGif() {
        GifDrawable gif = (GifDrawable) gifView.getDrawable();
        gif.stop();
    }
}
