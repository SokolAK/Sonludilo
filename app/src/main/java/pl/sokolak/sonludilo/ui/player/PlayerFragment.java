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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rachitgoyal.segmented.SegmentedProgressBar;

import java.util.List;
import java.util.stream.Collectors;

import pl.sokolak.sonludilo.ListSingleItemAdapter;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.SeekBarListener;
import pl.sokolak.sonludilo.Utils;
import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private PlayerViewModel playerViewModel;
    private Handler timeHandler;
    private View root;
    private ListView trackListView;
    private Button bClear;
    private ImageButton bPlay;
    private ImageButton bPause;
    private ImageButton bStop;
    private ImageButton bVolUp;
    private ImageButton bVolDown;
    private ImageButton bPrev;
    private ImageButton bNext;
    private ImageButton bRepeat;
    private SeekBar seekBar;
    private View gifView;
    private SegmentedProgressBar volumeBar;
    private TextView tipView;
    private TextView remainingTime;
    private TextView elapsedTime;
    private Thread thread = Thread.currentThread();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_player, container, false);
        playerViewModel = new ViewModelProvider(this, new PlayerViewModelFactory(getContext(), root)).get(PlayerViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        initViews();

        configurePlayListener();
        configurePauseListener();
        configureStopListener();
        configureNextListener();
        configurePrevListener();
        configureVolUpListener();
        configureVolDownListener();
        configureRepeatListener();
        configureClearListener();
        configureGifListener();
        configureTrackListClickListener();
        configureTrackListLongClickListener();

        configureVolumeBarObserver();
        configureCurrentTrackListObserver();
        configureCurrentTrackObserver();
        configurePlayerStatusObserver();

        initTipView();
        //controlTimeUpdate(true);
        timeHandler = new Handler(Looper.getMainLooper());
        timeHandler.postDelayed(updateTime, 0);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener(playerViewModel));
        playerViewModel.initVolumeBarProgress(volumeBar);
        playerViewModel.initSeekBarProgress(sharedViewModel.getCurrentTrack().getValue(), seekBar);
        playerViewModel.updateGif();
        return root;
    }


    private void initViews() {
        trackListView = root.findViewById(R.id.track_list);
        bClear = root.findViewById(R.id.button_clear);
        bPlay = root.findViewById(R.id.button_play);
        bPause = root.findViewById(R.id.button_pause);
        bStop = root.findViewById(R.id.button_stop);
        bVolUp = root.findViewById(R.id.button_vol_up);
        bVolDown = root.findViewById(R.id.button_vol_down);
        bPrev = root.findViewById(R.id.button_prev);
        bNext = root.findViewById(R.id.button_next);
        bRepeat = root.findViewById(R.id.button_repeat);
        seekBar = root.findViewById(R.id.seek_bar);
        gifView = root.findViewById(R.id.tape_image);
        volumeBar = root.findViewById(R.id.volume_bar);
        bRepeat.setSelected(playerViewModel.isRepeatEnabled());
        tipView = root.findViewById(R.id.tip);
        remainingTime = root.findViewById(R.id.remaining_time);
        elapsedTime = root.findViewById(R.id.elapsed_time);
    }

    private void initTipView() {
        if (sharedViewModel.isTrackListNotEmpty()) {
            tipView.setVisibility(View.VISIBLE);
            tipView.setSelected(true);
        } else {
            tipView.setVisibility(View.INVISIBLE);
        }
    }

    private void configurePlayerStatusObserver() {
        playerViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == PlayerModel.Status.COMPLETED) {
                if (playerViewModel.isRepeatEnabled()) {
                    bStop.performClick();
                    bPlay.performClick();
                } else {
                    bNext.performClick();
                }
            }
        });
    }

    private void configureCurrentTrackListObserver() {
        sharedViewModel.getCurrentTrackList().observe(getViewLifecycleOwner(), list -> {
            trackListView.setAdapter(new ListSingleItemAdapter(
                    getContext(),
                    list.stream().map(Track::toMultiLineStringShort).collect(Collectors.toList())));
            isCurrentTrackOnList();
        });
    }

    private void configureCurrentTrackObserver() {
        sharedViewModel.getCurrentTrack().observe(getViewLifecycleOwner(), n -> {
            List<Track> currentTrackList = sharedViewModel.getCurrentTrackList().getValue();
            if (currentTrackList != null) {
                if (!isCurrentTrackOnList()) {
                    if (currentTrackList.size() > 0) {
                        trackListItemClick(0, false);
                    }
                    bStop.performClick();
                }
                if (currentTrackList.size() > 0) {
                    bClear.setVisibility(View.VISIBLE);
                } else {
                    bClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private boolean isCurrentTrackOnList() {
        List<Track> currentTrackList = sharedViewModel.getCurrentTrackList().getValue();
        Track currentTrack = sharedViewModel.getCurrentTrack().getValue();
        if (currentTrackList != null && currentTrack != null) {
            for (int i = 0; i < currentTrackList.size(); ++i) {
                if (currentTrackList.get(i).getUri().equals(currentTrack.getUri())) {
                    trackListView.setItemChecked(i, true);
                    int idSelected = Math.max(i - 2, 0);
                    trackListView.setSelection(idSelected);
                    return true;
                }
            }
        }
        return false;
    }

    private void configureTrackListClickListener() {
        trackListView.setOnItemClickListener((parent, view, position, id) -> {
            List<Track> trackList = sharedViewModel.getCurrentTrackList().getValue();
            if (sharedViewModel.isTrackListNotEmpty()) {
                if (position < trackList.size()) {
                    if (!sharedViewModel.isCurrentTrackPosition(position)) {
                        Track currentTrack = sharedViewModel.getTrack(position);
                        sharedViewModel.setCurrentTrack(currentTrack);
                        playerViewModel.trackListItemClicked(currentTrack, seekBar);
                        updateTime();
                    }
                }
            }
        });
    }

    private void configureTrackListLongClickListener() {
        trackListView.setOnItemLongClickListener((parent, view, position, id) -> {
            //int currentPosition = trackListView.getCheckedItemPosition();
            Track currentTrack = sharedViewModel.getCurrentTrack().getValue();
            sharedViewModel.removeTrack(position);

            if (sharedViewModel.isTrackListNotEmpty()) {
                int currentPosition = sharedViewModel.getTrackPosition(currentTrack);
                if (currentPosition >= 0) {
                    trackListItemClick(currentPosition, false);
                } else {
                    position = Math.min(position, trackListView.getCount() - 1);
                    boolean stop = !playerViewModel.isPlaying();
                    trackListItemClick(position, stop);
                }
            } else {
                bClear.performClick();
            }
            return true;
        });
    }

    private void configureVolumeBarObserver() {
        sharedViewModel.getCurrentVolume().observe(getViewLifecycleOwner(), v -> {
            volumeBar.setEnabledDivisions(playerViewModel.getVolumeSegments(v));
        });
    }

    private void trackListItemClick(int position, boolean stop) {
        if (position >= 0 && position < trackListView.getCount()) {
            trackListView.performItemClick(trackListView.getAdapter().getView(position, null, null),
                    position,
                    trackListView.getAdapter().getItemId(position));
            if (stop) {
                bStop.performClick();
            }
        }
    }

    private void configurePlayListener() {
        bPlay.setOnClickListener(l -> {
            playerViewModel.bPlayClicked();
        });
    }

    private void configurePauseListener() {
        bPause.setOnClickListener(l -> {
            playerViewModel.bPauseClicked();
        });
    }

    private void configureStopListener() {
        bStop.setOnClickListener(l -> {
            playerViewModel.bStopClicked();
        });
    }

    private void configureVolUpListener() {
        bVolUp.setOnClickListener(l -> {
                    playerViewModel.bVolUpClicked();
                    updateVolume();
                }
        );
    }

    private void configureVolDownListener() {
        bVolDown.setOnClickListener(l -> {
                    playerViewModel.bVolDownClicked();
                    updateVolume();
                }
        );
    }

    private void configureNextListener() {
        bNext.setOnClickListener(l -> {
                    if (trackListView.getCount() > 0) {
                        int newPosition = trackListView.getCheckedItemPosition() + 1;
                        if (newPosition < trackListView.getCount()) {
                            trackListItemClick(newPosition, !playerViewModel.isPlaying());
                        }
                    }
                }
        );
    }

    private void configurePrevListener() {
        bPrev.setOnClickListener(l -> {
                    List<Track> trackList = sharedViewModel.getCurrentTrackList().getValue();
                    if (trackList != null) {
                        int newPosition = trackListView.getCheckedItemPosition() - 1;
                        if (newPosition >= 0) {
                            trackListItemClick(newPosition, !playerViewModel.isPlaying());
                        }
                    }
                }
        );
    }

    private void configureRepeatListener() {
        bRepeat.setOnClickListener(v -> {
            playerViewModel.setRepeatEnabled(!playerViewModel.isRepeatEnabled());
            v.setSelected(playerViewModel.isRepeatEnabled());
        });
    }

    private void configureClearListener() {
        bClear.setOnClickListener(l -> {
            sharedViewModel.clearTrackList();
            sharedViewModel.setCurrentTrack(null);
            playerViewModel.trackListItemClicked(null, seekBar);
            initTipView();
            //bStop.performClick();
        });
    }

    private void configureGifListener() {
        gifView.setOnClickListener(v ->
        {
            if (playerViewModel.isPlaying()) {
                bPause.performClick();
            } else {
                bPlay.performClick();
            }
        });
    }

    private void updateVolume() {
        AudioManager audioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        sharedViewModel.setCurrentVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

//    private void controlTimeUpdate(boolean run) {
//        if (!run) {
//            if (timeHandler != null) {
//                timeHandler.removeCallbacks(updateTime);
//                timeHandler = null;
//            }
//        } else {
//            timeHandler = new Handler(Looper.getMainLooper());
//            timeHandler.postDelayed(updateTime, 0);
//        }
//    }

    private int[] updateTime() {
        int[] time = playerViewModel.getPlayerTime();
        SeekBar seekBar = root.findViewById(R.id.seek_bar);
        elapsedTime.setText(Utils.formatTime(time[0]));
        remainingTime.setText("-" + Utils.formatTime(time[1]));
        playerViewModel.setSeekBarProgress(seekBar, time[0]);
        return time;
    }

    private final Runnable updateTime = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {
            updateTime();
            timeHandler.postDelayed(this, 200);
        }
    };
}