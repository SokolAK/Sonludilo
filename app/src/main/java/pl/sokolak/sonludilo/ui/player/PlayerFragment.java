package pl.sokolak.sonludilo.ui.player;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.IOException;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.tracks.Track;

public class PlayerFragment extends Fragment {

    private PlayerViewModel playerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        playerViewModel = new ViewModelProvider(this, new PlayerViewModelFactory(getContext())).get(PlayerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        //final TextView textView = root.findViewById(R.id.text_player);
        //playerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ListView trackList = root.findViewById(R.id.track_list);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getCurrentTrackList().observe(getViewLifecycleOwner(), item -> {
            //textView.setText(item.getTitle());
            trackList.setAdapter(new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    item));

            //TextView currentTrack = root.findViewById(R.id.current_track);
            //currentTrack.setText(item.get(0).toString());
            //currentTrack.setSelected(true);
        });

//        if(model.getCurrentTrackList().getValue() != null) {
//            for (Track track : model.getCurrentTrackList().getValue()) {
//                System.out.println(track.toString());
//            }
//        }

        ImageView imageView = root.findViewById(R.id.image_view);
        Glide.with(this).load(R.raw.tape80).into(imageView);




        Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 187537);
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), contentUri);
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();


        return root;
    }

}