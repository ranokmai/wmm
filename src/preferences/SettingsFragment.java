package preferences;

import models.Global;
import preferences.ColorPickerPreference;

import com.example.wmm.MainActivity;
import com.example.wmm.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

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
	private final String COLOROUT_PREFERENCE_KEY = "colorOut";
	
	/**
	 * String used as key to access button text color shared preferences. 
	 * Range: n/a, Resolution: n/a 
	 */
	private final String COLORIN_PREFERENCE_KEY = "colorIn";
	
	private ColorPickerPreference colorOutPref = null;
	private ColorPickerPreference colorInPref = null;
	
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
        
        colorOutPref = (ColorPickerPreference) findPreference(COLOROUT_PREFERENCE_KEY);
        colorInPref = (ColorPickerPreference) findPreference(COLORIN_PREFERENCE_KEY);
        
        colorOutPref.setColor( Global.colorOut);
        colorInPref.setColor( Global.colorIn);
        
        Preference resetB = (Preference) findPreference("reset");
        resetB.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference pref) { 
                
            	colorOutPref.onSetInitialValue(false, Global.outgoingCol);
                colorInPref.onSetInitialValue(false, Global.incomingCol);
                 
     			colorOutPref.setColor(colorOutPref.getColor() );
    			colorInPref.setColor(colorInPref.getColor() );
    			
                return true;
            }
        });
    }
	
	
	/**
	 * Called with every change of value to an item in the preferences. Records change in shared preferences
	 * 
	 * @param sp An instance of the shared preferences for the fragment
	 * @param key A string with the key of the preference which was modified
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		
		System.out.println("LOUIS Old value = " + Global.colorOut);
		System.out.println("LOUIS Old value = " + Global.colorIn);
		
		if (key.equals(COLOROUT_PREFERENCE_KEY))
        {            
			//force it to persist...
			colorOutPref.setColor(colorOutPref.getColor() );
			
			SharedPreferences prefs = parentSP; 
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("colorOut", colorOutPref.getColor());
            editor.commit();
	            
			Global.colorOut = this.colorOutPref.getColor();	
        }
		else if (key.equals(COLORIN_PREFERENCE_KEY))
        {
			colorInPref.setColor(colorInPref.getColor() );
			SharedPreferences prefs = parentSP; 
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("colorIn", colorInPref.getColor());
            editor.commit();
			Global.colorIn = this.colorInPref.getColor();
        }
		
		System.out.println("LOUIS New value = " + Global.colorOut);
		System.out.println("LOUIS New value = " + Global.colorIn);
		
		Global.persist = true;
		
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
