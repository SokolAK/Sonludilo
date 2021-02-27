package pl.sokolak.sonludilo.ui.albums;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.ui.tracks.Track;
import pl.sokolak.sonludilo.ui.tracks.TracksRepository;

public class AlbumsViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    private final MutableLiveData<List<List<String>>> mAlbums = new MutableLiveData<>();
    private final List<Album> albumsList;

    public AlbumsViewModel(Context context) {
        weakContext = new WeakReference<>(context);

        AlbumsRepository albumsRepository = new AlbumsRepository(context);
        albumsList = albumsRepository.getAll();

        List<List<String>> list = new ArrayList<>();
        for (Album album : albumsList) {
            String artist = context.getString(R.string.artist) + ": " + album.getArtist();
            String title = context.getString(R.string.title) + ": " + album.getTitle();
            String noTracks = context.getString(R.string.number_of_tracks) + ": " + album.getNoTracks();
            String year = context.getString(R.string.year) + ": " + album.getYear();
            list.add(List.of(artist, title, noTracks + "\n" + year));
        }

        mAlbums.setValue(list);
    }

    public LiveData<List<List<String>>> getList() {
        return mAlbums;
    }

    public List<Track> getTrackListForAlbum(int position) {
        String selection = "album_id = " + albumsList.get(position).getId();
        if (weakContext != null) {
            return new TracksRepository(weakContext.get()).getAll(selection);
        }
        return new ArrayList<>();
    }
}
