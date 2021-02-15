package pl.sokolak.sonludilo;

import android.Manifest;
import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.sokolak.sonludilo.ui.SharedViewModel;
import pl.sokolak.sonludilo.ui.player.PlayerFragment;

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

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_player, R.id.navigation_albums, R.id.navigation_tracks)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SharedViewModel sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, new VolumeObserver(this, sharedViewModel, null) );
    }


    public void checkPermission(String permission, int requestCode) {
        ActivityCompat
                .requestPermissions(
                        this,
                        new String[]{permission},
                        requestCode);
//        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat
//                    .requestPermissions(
//                            this,
//                            new String[]{permission},
//                            requestCode);
//        } else {
//            Toast
//                    .makeText(this,
//                            "Permission already granted",
//                            Toast.LENGTH_SHORT)
//                    .show();
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super
//                .onRequestPermissionsResult(requestCode,
//                        permissions,
//                        grantResults);
//
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this,
//                        "Storage Permission Granted",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                Toast.makeText(MainActivity.this,
//                        "Storage Permission Denied",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }
}