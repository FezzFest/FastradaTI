package be.kdg.FastradaMobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.*;
import android.widget.Toast;
import be.kdg.FastradaMobile.R;

/**
 * Created by Thomas on 13/02/14.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        // Listeners
        addGaugeListener();
        addWiFiListener();
        addAboutListener();
    }

    private void addGaugeListener() {
        Preference stylePreference = (Preference) findPreference("pref_gauge_style");
        if (stylePreference != null) {
            stylePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Toast.makeText(SettingsActivity.this, "Please restart the application.", Toast.LENGTH_LONG).show();

                    return true;
                }
            });
        }
    }

    private void addWiFiListener() {
        Preference wifiPreference = (Preference) findPreference("pref_arduino_wifi");
        if (wifiPreference != null) {
            wifiPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent tetherSettings = new Intent();
                    tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
                    startActivity(tetherSettings);

                    return true;
                }
            });
        }
    }

    private void addAboutListener() {
        Preference aboutPreference = (Preference) findPreference("pref_about");
        if (aboutPreference != null) {
            aboutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                    startActivity(intent);

                    return true;
                }
            });
        }
    }
}
