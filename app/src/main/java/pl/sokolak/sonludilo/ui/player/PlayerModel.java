package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import pl.sokolak.sonludilo.Utils;
import pl.sokolak.sonludilo.ui.tracks.Track;

public enum PlayerModel {
    INSTANCE;

    private WeakReference<Context> weakContext;
    private PlayerViewModel playerViewModel;
    private final MediaPlayer mediaPlayer;
    private boolean repeatEnabled = false;
    private Status status = Status.STOPPED;


    PlayerModel() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(l -> {
            if(status == Status.PLAYING)
                setStatus(Status.COMPLETED);
            //notifyViewModel();
        });
    }


    private void notifyViewModel() {
        playerViewModel.updateStatus(status);
        //playerViewModel.updateCurrentTrack(currentTrack);
    }

    public void setViewModel(PlayerViewModel playerViewModel) {
        this.playerViewModel = playerViewModel;
    }

    public void setContext(Context context) {
        weakContext = new WeakReference<>(context);
    }

    public void play() {
        if (playerViewModel.getCurrentTrack() != null) {
            mediaPlayer.start();
            setStatus(Status.PLAYING);
        }
    }

    public void pause() {
        if (playerViewModel.getCurrentTrack() != null) {
            mediaPlayer.pause();
            setStatus(Status.PAUSED);
        }
    }

    public void stop() {
        if (playerViewModel.getCurrentTrack() != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            setStatus(Status.STOPPED);
        }
    }

    public void setTime(int time) {
        mediaPlayer.seekTo(time);
    }

    public void setCurrentTrack(Track currentTrack) {
        mediaPlayer.reset();
        //mediaPlayer.release();
        if (currentTrack != null) {
            //Optional<Uri> uri = Optional.ofNullable(currentTrack.getUri());
            //uri.ifPresent(u -> mediaPlayer = MediaPlayer.create(context, u));
            //mediaPlayer = MediaPlayer.create(context,currentTrack.getUri());
            try {
                mediaPlayer.setDataSource(weakContext.get(), currentTrack.getUri());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mediaPlayer.setLooping(false);
        }
    }

    public Status getStatus() {
        return status;
    }

    private void setStatus(Status st) {
        status = st;
        notifyViewModel();
    }

    public int[] getTime() {
        int[] time = new int[]{0, 0};
        if (mediaPlayer != null && playerViewModel.getCurrentTrack() != null) {
            time[0] = mediaPlayer.getCurrentPosition();
            time[1] = mediaPlayer.getDuration() - time[0];
        }
        return time;
    }

    public boolean isRepeatEnabled() {
        return repeatEnabled;
    }

    public void setRepeatEnabled(boolean repeatEnabled) {
        this.repeatEnabled = repeatEnabled;
    }


    public enum Status {
        PLAYING, STOPPED, PAUSED, COMPLETED
    }
}
