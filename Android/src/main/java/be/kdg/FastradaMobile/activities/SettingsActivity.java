package be.kdg.FastradaMobile.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import be.kdg.FastradaMobile.R;
import org.codeandmagic.android.gauge.GaugeView;

/**
 * Created by Thomas on 13/02/14.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getPrefs();
    }


    private void getPrefs(){
        CheckBoxPreference checkBoxPreferenceAlarm = (CheckBoxPreference) findPreference("pref_alarm_enabled");
        final EditTextPreference editTextPreferenceAlarm = (EditTextPreference) findPreference("pref_alarm_temperature");

        checkBoxPreferenceAlarm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editTextPreferenceAlarm.setShouldDisableView(true);
                if ((Boolean) newValue) {
                   editTextPreferenceAlarm.setEnabled(true);
                } else {
                    editTextPreferenceAlarm.setEnabled(false);
                }
                return true;
            }
        });

    }



}
