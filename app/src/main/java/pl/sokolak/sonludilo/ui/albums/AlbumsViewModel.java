package pl.sokolak.sonludilo.ui.albums;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;
import pl.sokolak.sonludilo.ui.tracks.TracksRepository;

public class AlbumsViewModel extends ViewModel {
    private final WeakReference<Context> weakContext;
    private final MutableLiveData<List<String>> mAlbums = new MutableLiveData<>();
    private final List<Album> albumsList;

    public AlbumsViewModel(Context context) {
        weakContext = new WeakReference<>(context);

        AlbumsRepository albumsRepository = new AlbumsRepository(context);
        albumsList = albumsRepository.getAll();

        List<String> list = new ArrayList<>();
        for(Album album : albumsList) {
            list.add(album.toMultiLineString(context));
        }

        mAlbums.setValue(list);
    }

    public LiveData<List<String>> getList() {
        return mAlbums;
    }

    public List<Album> getAlbumsList() {
        return albumsList;
    }

    public List<Track> getTrackListForAlbum(int position) {
        String selection = "album_id = " + albumsList.get(position).getId();
        if (weakContext!=null) {
            return new TracksRepository(weakContext.get()).getAll(selection);
        }
        return new ArrayList<>();
    }
}
