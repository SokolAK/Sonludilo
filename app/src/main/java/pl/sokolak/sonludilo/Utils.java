package pl.sokolak.sonludilo;

import android.annotation.SuppressLint;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utils {
    @SuppressLint("DefaultLocale")
    public static String formatTime(int time) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }

    public static String notNull(String string) {
        return string != null ? string.trim() : "";
    }

    public static boolean isNotEmpty(List<?> list) {
        return list != null && list.size() > 0;
    }
}
