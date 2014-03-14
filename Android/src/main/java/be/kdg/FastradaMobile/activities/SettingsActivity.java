package be.kdg.FastradaMobile.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import be.kdg.FastradaMobile.R;

public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        // Listeners
        addResetUIListener();
        addGaugeListener();
        addWiFiListener();
        addAboutListener();
    }

    private void addResetUIListener() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefs.edit();
        //Inserts 0 into X and Y values preferences
        Preference stylePreference = (Preference) findPreference("pref_reset_UI");
        stylePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                editor.putString("pref_UI_topLayout_X", "" + "0");
                editor.putString("pref_UI_topLayout_Y", "" + "0");
                editor.putString("pref_UI_bottomLayout_X", "" + "0");
                editor.putString("pref_UI_bottomLayout_Y", "" + "0");
                editor.putString("pref_UI_gaugeViewPort_X", "" + "0");
                editor.putString("pref_UI_gaugeViewPort_Y", "" + "0");
                editor.putString("pref_UI_leftLayout_X", "" + "0");
                editor.putString("pref_UI_leftLayout_Y", "" + "0");
                editor.putString("pref_UI_rightLayout_X", "" + "0");
                editor.putString("pref_UI_rightLayout_Y", "" + "0");
                editor.putString("pref_UI_gaugeViewLand_X", "" + "0");
                editor.putString("pref_UI_gaugeViewLand_Y", "" + "0");
                editor.commit();
                Toast.makeText(SettingsActivity.this, "UI Reset", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void addGaugeListener() {
        Preference stylePreference = findPreference("pref_gauge_style");
        if (stylePreference != null) {
            stylePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    return true;
                }
            });
        }
    }

    private void addWiFiListener() {
        Preference wifiPreference = findPreference("pref_arduino_wifi");
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
        Preference aboutPreference = findPreference("pref_about");
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
