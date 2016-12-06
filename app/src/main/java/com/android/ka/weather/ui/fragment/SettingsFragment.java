package com.android.ka.weather.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.android.ka.weather.R;
import com.android.ka.weather.prefs.ShowWallpaperPrefs;
import com.android.ka.weather.prefs.UsingCurrentLocationPrefs;
import com.android.ka.weather.prefs.WeatherPrefs;
import com.android.ka.weather.ui.MainActivity;
import com.android.ka.weather.ui.SettingsActivity;

/**
 * Created by ka on 02/12/2016.
 */

public class SettingsFragment extends PreferenceFragment {
    public static final String CURRENT = "CURRENT";
    Preference prefsLocation;
    private CheckBoxPreference checkBoxPreference;
    private ListPreference listPreference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_prefs);
        prefsLocation = findPreference("prefs_location");
        checkBoxPreference = (CheckBoxPreference) findPreference("prefs_show");
        listPreference = (ListPreference) findPreference("prefs_time_update");

        prefsLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                UsingCurrentLocationPrefs.setUsingCurrentLocation(true);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setAction(CURRENT);
                startActivity(intent);
                return false;
            }
        });

        checkBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ShowWallpaperPrefs.setShowWallpaper(checkBoxPreference.isChecked());
                Intent intent = new Intent();
                intent.setAction(SettingsActivity.ACTION_CHANGE_BACKGROUND);
                getActivity().sendBroadcast(intent);
                return false;
            }
        });

        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listPreference.setSummary(listPreference.getEntries()[listPreference.findIndexOfValue(newValue.toString())]);
                WeatherPrefs.setUpdate(Integer.parseInt(newValue.toString()));
                return true;
            }
        });
    }
}
