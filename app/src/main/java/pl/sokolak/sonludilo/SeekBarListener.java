package pl.sokolak.sonludilo;

import android.widget.SeekBar;

import pl.sokolak.sonludilo.ui.player.PlayerViewModel;

public class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
    private PlayerViewModel playerViewModel;

    public SeekBarListener(PlayerViewModel playerViewModel) {
        this.playerViewModel = playerViewModel;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        playerViewModel.touchSeekBarProgress();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        playerViewModel.seekBarChanged(seekBar.getProgress());
        playerViewModel.releaseSeekBarProgress();
    }
}