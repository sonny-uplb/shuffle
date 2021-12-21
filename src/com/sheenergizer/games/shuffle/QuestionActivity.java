package com.sheenergizer.games.shuffle;

import java.util.List;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class QuestionActivity extends Activity {

	private GameData gd;
	private Question curquest;
	private Button opt1Btn, opt2Btn, opt3Btn, opt4Btn;
	private List<String> options;
	private Bundle extras;
	private int index, boxnum;
	private MediaPlayer buzz;
	private CountDownTimer mCountDown;
	private boolean soundToggle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.activity_question);
		
		TextView txtView = (TextView) findViewById(R.id.questionView);
		final TextView timerView = (TextView) findViewById(R.id.timerView);
		
		SharedPreferences pref  = PreferenceManager.getDefaultSharedPreferences(this);
		soundToggle = pref.getBoolean("soundtoggle", true);
		
		opt1Btn = (Button) findViewById(R.id.opt1Btn);
		opt2Btn = (Button) findViewById(R.id.opt2Btn);
		opt3Btn = (Button) findViewById(R.id.opt3Btn);
		opt4Btn = (Button) findViewById(R.id.opt4Btn);
		
		extras = getIntent().getExtras();
		
		if (extras == null) {
	          return;
	    }
		
		gd = ((ShuffleGame)getApplication()).getCurrentGame();
		index = extras.getInt("id");
		boxnum = extras.getInt("boxnum");
		
		/* Get the question */
		curquest = gd.getCurrentQuestion(index);
		
		txtView.setText(curquest.getQuestion());
		options = curquest.getQuestionOptions();
		
		opt1Btn.setText(options.get(0));
		opt2Btn.setText(options.get(1));
		opt3Btn.setText(options.get(2));
		opt4Btn.setText(options.get(3));
		
		if (mCountDown != null) {
			mCountDown.cancel();
		}
		
		mCountDown = new CountDownTimer(13000, 1000) {
			@Override
			public void onFinish() {
				timerView.setText("Time's up!");
				results(false);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				timerView.setText("Time left: "+ String.valueOf(millisUntilFinished / 1000));
			}
		}.start();
		
	}

	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.opt1Btn:
				results(opt1Btn.getText().equals(curquest.getAnswer()));			
				break;
			case R.id.opt2Btn:
				results(opt2Btn.getText().equals(curquest.getAnswer()));
				break;
			case R.id.opt3Btn:
				results(opt3Btn.getText().equals(curquest.getAnswer()));
				break;
			case R.id.opt4Btn:
				results(opt4Btn.getText().equals(curquest.getAnswer()));
				break;
		}
	}
	
	public void results(boolean correct) {
		if (buzz != null) {
			buzz.stop();
			buzz.release();
			buzz = null;
		}
		
		if (correct) {
			try {
				buzz = MediaPlayer.create(this, R.raw.button11);
				if (soundToggle) {
					buzz.start();
				}
			} catch (IllegalStateException e) {
				//e.printStackTrace();
			}
		} else {
			try {
				buzz = MediaPlayer.create(this, R.raw.button10);
				if (soundToggle) {
					buzz.start();
				}
			} catch (IllegalStateException e) {
				//e.printStackTrace();
			}
		}
		
		mCountDown.cancel();
		Intent intent = new Intent();
		String ans = correct ? "Correct!" : "Wrong!";
		
		Toast.makeText(getApplicationContext(), ans, Toast.LENGTH_SHORT).show();
		intent.putExtra("correct", correct);
		intent.putExtra("index", index);
		intent.putExtra("boxnum", boxnum);
		setResult(RESULT_OK, intent);
		finish();
		
	}

	@Override
	public void finish() {
		super.finish();
	}
	
	@Override
	public void onBackPressed() {
	}
	
}