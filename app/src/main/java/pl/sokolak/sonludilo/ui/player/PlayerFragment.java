package pl.sokolak.sonludilo.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private PlayerViewModel playerViewModel;
    private Handler timeHandler;
    private View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_player, container, false);
        //ImageView gifView = root.findViewById(R.id.image_view);

        playerViewModel = new ViewModelProvider(this, new PlayerViewModelFactory(getContext(), root)).get(PlayerViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ListView trackListView = root.findViewById(R.id.track_list);
        ImageButton bPlay = root.findViewById(R.id.button_play);
        ImageButton bPause = root.findViewById(R.id.button_pause);
        ImageButton bStop = root.findViewById(R.id.button_stop);
        ImageButton bVolUp = root.findViewById(R.id.button_vol_up);
        ImageButton bVolDown = root.findViewById(R.id.button_vol_down);


        sharedViewModel.getCurrentTrackList().observe(getViewLifecycleOwner(), item -> {
            trackListView.setAdapter(new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_activated_1,
                    item));
        });

        sharedViewModel.getCurrentTrackUri().observe(getViewLifecycleOwner(), n -> {
            //trackListView.setItemChecked(n, true);
            List<Track> currentTrackList = sharedViewModel.getCurrentTrackList().getValue();
            boolean isOnList = false;
            for (int i = 0; i < currentTrackList.size(); ++i) {
                System.out.println(currentTrackList.get(i).getUri() + " / " + n);
                if (currentTrackList.get(i).getUri().equals(n)) {
                    trackListView.setItemChecked(i, true);
                    isOnList = true;
                    break;
                }
            }
            if (!isOnList) {
                if (currentTrackList.size() > 0) {
                    //trackListView.setItemChecked(0, true);
                    trackListView.performItemClick(trackListView.getAdapter().getView(0, null, null),
                            0,
                            trackListView.getAdapter().getItemId(0));
                }
                bStop.performClick();
            }
        });

        trackListView.setOnItemClickListener((parent, view, position, id) -> {
            playerViewModel.trackListItemClicked(position, sharedViewModel.getCurrentTrackList().getValue());
            //sharedViewModel.setCurrentTrackNo(position);
            Track currentTrack = sharedViewModel.getCurrentTrackList().getValue().get(position);
            sharedViewModel.setCurrentTrackUri(currentTrack.getUri());
            updateGif();
        });

        bPlay.setOnClickListener(l -> {
            playerViewModel.bPlayClicked();
            updateGif();
        });
        bPause.setOnClickListener(l -> {
            playerViewModel.bPauseClicked();
            updateGif();
        });
        bStop.setOnClickListener(l -> {
            playerViewModel.bStopClicked();
            updateGif();
        });
        bVolUp.setOnClickListener(l -> {
                    playerViewModel.bVolUpClicked();
                    updateVolume();
                }
        );
        bVolDown.setOnClickListener(l -> {
                    playerViewModel.bVolDownClicked();
                    updateVolume();
                }
        );


        ProgressBar volumeBar = root.findViewById(R.id.volume_bar);
        sharedViewModel.getCurrentVolume().observe(getViewLifecycleOwner(), volumeBar::setProgress);
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        updateGif();
        controlTimeUpdate(true);
        return root;
    }


    private void updateGif() {
        switch (playerViewModel.getPlayerStatus()) {
            case PLAYING:
                startGif();
                break;
            case PAUSED:
                stopGif();
                break;
            case STOPPED:
                stopGif();
                break;
        }
    }

    private void startGif() {
        ImageView gifView = root.findViewById(R.id.image_view);
        GifDrawable gif = (GifDrawable) gifView.getDrawable();
        gif.start();
    }

    private void stopGif() {
        ImageView gifView = root.findViewById(R.id.image_view);
        GifDrawable gif = (GifDrawable) gifView.getDrawable();
        gif.stop();
    }


    private void updateVolume() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        sharedViewModel.setCurrentVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }


    private void controlTimeUpdate(boolean run) {
        if(!run) {
            if (timeHandler != null) {
                timeHandler.removeCallbacks(updateTime);
                timeHandler = null;
            }
        }
        else {
            timeHandler = new Handler(Looper.getMainLooper());
            timeHandler.postDelayed(updateTime, 0);
        }
    }

    private final Runnable updateTime = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {
            int[] time = playerViewModel.getPlayerTime();
            TextView remainingTime = root.findViewById(R.id.remaining_time);
            TextView elapsedTime = root.findViewById(R.id.elapsed_time);
            remainingTime.setText(formatTime(time[0]));
            elapsedTime.setText("-" + formatTime(time[1]));
            timeHandler.postDelayed(this, 200);
        }
    };

    @SuppressLint("DefaultLocale")
    private String formatTime(int time) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }
}