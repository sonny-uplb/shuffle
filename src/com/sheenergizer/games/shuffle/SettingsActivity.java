package com.sheenergizer.games.shuffle;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;


public class SettingsActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	
		Preference myPref= findPreference("reset");
		myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference arg0 ){
				Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
				alertDialog.setMessage("Clear Score?")
						.setCancelable(true)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {						
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
								SharedPreferences.Editor edit = pref.edit();
								edit.putLong("hiScore", 0); 
						    	edit.putString("hiName", "Juan Dela Cruz");
						    	edit.putInt("numCorrect", 0);
						    	edit.commit();
						    	return;
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
				alertDialog.show();
				return false;
			}
		});
	}
	
}