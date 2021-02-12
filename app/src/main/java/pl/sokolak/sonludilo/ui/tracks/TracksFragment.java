package pl.sokolak.sonludilo.ui.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;

public class TracksFragment extends Fragment {

    private TracksViewModel tracksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tracksViewModel = new ViewModelProvider(this, new TracksViewModelFactory(getContext())).get(TracksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tracks, container, false);

        ListView trackList = root.findViewById(R.id.track_list);

        //tracksViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        tracksViewModel.getList().observe(getViewLifecycleOwner(), v -> trackList.setAdapter(new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                v))
        );


        return root;
    }
}
