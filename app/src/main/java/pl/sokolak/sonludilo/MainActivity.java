package pl.sokolak.sonludilo;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.sokolak.sonludilo.observers.VolumeObserver;
import pl.sokolak.sonludilo.tabs.player.PlayerViewModel;


public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_player, R.id.navigation_albums, R.id.navigation_tracks)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, new VolumeObserver(this, playerViewModel, null));
    }

    public void checkPermission(String permission, int requestCode) {
        ActivityCompat
                .requestPermissions(
                        this,
                        new String[]{permission},
                        requestCode);
    }
}