package preferences;

import preferences.ColorPickerPreference;

import com.example.wmm.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;

/**
 * Extension of PreferenceFragment- sets up the settings menu
 *  
 * Whenever a value in the preferences menu is modified by the user,
 * the new value is stored in the shared preferences file for retrieval 
 * by the main activity.
 * 
 */
@SuppressLint("NewApi")
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    
	
	/**
	 * String used as key to access button background color shared preferences. 
	 * Range: n/a, Resolution: n/a 
	 */
	private final String COLOR1_PREFERNCE_KEY = "color1Preference";
	
	/**
	 * String used as key to access button text color shared preferences. 
	 * Range: n/a, Resolution: n/a 
	 */
	private final String COLOR2_PREFERNCE_KEY = "color2Preference";
	
	
	/**
	 * Reference to SharedPreferences file used by Android. 
	 * Units: N/A, Range: N/A, Resolution: N/A
	 */
	private static SharedPreferences parentSP;

	/**
	 * Called onCreate, stores the shared preferences file and loads in preferences
	 * 
	 * @param savedInstanceState A bundle containing the saved instance of the app when the fragment came into existence
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Activity a = getActivity();
        parentSP = PreferenceManager.getDefaultSharedPreferences(a);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.preferences);
        
        ((ColorPickerPreference)findPreference("color2")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary(ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
				return true;
			}

        });
        ((ColorPickerPreference)findPreference("color2")).setAlphaSliderEnabled(true);
    }
	
	/**
	 * Called with every change of value to an item in the preferences. Records change in shared preferences
	 * 
	 * @param sp An instance of the shared preferences for the fragment
	 * @param key A string with the key of the preference which was modified
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		
		
		if (key.equals(COLOR1_PREFERNCE_KEY))
        {
			ColorPickerPreference color1Pref = (ColorPickerPreference)findPreference(key);
            
            SharedPreferences prefs = parentSP; 
            SharedPreferences.Editor editor = prefs.edit();
            //editor.putString("color1Preference", color1Pref.);
            editor.commit();
        }
		else if (key.equals(COLOR2_PREFERNCE_KEY))
        {
			ColorPickerPreference color2Pref = (ColorPickerPreference)findPreference(key);
            
            SharedPreferences prefs = parentSP; 
            SharedPreferences.Editor editor = prefs.edit();
            //editor.putLong("color2Preference", );
            editor.commit();
        }
		
	}
	
	/**
	 * On resumption, awakens the preferences fragment after it was put to sleep
	 * 
	 */
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	}
	
	/**
	 * On close, puts the preferences fragment to sleep
	 * 
	 */
	@Override
	public void onPause() {
	    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	    super.onPause();
	} 
   
}
