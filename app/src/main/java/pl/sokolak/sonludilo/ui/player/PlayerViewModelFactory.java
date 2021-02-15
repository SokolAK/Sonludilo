package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PlayerViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private final ImageView gifView;

    public PlayerViewModelFactory(Context context, ImageView gifView) {
        this.context = context;
        this.gifView = gifView;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PlayerViewModel(context, gifView);
    }
}
