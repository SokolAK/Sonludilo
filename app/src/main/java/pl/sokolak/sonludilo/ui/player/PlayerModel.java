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

    PlayerModel() {
        this.mediaPlayer = new MediaPlayer();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void play() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
        mediaPlayer = MediaPlayer.create(context, currentTrack.getUri());
    }
}
