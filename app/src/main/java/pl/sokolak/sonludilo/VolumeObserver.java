package pl.sokolak.sonludilo;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

import androidx.lifecycle.ViewModelProvider;

import pl.sokolak.sonludilo.ui.SharedViewModel;

public class VolumeObserver extends ContentObserver {
    private final AudioManager audioManager;
    private final SharedViewModel sharedViewModel;

    public VolumeObserver(Context context, SharedViewModel sharedViewModel, Handler handler) {
        super(handler);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.sharedViewModel = sharedViewModel;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false;
    }

    @Override
    public void onChange(boolean selfChange) {
        sharedViewModel.setCurrentVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }
}
