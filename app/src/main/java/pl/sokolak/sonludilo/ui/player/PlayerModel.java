package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
import java.lang.ref.WeakReference;

import pl.sokolak.sonludilo.ui.tracks.Track;

public enum PlayerModel {
    INSTANCE;

    private final MediaPlayer mediaPlayer;
    private Track currentTrack;
    private boolean repeatEnabled = false;
    private WeakReference<Context> weakContext;
    private Status status = Status.STOPPED;
    private PlayerViewModel playerViewModel;

    PlayerModel() {
        this.mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(l -> {
            setStatus(Status.COMPLETED);
        });
    }

    private void notifyViewModel() {
        playerViewModel.updateStatus(status);
    }

    public void setViewModel(PlayerViewModel playerViewModel) {
        this.playerViewModel = playerViewModel;
    }

    public void setContext(Context context) {
        weakContext = new WeakReference<>(context);
    }

    public void play() {
        if (currentTrack != null) {
            mediaPlayer.start();
            if (mediaPlayer.isPlaying()) {
                setStatus(Status.PLAYING);
                notifyViewModel();
            }
        }
    }

    public void pause() {
        if (currentTrack != null) {
            mediaPlayer.pause();
            if (!mediaPlayer.isPlaying()) {
                setStatus(Status.PAUSED);
                notifyViewModel();
            }
        }
    }

    public void stop() {
        if (currentTrack != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            if (!mediaPlayer.isPlaying()) {
                setStatus(Status.STOPPED);
                notifyViewModel();
            }
        }
    }

    public void setTime(int time) {
        mediaPlayer.seekTo(time);
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
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
        if (mediaPlayer != null && currentTrack != null) {
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
