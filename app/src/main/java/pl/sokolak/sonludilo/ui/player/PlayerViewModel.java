package pl.sokolak.sonludilo.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.ref.WeakReference;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    //private final WeakReference<ImageView> weakGifView;
    private final WeakReference<View> weakRoot;
    private final PlayerModel playerModel;
    private Track currentTrack;
    private SharedViewModel sharedViewModel;
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
    }

//    public MutableLiveData<Integer> getmCurrentTrackNumber() {
//        return mCurrentTrackNumber;
//    }

    public PlayerModel.Status getPlayerStatus() {
        return playerModel.getStatus();
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

}
