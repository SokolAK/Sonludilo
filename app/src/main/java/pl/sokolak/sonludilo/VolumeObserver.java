package pl.sokolak.sonludilo;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

import androidx.lifecycle.ViewModelProvider;

import pl.sokolak.sonludilo.ui.player.PlayerViewModel;


public class VolumeObserver extends ContentObserver {
    private final AudioManager audioManager;
    private final PlayerViewModel playerViewModel;

    public VolumeObserver(Context context, PlayerViewModel playerViewModel, Handler handler) {
        super(handler);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.playerViewModel = playerViewModel;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false;
    }

    @Override
    public void onChange(boolean selfChange) {
        playerViewModel.setCurrentVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }
}
