package com.sheenergizer.games.shuffle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.graphics.PixelFormat;


public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_welcome);
		
	}

	public void onClick(View view) {
		
    	switch (view.getId()) {
    		case R.id.goBtn:
    			Intent intent = new Intent(WelcomeActivity.this, BoxActivity.class);
    			intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
    			
    			startActivity(intent);
    			WelcomeActivity.this.finish();
    			break;
    	}
	}

}