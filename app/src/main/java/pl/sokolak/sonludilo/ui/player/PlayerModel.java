package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.Optional;

import pl.sokolak.sonludilo.ui.tracks.Track;

public enum PlayerModel {
    INSTANCE;

    private MediaPlayer mediaPlayer;
    private Track currentTrack;
    private boolean repeatEnabled = false;
    private Context context;
    private Status status = Status.STOPPED;

    PlayerModel() {
        this.mediaPlayer = new MediaPlayer();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void play() {
        if (currentTrack != null) {
            mediaPlayer.start();
            if (mediaPlayer.isPlaying()) {
                status = Status.PLAYING;
            }
        }
    }

    public void pause() {
        if (currentTrack != null) {
            mediaPlayer.pause();
            if (!mediaPlayer.isPlaying()) {
                status = Status.PAUSED;
            }
        }
    }

    public void stop() {
        if (currentTrack != null) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            if (!mediaPlayer.isPlaying()) {
                status = Status.STOPPED;
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
        if(currentTrack != null) {
            //Optional<Uri> uri = Optional.ofNullable(currentTrack.getUri());
            //uri.ifPresent(u -> mediaPlayer = MediaPlayer.create(context, u));
            //mediaPlayer = MediaPlayer.create(context,currentTrack.getUri());
            try {
                mediaPlayer.setDataSource(context,currentTrack.getUri());
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


    public int[] getTime() {
        int[] time = new int[]{0, 0};
        if (mediaPlayer != null && currentTrack != null) {
            time[0] = mediaPlayer.getCurrentPosition();
            time[1] = mediaPlayer.getDuration() - time[0];
//            if (time[1] <= 10) {
//                mediaPlayer.seekTo(0);
//                mediaPlayer.start();
//            }
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
        PLAYING, STOPPED, PAUSED;
    }
}
