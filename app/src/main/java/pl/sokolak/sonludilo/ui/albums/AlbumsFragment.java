package pl.sokolak.sonludilo.ui.albums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import pl.sokolak.sonludilo.ListTripleItemAdapter;
import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;

public class AlbumsFragment extends Fragment {
    private AlbumsViewModel albumsViewModel;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        albumsViewModel = new ViewModelProvider(this, new AlbumsViewModelFactory(getContext())).get(AlbumsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_albums, container, false);

        ListView albumsList = root.findViewById(R.id.albums_list);

        albumsViewModel.getList().observe(getViewLifecycleOwner(), list -> albumsList.setAdapter(new ListTripleItemAdapter(
                getContext(),
                list))
        );

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        albumsList.setOnItemClickListener((parent, view, position, id) -> {
            sharedViewModel.setCurrentTrackList(albumsViewModel.getTrackListForAlbum(position));
            NavHostFragment.findNavController(this).navigate(R.id.action_albums_to_player);
        });

        albumsList.setOnItemLongClickListener((parent, view, position, id) -> {
            sharedViewModel.setCurrentTrackList(albumsViewModel.getTrackListForAlbum(position));
            NavHostFragment.findNavController(this).navigate(R.id.action_albums_to_tracks);
            return true;
        });

        TextView tipView = root.findViewById(R.id.tip);
        tipView.setSelected(true);

        return root;
    }
}
