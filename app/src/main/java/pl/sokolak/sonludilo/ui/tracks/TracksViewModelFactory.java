package pl.sokolak.sonludilo.ui.tracks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TracksViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public TracksViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TracksViewModel(context);
    }
}
