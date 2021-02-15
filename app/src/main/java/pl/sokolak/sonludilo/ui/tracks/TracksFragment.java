package pl.sokolak.sonludilo.ui.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;

public class TracksFragment extends Fragment {
    private TracksViewModel tracksViewModel;
    private SharedViewModel sharedModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tracksViewModel = new ViewModelProvider(this, new TracksViewModelFactory(getContext())).get(TracksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tracks, container, false);

        ListView trackList = root.findViewById(R.id.track_list);

        tracksViewModel.getList().observe(getViewLifecycleOwner(), list -> trackList.setAdapter(new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                list))
        );

        sharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        trackList.setOnItemClickListener((parent, view, position, id) -> {
            sharedModel.setCurrentTrackList(List.of(tracksViewModel.getTrackList().get(position)));
            sharedModel.setCurrentTrackNo(0);
            NavHostFragment.findNavController(this).navigate(R.id.action_tracks_to_player);
        });

        return root;
    }
}
