package pl.sokolak.sonludilo.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import pl.sokolak.sonludilo.ui.tracks.Track;

public enum PlayerModel {
    INSTANCE;

    private MediaPlayer mediaPlayer;
    private Track currentTrack;
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
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, currentTrack.getUri());
    }

    public Status getStatus() {
        return status;
    }


    public int[] getTime() {
        int[] time = new int[]{0,0};
        if (mediaPlayer != null && currentTrack != null) {
            time[0] = mediaPlayer.getCurrentPosition();
            time[1] = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
            if (time[1] <= 10) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
        }
        return time;
    }



    public enum Status {
        PLAYING, STOPPED, PAUSED;
    }
}
