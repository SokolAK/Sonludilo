package pl.sokolak.sonludilo.ui.player;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

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

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
        mediaPlayer = MediaPlayer.create(context, currentTrack.getUri());
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        PLAYING, STOPPED, PAUSED;
    }
}
