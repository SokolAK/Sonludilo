package pl.sokolak.sonludilo.ui.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import pl.sokolak.sonludilo.ListTripleItemAdapter;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.Utils;
import pl.sokolak.sonludilo.ui.SharedViewModel;

public class TracksFragment extends Fragment {
    private TracksViewModel tracksViewModel;
    private SharedViewModel sharedViewModel;
    private ListView trackListView;
    private View listButtons;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        tracksViewModel = new ViewModelProvider(this, new TracksViewModelFactory(getContext())).get(TracksViewModel.class);
        tracksViewModel.readTrackList(null, List.of("album", "track_no", "artist", "track_name"));
        tracksViewModel.setTrackListString();
        root = inflater.inflate(R.layout.fragment_tracks, container, false);

        trackListView = root.findViewById(R.id.track_list);
        tracksViewModel.getTrackList().observe(getViewLifecycleOwner(), list -> {
                    tracksViewModel.setTrackListString();
                    setListAdapter();
                    checkCurrentItems(list);
                    List<Track> sharedTrackList = sharedViewModel.getCurrentTrackList().getValue();
                    if (Utils.isNotEmpty(sharedTrackList)) {
                        int id = tracksViewModel.getTrackList().getValue().indexOf(sharedTrackList.get(0));
                        trackListView.setSelection(id);
                    }
                    toggleListButtons(list.size() > 0);
                }
        );

        setClickListener();
        configureSortButtons();

        TextView tipView = root.findViewById(R.id.tip);
        tipView.setSelected(true);

        return root;
    }

    private void configureSortButtons() {
        listButtons = root.findViewById(R.id.list_buttons);

        Button bArtist = root.findViewById(R.id.button_artist);
        bArtist.setOnClickListener(l -> {
            tracksViewModel.readTrackList(null, List.of("artist"));
        });
        Button bTitle = root.findViewById(R.id.button_track);
        bTitle.setOnClickListener(l -> {
            tracksViewModel.readTrackList(null, List.of("track"));
        });
        Button bAlbum = root.findViewById(R.id.button_album);
        bAlbum.setOnClickListener(l -> {
            tracksViewModel.readTrackList(null, List.of("album"));
        });
    }

    private void toggleListButtons(boolean flag) {
        if (flag) {
            listButtons.setVisibility(View.VISIBLE);
        } else {
            listButtons.setVisibility(View.INVISIBLE);
        }
    }

    private void checkCurrentItems(List<Track> list) {
        List<Track> sharedTrackList = sharedViewModel.getCurrentTrackList().getValue();
        IntStream.range(0, list.size())
                .forEach(i -> {
                            Track track = list.get(i);
                            boolean isOnList = sharedTrackList.contains(track);
                            trackListView.setItemChecked(i, isOnList);
                        }
                );
    }

    private void setListAdapter() {
        trackListView.setAdapter(new ListTripleItemAdapter(
                getContext(),
                tracksViewModel.getTrackListString().getValue()));
    }

    private void setClickListener() {
        trackListView.setOnItemClickListener((parent, view, position, id) -> {
            Track track = tracksViewModel.getTrackList().getValue().get(position);
            if (!sharedViewModel.checkIfTrackOnList(track)) {
                sharedViewModel.addTrack(track);
            } else {
                sharedViewModel.removeTrack(track);
            }
            //NavHostFragment.findNavController(this).navigate(R.id.action_tracks_to_player);
        });
    }
}
