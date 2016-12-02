package com.android.ka.weather.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.android.ka.weather.R;

/**
 * Created by ka on 02/12/2016.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_prefs);
    }
}
