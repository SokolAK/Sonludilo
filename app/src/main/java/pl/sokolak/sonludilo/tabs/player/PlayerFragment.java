package pl.sokolak.sonludilo.tabs.player;

import android.annotation.SuppressLint;
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

import java.util.Comparator;
import java.util.stream.Collectors;

import pl.sokolak.sonludilo.adapters.ListSingleItemAdapter;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.observers.SeekBarListener;
import pl.sokolak.sonludilo.Utils;
import pl.sokolak.sonludilo.tabs.tracks.Track;

public class PlayerFragment extends Fragment {
    private PlayerViewModel playerViewModel;
    private Handler timeHandler;
    private View root;
    private ListView trackListView;
    private Button bClear;
    private ImageButton bPlay, bPause, bStop, bVolUp, bVolDown, bPrev, bNext, bRepeat;
    private SeekBar seekBar;
    private View gifView;
    private SegmentedProgressBar volumeBar;
    private TextView tipView;
    private TextView remainingTime;
    private TextView elapsedTime;
    private View listButtons;
    private Button bNumber, bArtist, bTitle, bAlbum;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_player, container, false);
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
        playerViewModel.init(getContext(), root);
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
        configureListButtonsListeners();

        configureVolumeBarObserver();
        configureCurrentTrackListObserver();

        initTipView();
        //controlTimeUpdate(true);
        playerViewModel.adjustSeekBar(seekBar);
        timeHandler = new Handler(Looper.getMainLooper());
        timeHandler.postDelayed(updateTime, 0);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener(playerViewModel));
        playerViewModel.initVolumeBarProgress(volumeBar);
        playerViewModel.updateGif();

        return root;
    }

    private void configureListButtonsListeners() {
        bNumber.setOnClickListener(l -> {
            sortTrackList((o1, o2) -> o1.getNo() - o2.getNo());
        });
        bArtist.setOnClickListener(l -> {
            sortTrackList((o1, o2) -> o1.getArtist().compareTo(o2.getArtist()));
        });
        bTitle.setOnClickListener(l -> {
            sortTrackList((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
        });
        bAlbum.setOnClickListener(l -> {
            sortTrackList((o1, o2) -> o1.getAlbum().compareTo(o2.getAlbum()));
        });
    }

    private void sortTrackList(Comparator<Track> comparator) {
        playerViewModel.sortTrackList(comparator);
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
        listButtons = root.findViewById(R.id.list_buttons);
        bNumber = root.findViewById(R.id.button_no);
        bArtist = root.findViewById(R.id.button_artist);
        bTitle = root.findViewById(R.id.button_track);
        bAlbum = root.findViewById(R.id.button_album);
    }

    private void initTipView() {
        if (Utils.isNotEmpty(playerViewModel.getTracks())) {
            tipView.setVisibility(View.VISIBLE);
            tipView.setSelected(true);
        } else {
            tipView.setVisibility(View.INVISIBLE);
        }
    }

    private void configureCurrentTrackListObserver() {
        playerViewModel.getTrackList().observe(getViewLifecycleOwner(), list -> {
            trackListView.setAdapter(new ListSingleItemAdapter(
                    getContext(),
                    list.getTracks().stream().map(Track::toMultiLineStringShort).collect(Collectors.toList())));
            toggleListButtons(Utils.isNotEmpty(list.getTracks()));

            trackListView.setItemChecked(list.getCurrentId(), true);
            playerViewModel.adjustSeekBar(seekBar);
            focusOnCurrentTrack();
        });
    }

//    private void clickTrackListItem(int position) {
//        if (position >= 0 && position < trackListView.getCount()) {
//            trackListView.performItemClick(trackListView.getAdapter().getView(position, null, null),
//                    position,
//                    trackListView.getAdapter().getItemId(position));
//        }
//        //bStop.performClick();
//    }


    private void toggleListButtons(boolean flag) {
        if (flag) {
            listButtons.setVisibility(View.VISIBLE);
        } else {
            listButtons.setVisibility(View.INVISIBLE);
        }
    }


    private void configureTrackListClickListener() {
        trackListView.setOnItemClickListener((parent, view, position, id) -> {
            playerViewModel.clickTrackListItem(position);
            focusOnCurrentTrack();
        });
    }

    private void configureTrackListLongClickListener() {
        trackListView.setOnItemLongClickListener((parent, view, position, id) -> {
            playerViewModel.removeTrack(position);
            focusOnCurrentTrack();
            return true;
        });
    }

    private void focusOnCurrentTrack() {
        int idSelected = Math.max(playerViewModel.getCurrentId() - 2, 0);
        trackListView.setSelection(idSelected);
    }


    private void configureVolumeBarObserver() {
        playerViewModel.getCurrentVolume().observe(getViewLifecycleOwner(), v -> {
            volumeBar.setEnabledDivisions(playerViewModel.getVolumeSegments(v));
        });
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
                }
        );
    }

    private void configureVolDownListener() {
        bVolDown.setOnClickListener(l -> {
                    playerViewModel.bVolDownClicked();
                }
        );
    }

    private void configureNextListener() {
        bNext.setOnClickListener(l ->
                playerViewModel.setNextTrack()
        );
    }

    private void configurePrevListener() {
        bPrev.setOnClickListener(l ->
                playerViewModel.setPrevTrack()
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
            playerViewModel.clearTrackList();
            //playerViewModel.clickTrackListItem(-1);
            initTipView();
            //bStop.performClick();
        });
    }

    private void configureGifListener() {
        gifView.setOnClickListener(v ->
                playerViewModel.gifClicked());
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

    private final Runnable updateTime = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {
            updateTime();
            timeHandler.postDelayed(this, 500);
        }
    };

    private void updateTime() {
        int[] time = playerViewModel.getPlayerTime();
        System.out.println(time[0] + " " + time[1]);
        SeekBar seekBar = root.findViewById(R.id.seek_bar);
        elapsedTime.setText(Utils.formatTime(time[0]));
        remainingTime.setText("-" + Utils.formatTime(time[1]));
        playerViewModel.setSeekBarProgress(seekBar, time[0]);
        if (time[1] <= 100 && time[0] > 0) {
            if (playerViewModel.getPlayerStatus() != PlayerModel.Status.COMPLETED)
                playerViewModel.updateStatus(PlayerModel.Status.COMPLETED);
            //notifyViewModel();
        }
    }
}