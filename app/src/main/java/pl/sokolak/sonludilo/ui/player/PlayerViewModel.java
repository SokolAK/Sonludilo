package pl.sokolak.sonludilo.ui.player;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.ui.albums.Album;
import pl.sokolak.sonludilo.ui.albums.AlbumsRepository;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerViewModel extends ViewModel {

    private Context context;
    private PlayerModel playerModel;
    private MutableLiveData<Integer> mCurrentTrackNumber = new MutableLiveData<>();

    public PlayerViewModel(Context context) {
        this.context = context;
        playerModel = PlayerModel.INSTANCE;
        playerModel.setContext(context);
        mCurrentTrackNumber.setValue(0);
    }

    public void trackListItemClicked(int position, List<Track> trackList) {
        mCurrentTrackNumber.setValue(position);
        Track currentTrack = trackList.get(position);

        playerModel.stop();
        playerModel.setCurrentTrack(currentTrack);
        playerModel.play();
    }

    public MutableLiveData<Integer> getmCurrentTrackNumber() {
        return mCurrentTrackNumber;
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
}
