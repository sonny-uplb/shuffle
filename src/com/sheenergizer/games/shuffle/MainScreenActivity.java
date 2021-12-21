/** Shuffle is a trivia game where the player picks a box with preferred category and points
 * 
 * created by RSM
 */
package com.sheenergizer.games.shuffle;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.text.TextUtils.TruncateAt;
//import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainScreenActivity extends Activity {

	private static final int REQUEST_CODE = 5;
	private QuestionSource qs;
	private GameData c;
	private SharedPreferences pref;
	private long newScore;
	private int numCorrect;
	private AlertDialog.Builder promptDialog;
	private AlertDialog.Builder exitDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        setContentView(R.layout.activity_main_screen);
        
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        
        TextView verView = (TextView) findViewById(R.id.verView);
        String versionName = "";
        try {
        	versionName = getPackageManager().getPackageInfo(getPackageName(), 0 ).versionName;
        } catch (NameNotFoundException e) {
        }
        
        verView.setText("v" +versionName);
        
    }

    public void onClick(View view) {
    	Intent intent;
    	switch (view.getId()) {
    		case R.id.playBtn:
    			
    			new LoadDB().execute();	
    			
    			break;
    			
    		case R.id.hiscoreBtn:
    			intent = new Intent(this, HiscoreActivity.class);
    			startActivity(intent);
    			break;
    			
    		case R.id.settingsBtn:
    			intent = new Intent(this, SettingsActivity.class);
    			startActivity(intent);
    			break;
    	}
	}

    public class LoadDB extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog progBar= new ProgressDialog(MainScreenActivity.this);
		
		public void onPreExecute() {
			progBar.setMessage("Loading questions");
		    progBar.show();
		}
		
		public Void doInBackground(Void... unused) {
			int gamelevel, maxcorrect, maxquestion;  
			
			qs = new QuestionSource(MainScreenActivity.this);
			qs.openRead();
  			if ( qs.getCount() < 1) {
  				qs.close();
  				qs.fillData();
  			}
  			
  			String level = pref.getString("gamelevel", "");
  			//Log.d("LEVEL", "L "+ level);
  			
  			if (level.equals("2")) {
  				gamelevel = 2;
  				maxcorrect = 100;
  				maxquestion = 116;
  			} else {
  				gamelevel = 1;
  				maxcorrect = 50;
  				maxquestion = 62;
  			}
  				
  			List<Question> quest = qs.getQuestionSet(gamelevel, maxquestion);
	  			
  			qs.close();

  			c = new GameData(maxcorrect);
  			c.setQuestionList(quest);
  			
  			((ShuffleGame)getApplication()).setCurrentGame(c);
  			
  			return null;
		  }

		  public void onPostExecute(Void unused) {
			  progBar.dismiss();
			  Intent intent = new Intent(MainScreenActivity.this, WelcomeActivity.class);
			  startActivityForResult(intent,REQUEST_CODE);
		  }
	}
    
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {	
    		Long score;

    		newScore = data.getExtras().getLong("score");
    		numCorrect = data.getExtras().getInt("correct");
    		score = pref.getLong("hiScore", 0);
    		if (score < newScore ) {    	    			
    			createPrompt();
    			promptDialog.show();
    		}
    	}
	}
    
    private void editScore(String name) {
    	Editor editor = pref.edit();
    	editor.putLong("hiScore", newScore); 
    	editor.putString("hiName", name);
    	editor.putInt("numCorrect", numCorrect);
    	editor.commit();
    }
    
    protected void createPrompt() {
        	
        	final EditText inputName = new EditText(MainScreenActivity.this);
        	inputName.setEllipsize(TruncateAt.END);
        	inputName.setSingleLine();
        	
        	promptDialog = new Builder(MainScreenActivity.this);
        	
        	promptDialog.setView(inputName)
        				.setTitle("New High Score")
        				.setMessage("Enter name:")
        				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {				
        					@Override
        					public void onClick(DialogInterface dialog, int which) {
        						String name = inputName.getText().toString();
        						editScore(name);
        					}
        				});
    }
        
    protected void createExitDialog() {
        	
        exitDialog = new Builder(MainScreenActivity.this);
        	
        exitDialog.setTitle("Exit Shuffle")
        		.setMessage("Are you sure you want to exit?")
        		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {				
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				finish();
        			}
        		})
        		.setNegativeButton("No", new DialogInterface.OnClickListener() {				
        			@Override
        			public void onClick(DialogInterface dialog, int which) {
        				return;
        			}
        		});
     }
        
    @Override
    public void onBackPressed() {
    	createExitDialog();
        exitDialog.show();
    }
        
        @Override
        public void finish() {
        	super.finish();
        }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }
    
}