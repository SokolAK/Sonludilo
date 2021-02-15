package pl.sokolak.sonludilo.ui.player;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PlayerViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private final View root;

    public PlayerViewModelFactory(Context context, View root) {
        this.context = context;
        this.root = root;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PlayerViewModel(context, root);
    }
}
