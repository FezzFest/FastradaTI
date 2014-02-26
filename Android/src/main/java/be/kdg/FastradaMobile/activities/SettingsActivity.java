package be.kdg.FastradaMobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.*;
import be.kdg.FastradaMobile.R;

/**
 * Created by Thomas on 13/02/14.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        // About Preference
        addAboutListener();
    }

    private void addAboutListener() {
        Preference aboutPreference = (Preference) findPreference("pref_about");
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
