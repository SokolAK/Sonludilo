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

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.SharedViewModel;

public class PlayerFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private PlayerViewModel playerViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        ImageView gifView = root.findViewById(R.id.image_view);
        //Glide.with(this).load(R.raw.tape80).into(gifView);

        playerViewModel = new ViewModelProvider(this, new PlayerViewModelFactory(getContext(), gifView)).get(PlayerViewModel.class);

        //final TextView textView = root.findViewById(R.id.text_player);
        //playerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ListView trackList = root.findViewById(R.id.track_list);
        ImageButton bPlay = root.findViewById(R.id.button_play);
        ImageButton bPause = root.findViewById(R.id.button_pause);
        ImageButton bStop = root.findViewById(R.id.button_stop);
        ImageButton bVolUp = root.findViewById(R.id.button_vol_up);
        ImageButton bVolDown = root.findViewById(R.id.button_vol_down);


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getCurrentTrackList().observe(getViewLifecycleOwner(), item -> {
            //textView.setText(item.getTitle());
            trackList.setAdapter(new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    item));

            //TextView currentTrack = root.findViewById(R.id.current_track);
            //currentTrack.setText(item.get(0).toString());
            //currentTrack.setSelected(true);
        });

        trackList.setOnItemClickListener((parent, view, position, id) ->
                playerViewModel.trackListItemClicked(position, sharedViewModel.getCurrentTrackList().getValue()));

        //System.out.println(">>>>> " + playerViewModel.getmCurrentTrackNumber().getValue());

        //playerViewModel.getmCurrentTrackNumber().observe(getViewLifecycleOwner(), n -> System.out.println(">>>>> " + n));

        bPlay.setOnClickListener(l -> playerViewModel.bPlayClicked());
        bPause.setOnClickListener(l -> playerViewModel.bPauseClicked());
        bStop.setOnClickListener(l -> playerViewModel.bStopClicked());
        bVolUp.setOnClickListener(l -> playerViewModel.bVolUpClicked());
        bVolDown.setOnClickListener(l -> playerViewModel.bVolDownClicked());

//        Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 187537);
//        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), contentUri);
//        mediaPlayer.stop();
//        try {
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mediaPlayer.start();
        return root;
    }

}