//
// Project 4
// Names: Esther Finkelstein, Tyler Sax, Luca Soldaini
// Emails: efinkelstein25@gmail.com, tylersax@gmail.com, luca.soldaini@gmail.com
// Instructor: Singh
// COSC 150
//
// In accordance with the class policies and Georgetown's Honor Code, 
// we certify that, with the exceptions of the lecture notes and those
// items noted below, I have neither given nor recieved any assistance
// on this project.
//
// Description: This program serves as a piece of educational software for 
// children between the ages of five and seven. Users will first go through
// six lessons about internal and external parts of a computer. The internal
// parts covered are the CPU, Power Supply, and Bus System. The external parts 
// covered are the speakers, microphone, and webcam. In each lesson, the computer
// part is compared to a body part. Afterwards, the user takes a quiz on what he
// has learned. At the end, the user has the option of sharing his scores with others. 
//
//
//


package com.touchPackage;

import com.touchPackage.R;
import com.touchPackage.R.id;
import com.touchPackage.R.layout;
import com.touchPackage.App;
import com.touchPackage.ShareScores;

import java.io.File;
import java.util.Locale;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 *
 * @author Esther Finkelstein, Tyler Sax, Luca Soldaini
 * 
 * This class creates the Intro screen. It explains the application to the user, 
 * and allows him to enter his name. In addition, by clicking on the speaker image, 
 * the user can hear the explanation spoken out loud. Afterwards, the user clicks on 
 * the button at the bottom, which brings him to the Menu screen.
 *
 */
public class IntroActivity extends Activity {

	private Button nextScreenButton; //button to go to the the Menu screen
	private EditText nameText; //where the user puts their name
	private TextView explanationText; //explanation of the program
	private App myApp;
	private MediaPlayer mp_file; //audio file with recorded text

	private Dialog pictureHintDialog; // speak button shown when voice speaks the instructions
	private Handler pictureHintButtonHandler; //handler that handles the delay for pictureHintDialog
	final static int SHOW_HINT_DIALOG = 1;
	final static int HIDE_HINT_DIALOG = 0;
	final static int showPictureHint = 18000; // time when pictureHintDialog get shown
	final static int hidePictureHint = 20500; // time when pictureHintDialog disappears


	/** Called when the activity is first created.*/
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_activity);

		nextScreenButton = (Button) findViewById(R.id.nextscreen);
		nameText = (EditText) findViewById(R.id.enterName);
		explanationText = (TextView) findViewById(R.id.textView2);
		myApp = (App)getApplicationContext();
		mp_file = MediaPlayer.create(this, R.raw.intro_audio);

		/* sets up the dialog that will appear during the instructions */ 
		pictureHintDialog = new Dialog(this);
		pictureHintDialog.setContentView(R.layout.teach_button_alert);
		pictureHintDialog.setCancelable(false);

		/* sets up the handler to show the dialog with the proper delay */
		pictureHintButtonHandler = new Handler(new Handler.Callback()
		{
			@Override
			public boolean handleMessage(Message msg) {
				if(msg.what==SHOW_HINT_DIALOG){
					pictureHintDialog.show();
				}else if (msg.what==HIDE_HINT_DIALOG){
					pictureHintDialog.dismiss();
				}
				return false;
			}
		});

		//switches activities to the Menu Activity
		nextScreenButton.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v){
				launchMenuScreen(); 
			}
		});

		/* start speaking the instructions and launches the handler with the proper delay */
		mp_file.start(); 
		pictureHintButtonHandler.sendEmptyMessageDelayed(SHOW_HINT_DIALOG, showPictureHint);
		pictureHintButtonHandler.sendEmptyMessageDelayed(HIDE_HINT_DIALOG, hidePictureHint);

	}//onCreate


	/**
	 * Launches the Menu Activity 
	 * 
	 */

	protected void launchMenuScreen(){
		if (mp_file.isPlaying()) mp_file.stop(); // stop speaking if the user clicks on the lessons button
		Intent i = new Intent(this, MenuActivity.class);
		myApp.setName(nameText.getText().toString()); //get the name from user and set gloabl name variable 
		startActivity(i);
		//this.finish();
	}
}//IntroActivity 
