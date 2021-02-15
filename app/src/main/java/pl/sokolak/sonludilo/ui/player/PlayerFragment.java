package pl.sokolak.sonludilo.ui.player;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.List;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private PlayerViewModel playerViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        //ImageView gifView = root.findViewById(R.id.image_view);

        playerViewModel = new ViewModelProvider(this, new PlayerViewModelFactory(getContext(), root)).get(PlayerViewModel.class);

        ListView trackListView = root.findViewById(R.id.track_list);
        ImageButton bPlay = root.findViewById(R.id.button_play);
        ImageButton bPause = root.findViewById(R.id.button_pause);
        ImageButton bStop = root.findViewById(R.id.button_stop);
        ImageButton bVolUp = root.findViewById(R.id.button_vol_up);
        ImageButton bVolDown = root.findViewById(R.id.button_vol_down);


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
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
                System.out.println(currentTrackList.get(i).getUri() + " / " +n);
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
        });

        bPlay.setOnClickListener(l -> playerViewModel.bPlayClicked());
        bPause.setOnClickListener(l -> playerViewModel.bPauseClicked());
        bStop.setOnClickListener(l -> playerViewModel.bStopClicked());
        bVolUp.setOnClickListener(l -> playerViewModel.bVolUpClicked());
        bVolDown.setOnClickListener(l -> playerViewModel.bVolDownClicked());


        return root;
    }

}