package pl.sokolak.sonludilo.ui.albums;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import pl.sokolak.sonludilo.R;
import pl.sokolak.sonludilo.Utils;
import pl.sokolak.sonludilo.ui.tracks.Track;
import pl.sokolak.sonludilo.ui.tracks.TracksRepository;

public class AlbumsViewModel extends ViewModel {
    private final MutableLiveData<List<List<String>>> albumsListString = new MutableLiveData<>();
    private final MutableLiveData<List<Album>> albumsList = new MutableLiveData<>();
    private WeakReference<Context> weakContext;

    public void setContext(Context context) {
        weakContext = new WeakReference<>(context);
    }

    public void readAlbumsList(String selection, List<String> sort) {
        Context context = weakContext.get();
        AlbumsRepository albumsRepository = new AlbumsRepository(context);
        albumsList.setValue(albumsRepository.getAll(selection, sort));
    }

    public void setAlbumsListString() {
        List<List<String>> list = new ArrayList<>();
        if(Utils.isNotEmpty(albumsList.getValue())) {
            Context context = weakContext.get();
            for (Album album : albumsList.getValue()) {
                String artist = context.getString(R.string.artist) + ": " + album.getArtist();
                String title = context.getString(R.string.title) + ": " + album.getTitle();
                String noTracks = context.getString(R.string.number_of_tracks) + ": " + album.getNoTracks();
                String year = context.getString(R.string.year) + ": " + album.getYear();
                list.add(List.of(artist, title, noTracks + "\n" + year));
            }
        }
        albumsListString.setValue(list);
    }

    public List<Track> getTrackListForAlbum(int position) {
        if (Utils.isNotEmpty(albumsList.getValue())) {
            String selection = "album_id = " + albumsList.getValue().get(position).getId();
            return new TracksRepository(weakContext.get()).getAll(selection, List.of("album"));
        }
        return new ArrayList<>();
    }

    public LiveData<List<Album>> getAlbumsList() {
        return albumsList;
    }

    public LiveData<List<List<String>>> getAlbumsListString() {
        return albumsListString;
    }
}
