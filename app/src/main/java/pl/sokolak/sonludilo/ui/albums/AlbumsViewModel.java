package pl.sokolak.sonludilo.ui.albums;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.ui.tracks.Track;
import pl.sokolak.sonludilo.ui.tracks.TracksRepository;

public class AlbumsViewModel extends ViewModel {
    private Context context;
    private final MutableLiveData<List<String>> mAlbums = new MutableLiveData<>();
    private final List<Album> albumsList;

    public AlbumsViewModel(Context context) {
        this.context = context;

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
        return new TracksRepository(context).getAll(selection);
    }
}
