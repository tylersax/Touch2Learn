package com.touchPackage;

import java.io.File;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.touchPackage.IntroActivity;
import com.touchPackage.App;
import com.touchPackage.MenuActivity;

/**
 * 
 * This Activity creates a screen so that the user can share his scores. 
 * 
 */
public class ShareScores extends Activity {

	Button shareButton; //share scores 
	Button introButton; //go back to intro screen
	TextView congrats; //congratulations text
	TextView scoreText; //shows the score 
	private App myApp;
	int score; // user's score
	String letterGrade; //letter grade score 
	String name; //user's name
	private MediaPlayer mp_file; //audio file with the spoken text

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_scores);

		shareButton = (Button) findViewById(R.id.shareScores);
		introButton = (Button) findViewById(R.id.goToIntro);
		congrats = (TextView) findViewById(R.id.textViewCongrats);
		mp_file = MediaPlayer.create(this, R.raw.share_audio);
		scoreText = (TextView) findViewById(R.id.score);

		myApp = (App)getApplicationContext();
		name = myApp.getName();
		score = myApp.getScore();
		
		//shows letter grade score on screen
		scoreText.setText("Your letter grade is: " + getLetterGrade(score));

		//personalized congratulations message 
		congrats.setText(name + ", congratulations on completing the quizzes! Now you can share your scores!");

		mp_file.start();//play the congratulations audio

		//calls the method to share scores 
		shareButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v){
				shareScores();
			}
		});

		//calls methods to switch back the IntroActivity
		introButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				launchIntroScreen();
			}
		});
	}//onCreate

	/**
	 * launches IntroActivity
	 */
	protected void launchIntroScreen(){
		myApp.setLessonStatus(0);//Reset the player's lessonStatus
		myApp.setQuizStatus(0);//Reset the player's quizStatus
		myApp.setName("");//Reset the player's name
		Intent i = new Intent(this, IntroActivity.class);
		startActivity(i);
		this.finish();//end the current activity
	}

	/**
	 * Shares the user's score
	 */
	public void shareScores(){
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Score");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I scored "+ score);
		startActivity(Intent.createChooser(shareIntent, "Share your score"));
	}
	/**
	 * 
	 * @param numberScore number of points scored
	 * @return letter grade equivalent 
	 */
	public String getLetterGrade(int numberScore)
	{	
		if (numberScore >= 550){
			return "A";
		}
		if (numberScore >= 450 && numberScore < 550){
			return "B";
		}
		if (numberScore >= 350 && numberScore < 450){
			return "C";
		}
		if (numberScore >= 250 && numberScore < 350){
			return "D";
		}
		if (numberScore < 250){
			return "F";
		}
		else return"";
	}//getLetterGrade

}
