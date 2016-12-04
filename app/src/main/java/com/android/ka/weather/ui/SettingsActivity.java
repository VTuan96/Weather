package com.android.ka.weather.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.android.ka.weather.R;
import com.android.ka.weather.prefs.ShowWallpaperPrefs;

public class SettingsActivity extends AppCompatActivity {

    public static final String ACTION_CHANGE_BACKGROUND = "com.android.ka.weather.ui.ACTION_CHANGE_BACKGROUND";
    private IntentFilter filter = new IntentFilter(ACTION_CHANGE_BACKGROUND);
    private RelativeLayout rela;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (ShowWallpaperPrefs.getShowWallpaper()) {
                    rela.setBackgroundResource(R.mipmap.ic_launcher);
                } else {
                    rela.setBackgroundResource(R.drawable.clear);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rela = (RelativeLayout) findViewById(R.id.relaSettings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
