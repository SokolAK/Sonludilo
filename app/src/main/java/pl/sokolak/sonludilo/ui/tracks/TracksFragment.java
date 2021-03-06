package pl.sokolak.sonludilo.ui.tracks;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import pl.sokolak.sonludilo.ListTripleItemAdapter;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.Utils;
import pl.sokolak.sonludilo.ui.albums.AlbumsViewModel;
import pl.sokolak.sonludilo.ui.player.PlayerViewModel;

public class TracksFragment extends Fragment {
    private TracksViewModel tracksViewModel;
    private PlayerViewModel playerViewModel;
    private ListView trackListView;
    private View listButtons;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tracks, container, false);

        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
        tracksViewModel = new ViewModelProvider(requireActivity()).get(TracksViewModel.class);
        tracksViewModel.setContext(getContext());

        tracksViewModel.readTrackList(null, List.of("album", "track_no", "artist", "track_name"));
        tracksViewModel.setTrackListString();

        trackListView = root.findViewById(R.id.track_list);
        tracksViewModel.getTrackList().observe(getViewLifecycleOwner(), list -> {
                    tracksViewModel.setTrackListString();
                    setListAdapter();
                    checkCurrentItems(list);
                    setPosition();
                    toggleListButtons(list.size() > 0);
                }
        );

        setClickListener();
        configureSortButtons();

        TextView tipView = root.findViewById(R.id.tip);
        tipView.setSelected(true);

        return root;
    }

    private void setPosition() {
        List<Track> sharedTrackList = playerViewModel.getTracks();
        if (Utils.isNotEmpty(sharedTrackList)) {
            int id = tracksViewModel.getTrackList().getValue().indexOf(sharedTrackList.get(0));
            trackListView.setSelection(id);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
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
        List<Track> sharedTrackList = playerViewModel.getTracks();
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
            List<Track> allTracks = tracksViewModel.getTrackList().getValue();
            Track track = allTracks.get(position);
            if (!playerViewModel.isTrackOnList(track)) {
                playerViewModel.addTrack(track);
            } else {
                playerViewModel.removeTrack(track);
            }
            //NavHostFragment.findNavController(this).navigate(R.id.action_tracks_to_player);
        });
    }
}
