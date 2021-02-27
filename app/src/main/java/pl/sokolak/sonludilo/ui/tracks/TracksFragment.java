package pl.sokolak.sonludilo.ui.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        tracksViewModel = new ViewModelProvider(this, new TracksViewModelFactory(getContext())).get(TracksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tracks, container, false);

        trackListView = root.findViewById(R.id.track_list);
        tracksViewModel.getTrackList().observe(getViewLifecycleOwner(), list -> {
                    setListAdapter();
                    checkCurrentItems(list);
                    List<Track> sharedTrackList = sharedViewModel.getCurrentTrackList().getValue();
                    if (Utils.isNotEmpty(sharedTrackList)) {
                        int id = tracksViewModel.getTrackList().getValue().indexOf(sharedTrackList.get(0));
                        trackListView.setSelection(id);
                    }
                }
        );

        setClickListener();

        TextView tipView = root.findViewById(R.id.tip);
        tipView.setSelected(true);

        return root;
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
