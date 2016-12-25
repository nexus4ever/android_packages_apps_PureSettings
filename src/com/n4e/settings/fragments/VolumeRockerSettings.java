package com.n4e.settings.fragments;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class VolumeRockerSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String VOLUME_KEY_CURSOR_CONTROL = "volume_key_cursor_control";
    private static final String SCREENRECORD_CHORD_TYPE = "screenrecord_chord_type";

    private ListPreference mVolumeKeyCursorControl;
    private ListPreference mScreenrecordChordType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.n4e_settings_volume);
        final ContentResolver resolver = getActivity().getContentResolver();

        // volume key cursor control
        int cursorControlAction = Settings.System.getInt(resolver,
                Settings.System.VOLUME_KEY_CURSOR_CONTROL, 0);
        mVolumeKeyCursorControl = initActionList(VOLUME_KEY_CURSOR_CONTROL,
cursorControlAction);

        // screenrecord chord type
        int recordChordValue = Settings.System.getInt(resolver,
                Settings.System.SCREENRECORD_CHORD_TYPE, 0);
        mScreenrecordChordType = initActionList(SCREENRECORD_CHORD_TYPE,
                recordChordValue);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        if (preference == mVolumeKeyCursorControl) {
            String volumeKeyCursorControl = (String) value;
            int volumeKeyCursorControlValue = Integer.parseInt(volumeKeyCursorControl);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.VOLUME_KEY_CURSOR_CONTROL, volumeKeyCursorControlValue);
            int volumeKeyCursorControlIndex = mVolumeKeyCursorControl
                    .findIndexOfValue(volumeKeyCursorControl);
            mVolumeKeyCursorControl
                    .setSummary(mVolumeKeyCursorControl.getEntries()[volumeKeyCursorControlIndex]);
            return true;
        } else if  (preference == mScreenrecordChordType) {
            handleActionListChange(mScreenrecordChordType, value,
                    Settings.System.SCREENRECORD_CHORD_TYPE);
            return true;
        }
        return false;
    }

    private ListPreference initActionList(String key, int value) {
        ListPreference list = (ListPreference) getPreferenceScreen().findPreference(key);
        list.setValue(Integer.toString(value));
        list.setSummary(list.getEntry());
        list.setOnPreferenceChangeListener(this);
        return list;
    }

    private void handleActionListChange(ListPreference pref, Object newValue, String setting) {
        String value = (String) newValue;
        int index = pref.findIndexOfValue(value);
        pref.setSummary(pref.getEntries()[index]);
        Settings.System.putInt(getActivity().getContentResolver(), setting, Integer.valueOf(value));
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.NEXUS_SETTINGS;
    }
}
