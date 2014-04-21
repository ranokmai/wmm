package preferences;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

/**
 * Extension of Activity- overlays context with settings fragment.
 * 
 */
public class SettingsActivity extends Activity {	
	
	/**
	 * Called onCreate, replaces the content with a PreferenceFragment
	 * 
	 * @param savedInsatnceState a bundle containing the saved instance of the app when the Activity was created 
	 */
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace( android.R.id.content, new SettingsFragment() )
                .commit();
        
    }
	
}