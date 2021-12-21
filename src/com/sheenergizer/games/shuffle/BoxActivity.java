package com.sheenergizer.games.shuffle;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class BoxActivity extends Activity {

	private static final int REQUEST_CODE = 5;
	private GameData gd;
	private Question curquest;
	private int box1Id, box2Id, box3Id; // index in question set
	private Button box1Btn, box2Btn, box3Btn;
	private TextView scoreView;
	private MediaPlayer buzzShuffle, buzzEnd;
	private AlertDialog.Builder alertDialog;
	private AlertDialog.Builder quitDialog;
	private boolean soundToggle;
	private ToggleButton soundTogBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_box);
		
		SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor edit = pref.edit();
		soundToggle = pref.getBoolean("soundtoggle", true);
		
		gd = ((ShuffleGame)getApplication()).getCurrentGame();
		
		scoreView = (TextView) findViewById(R.id.scoreView);
		box1Btn = (Button) findViewById(R.id.box1Btn);
		box2Btn = (Button) findViewById(R.id.box2Btn);
		box3Btn = (Button) findViewById(R.id.box3Btn);
		
		box1Id = setButtonText(box1Btn);
		box2Id = setButtonText(box2Btn);
		box3Id = setButtonText(box3Btn);
		buzzShuffle = MediaPlayer.create(getBaseContext(), R.raw.button17);
		buzzEnd = MediaPlayer.create(getBaseContext(), R.raw.button5);
		
		soundTogBtn = (ToggleButton) findViewById(R.id.soundTogBtn);
		
		if (soundToggle) {
			soundTogBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.speaker_louder_32));
		} else {
			soundTogBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.speaker_off_32));
		} 
		soundTogBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (soundTogBtn.isChecked()) {
					edit.putBoolean("soundtoggle", true);
					edit.commit();
					soundToggle = true;
					soundTogBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.speaker_louder_32));
				} else {
					edit.putBoolean("soundtoggle", false);
					edit.commit();
					soundToggle = false;
					soundTogBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.speaker_off_32));
				}
			}
		});
		
		scoreView.setText("Correct: " + gd.getCount() + "\tScore: " + gd.getScore());
		createDialog();
	}

	private int setButtonText(Button btn) {
		
		curquest = gd.getNextQuestion();
		String dat = curquest.getCategory() + "\n" + curquest.getPts();
		
		btn.setText(dat);
		return gd.getIndex();
	}
	
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
			case R.id.shuffleBtn:
				try {
					if (soundToggle) {
						buzzShuffle.start();
					}
				} catch (IllegalStateException e) {
					
				}
				if (box1Id != -1 )
					box1Id = setButtonText(box1Btn);
				if (box2Id != -1 )
					box2Id = setButtonText(box2Btn);
				if (box3Id != -1 )
					box3Id = setButtonText(box3Btn);

				break;
			
			case R.id.box1Btn:
				
				intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("id", box1Id);
				intent.putExtra("boxnum", 1);
				startActivityForResult(intent,REQUEST_CODE);
				break;
			
			case R.id.box2Btn:
				
				intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("id", box2Id);
				intent.putExtra("boxnum", 2);
				startActivityForResult(intent,REQUEST_CODE);
				break;
			
			case R.id.box3Btn:
				
				intent = new Intent(this, QuestionActivity.class);
				intent.putExtra("id", box3Id);
				intent.putExtra("boxnum", 3);
				startActivityForResult(intent,REQUEST_CODE);
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("correct")) {
				
				int idx = data.getExtras().getInt("index");
				int pts = gd.getCurrentQuestion(idx).getPts();
				int boxnum = data.getExtras().getInt("boxnum");
				
				if (data.getExtras().getBoolean("correct")) {
					gd.addScore(pts);
					switch (boxnum)  {
						case 1:
							box1Id = setButtonText(box1Btn);
							break;
						case 2:
							box2Id = setButtonText(box2Btn);
							break;
						case 3:
							box3Id = setButtonText(box3Btn);
							break;
					}
					
				} else {
					gd.deductLife(1);
					gd.deductScore(pts);
					switch (boxnum)  {
					case 1:
						box1Btn.setEnabled(false);
						box1Btn.setText("X");
						box1Id = -1;
						break;
					case 2:
						box2Btn.setEnabled(false);
						box2Btn.setText("X");
						box2Id = -1;
						break;
					case 3:
						box3Btn.setEnabled(false);
						box3Btn.setText("X");
						box3Id = -1;
						break;
					}
				}
				
				scoreView.setText("Correct: " + gd.getCount() + "\tScore: " + gd.getScore());
				
				if (!gd.removeQuestion(idx) || gd.hasCompletedSet()) {
					if (soundToggle) {
						buzzEnd.start();
					}
					alertDialog.setMessage(R.string.msgWinner);
					alertDialog.show();
				}
								
				if (gd.isGameOver()) {
					if (soundToggle) {
						buzzEnd.start();
					}
					alertDialog.setMessage(R.string.msgLoser);
					alertDialog.show();
				}
			}
		}
	}
	
	protected void createDialog() {
		
		alertDialog = new Builder(BoxActivity.this);
		alertDialog.setTitle("Game Over")
			.setCancelable(false)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					BoxActivity.this.finish();
				}
			});
	}
	
	@Override
	public void finish() {
		if (buzzShuffle != null) buzzShuffle.release();
		if (buzzEnd != null) buzzEnd.release();
		
		Intent dat = new Intent(BoxActivity.this, MainScreenActivity.class);
		dat.putExtra("score", gd.getScore());
		dat.putExtra("correct", gd.getCount());
		setResult(RESULT_OK, dat);
		super.finish();
	}
	
	@Override
	public void onBackPressed() {
		createQuitDialog();
		quitDialog.show();
	}
	
	protected void createQuitDialog() {
    	
		   quitDialog = new Builder(BoxActivity.this);
		    	
		   quitDialog.setMessage("Are you sure you want to quit?")
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
}