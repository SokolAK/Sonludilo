package pl.sokolak.sonludilo.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.notifications.NotificationsViewModel;

import pl.sokolak.sonludilo.ui.tracks.TracksViewModel;
import pl.sokolak.sonludilo.ui.tracks.TracksViewModelFactory;

public class PlayerFragment extends Fragment {

    private PlayerViewModel playerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        playerViewModel = new ViewModelProvider(this, new PlayerViewModelFactory(getContext())).get(PlayerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        final TextView textView = root.findViewById(R.id.text_player);
        playerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), item -> {
            textView.setText(item.getTitle());
        });


        return root;
    }
}