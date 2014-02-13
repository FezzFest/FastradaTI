package be.kdg.FastradaMobile.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import be.kdg.FastradaMobile.R;

/**
 * Created by Thomas on 13/02/14.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
