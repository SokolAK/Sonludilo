package pl.sokolak.sonludilo.tabs.albums;

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
import androidx.navigation.fragment.NavHostFragment;

import java.util.Arrays;
import java.util.Collections;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.adapters.ListTripleItemAdapter;
import pl.sokolak.sonludilo.tabs.player.PlayerViewModel;


public class AlbumsFragment extends Fragment {
    private AlbumsViewModel albumsViewModel;
    private PlayerViewModel playerViewModel;
    private ListView albumsListView;
    private View listButtons;
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        albumsViewModel = new ViewModelProvider(requireActivity()).get(AlbumsViewModel.class);
        albumsViewModel.setContext(getContext());
        albumsViewModel.readAlbumsList(null, Arrays.asList("artist", "album", "year"));
        albumsViewModel.setAlbumsListString();
        root = inflater.inflate(R.layout.fragment_albums, container, false);

        albumsListView = root.findViewById(R.id.albums_list);

        albumsViewModel.getAlbumsList().observe(getViewLifecycleOwner(), list -> {
                    albumsViewModel.setAlbumsListString();
                    albumsListView.setAdapter(new ListTripleItemAdapter(
                            getContext(),
                            albumsViewModel.getAlbumsListString().getValue()));
                    toggleListButtons(list.size() > 0);
                }
        );

        configureSortButtons();
        configureListListeners();

        TextView tipView = root.findViewById(R.id.tip);
        tipView.setSelected(true);

        return root;
    }

    private void configureListListeners() {
        playerViewModel = new ViewModelProvider(requireActivity()).get(PlayerViewModel.class);
        albumsListView.setOnItemClickListener((parent, view, position, id) -> {
            playerViewModel.updateTrackList(albumsViewModel.getTrackListForAlbum(position));
            NavHostFragment.findNavController(this).navigate(R.id.action_albums_to_player);
        });

        albumsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            playerViewModel.updateTrackList(albumsViewModel.getTrackListForAlbum(position));
            NavHostFragment.findNavController(this).navigate(R.id.action_albums_to_tracks);
            return true;
        });
    }

    private void configureSortButtons() {
        listButtons = root.findViewById(R.id.list_buttons);

        Button bArtist = root.findViewById(R.id.button_artist);
        bArtist.setOnClickListener(l -> {
            albumsViewModel.readAlbumsList(null, Collections.singletonList("artist"));
        });
        Button bAlbum = root.findViewById(R.id.button_album);
        bAlbum.setOnClickListener(l -> {
            albumsViewModel.readAlbumsList(null, Collections.singletonList("album"));
        });
        Button bYear = root.findViewById(R.id.button_year);
        bYear.setOnClickListener(l -> {
            albumsViewModel.readAlbumsList(null, Collections.singletonList("year"));
        });
    }

    private void toggleListButtons(boolean flag) {
        if (flag) {
            listButtons.setVisibility(View.VISIBLE);
        } else {
            listButtons.setVisibility(View.INVISIBLE);
        }
    }
}
