package pl.sokolak.sonludilo.ui.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rachitgoyal.segmented.SegmentedProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifDrawable;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.SeekBarListener;
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
        ImageButton bPrev = root.findViewById(R.id.button_prev);
        ImageButton bNext = root.findViewById(R.id.button_next);
        SeekBar seekBar = root.findViewById(R.id.seek_bar);


        sharedViewModel.getCurrentTrackList().observe(getViewLifecycleOwner(), item -> {
            trackListView.setAdapter(new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_activated_1,
                    item));
        });

        sharedViewModel.getCurrentTrack().observe(getViewLifecycleOwner(), n -> {
            //trackListView.setItemChecked(n, true);
            List<Track> currentTrackList = sharedViewModel.getCurrentTrackList().getValue();
            boolean isOnList = false;
            if (currentTrackList != null) {
                for (int i = 0; i < currentTrackList.size(); ++i) {
                    if (currentTrackList.get(i).getUri().equals(n.getUri())) {
                        trackListView.setItemChecked(i, true);

                        int idSelected = Math.max(i - 3, 0);
                        trackListView.setSelection(idSelected);
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
            }
        });

        trackListView.setOnItemClickListener((parent, view, position, id) -> {
            List<Track> trackList = sharedViewModel.getCurrentTrackList().getValue();
            if (trackList != null) {
                if (position < trackList.size()) {
                    playerViewModel.trackListItemClicked(position, trackList);

                    //sharedViewModel.setCurrentTrackNo(position);
                    //Track currentTrack = sharedViewModel.getCurrentTrackList().getValue().get(position);
                    Track currentTrack = sharedViewModel.getTrack(position).getValue();

                    //sharedViewModel.getCurrentTrackUri(currentTrack.getUri());
                    if (currentTrack != null) {
                        seekBar.setMax(currentTrack.getDuration());
                    }
                    updateGif();
                }
            }
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
        bNext.setOnClickListener(l -> {
                    List<Track> trackList = sharedViewModel.getCurrentTrackList().getValue();
                    if (trackListView.getCount() > 0) {
                        int newPosition = trackListView.getCheckedItemPosition() + 1;
                        if (newPosition >= trackListView.getCount()) {
                            newPosition = 0;
                        }
                        trackListView.performItemClick(trackListView.getAdapter().getView(newPosition, null, null),
                                newPosition,
                                trackListView.getAdapter().getItemId(newPosition));
                    }
                }
        );
        bPrev.setOnClickListener(l -> {
                    List<Track> trackList = sharedViewModel.getCurrentTrackList().getValue();
                    if (trackList != null) {
                        int newPosition = trackListView.getCheckedItemPosition() - 1;
                        if (newPosition >= 0) {
                            trackListView.performItemClick(trackListView.getAdapter().getView(newPosition, null, null),
                                    newPosition,
                                    trackListView.getAdapter().getItemId(newPosition));
                        }
                    }
                }
        );
        seekBar.setOnSeekBarChangeListener(new SeekBarListener(playerViewModel));


        //ProgressBar volumeBar = root.findViewById(R.id.volume_bar);
        //sharedViewModel.getCurrentVolume().observe(getViewLifecycleOwner(), volumeBar::setProgress);
        //AudioManager audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        //volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        SegmentedProgressBar volumeBar = root.findViewById(R.id.volume_bar);
        sharedViewModel.getCurrentVolume().observe(getViewLifecycleOwner(), v -> {
            volumeBar.setEnabledDivisions(playerViewModel.getVolumeSegments(v));
        });
        AudioManager audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        //volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setEnabledDivisions(playerViewModel.getVolumeSegments(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));

        updateGif();
        controlTimeUpdate(true);
        Track track = sharedViewModel.getCurrentTrack().getValue();
        if (track != null) {
            seekBar.setMax(sharedViewModel.getCurrentTrack().getValue().getDuration());
        }
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
        AudioManager audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        sharedViewModel.setCurrentVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    private void controlTimeUpdate(boolean run) {
        if (!run) {
            if (timeHandler != null) {
                timeHandler.removeCallbacks(updateTime);
                timeHandler = null;
            }
        } else {
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
            SeekBar seekBar = root.findViewById(R.id.seek_bar);

            elapsedTime.setText(formatTime(time[0]));
            remainingTime.setText("-" + formatTime(time[1]));
            playerViewModel.setSeekBarProgress(seekBar, time[0]);
            //seekBar.setProgress(time[0]);

            if (time[1] <= 10) {
//                ImageButton bPause = root.findViewById(R.id.button_pause);
//                bPause.performClick();
                //ImageButton bPause = root.findViewById(R.id.button_stop);
                //bPause.performClick();
                ImageButton bNext = root.findViewById(R.id.button_next);
                bNext.performClick();
            }

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