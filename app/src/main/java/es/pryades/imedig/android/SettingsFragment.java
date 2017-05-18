package es.pryades.imedig.android;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by dismer on 6/03/17.
 */

public class SettingsFragment extends PreferenceFragment {

    final static String SETTINGS_IMEDIG_URL = "imedig_url";
    final static String SETTINGS_IMEDIG_TIMEOUT = "imedig_timeout";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
