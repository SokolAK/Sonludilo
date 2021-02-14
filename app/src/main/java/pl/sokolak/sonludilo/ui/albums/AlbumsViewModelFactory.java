package pl.sokolak.sonludilo.ui.albums;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import pl.sokolak.sonludilo.ui.albums.AlbumsViewModel;


public class AlbumsViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public AlbumsViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AlbumsViewModel(context);
    }
}
