package pl.sokolak.sonludilo.ui.player;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import pl.sokolak.sonludilo.ui.player.PlayerViewModel;

public class PlayerViewModelFactory implements ViewModelProvider.Factory {
    private Context context;

    public PlayerViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PlayerViewModel(context);
    }
}
