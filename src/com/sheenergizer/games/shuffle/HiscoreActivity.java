package com.sheenergizer.games.shuffle;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;


public class HiscoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_hiscore);
	
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		Long score = pref.getLong("hiScore", 0);
		String name = pref.getString("hiName", "Juan Dela Cruz");
		int correct = pref.getInt("numCorrect", 0);
		
		TextView hiscore = (TextView) findViewById(R.id.hiscoreView);
		hiscore.setText("Highest Grand Prize Winner\n" + name + "\n"+ score + " points"
				+ "\nCorrect answer: " + correct);
	}

	public void onClick(View view) {
		
    	switch (view.getId()) {
    		case R.id.backBtn:
    			finish();
    			break;
    	}
	}
}
