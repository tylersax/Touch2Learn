package com.touchPackage;

import java.util.Locale;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/** LessonActivity
 * 
 * This Activity creates the lessons. The user drags the image of a computer part
 * to the speaker image, after which the program reads the lesson out loud to the user.
 *
 */
public class LessonActivity extends Activity{

	private App myApp;

	/* if the image was dragged to the speaker */
	private boolean lessonTaught;

	/* text to speech button on the top right. */
	private Button speakButton;

	/*button that takes you to the next lesson  */
	private Button nextLessonButton;

	/*body and computer parts at the bottom of the screen */
	private ImageView bodyPartImage;
	private ImageView computerPartImage;

	private RelativeLayout lessonLayout;

	/* instructions on how to listen to the lessons */
	private ImageView dragInstructions;

	/* which lesson the user is on */
	private int lessonStatus;  

	/* audio file with the recorded lesson */
	private MediaPlayer mp_file;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//Check with player to determine Lesson status, then set appropriate variables
		myApp = (App)getApplicationContext();
		lessonStatus = myApp.getLessonStatus(); 

		dragInstructions=null;

		//sets the layout to the appropriate lesson 
		switch (lessonStatus){
		case 0:
			setContentView(R.layout.lesson1);
			mp_file = MediaPlayer.create(this, R.raw.lesson1audio);


			// sets up the instruction mask to show on the first lesson
			// the instructions (a big arrow and a "drag me" text can be dismissed by 
			// tapping everywhere on the screen or by start dragging the computer part
			// if the user does not dismiss them, they automatically fade out after 5 seconds
			dragInstructions = (ImageView) this.findViewById(R.id.instructions);
			dragInstructions.setOnClickListener(new OnClickListener (){

				@Override
				public void onClick(View v) {
					dragInstructions.clearAnimation();
					dragInstructions.setVisibility(View.INVISIBLE);
					dragInstructions.setEnabled(false);
				}	
			});

			break;			
		case 1:
			setContentView(R.layout.lesson2);
			mp_file = MediaPlayer.create(this, R.raw.lesson2audio);
			break;
		case 2:
			setContentView(R.layout.lesson3);
			mp_file = MediaPlayer.create(this, R.raw.lesson3audio);
			myApp.lesson1completed();
			break;
		case 3:
			setContentView(R.layout.lesson4);
			mp_file = MediaPlayer.create(this, R.raw.lesson4audio);
			break;
		case 4:
			setContentView(R.layout.lesson5);
			mp_file = MediaPlayer.create(this, R.raw.lesson5audio);
			break;
		case 5:
			setContentView(R.layout.lesson6);
			mp_file = MediaPlayer.create(this, R.raw.lesson6audio);
			myApp.lesson2completed();
			break;
		}

		/* gets the display proprieties. they are needed to compute correctely all the animations */
		Display display = getWindowManager().getDefaultDisplay();
		Point displaySize = new Point();
		display.getSize(displaySize);

		bodyPartImage = (ImageView) this.findViewById(R.id.bodypart);
		bodyPartImage.setVisibility(View.INVISIBLE);

		computerPartImage = (ImageView) this.findViewById(R.id.computerpart);
		computerPartImage.setOnTouchListener(new Dragger(displaySize));	

		lessonLayout = (RelativeLayout) this.findViewById(R.id.lessonScreenLayout);

		speakButton = (Button) this.findViewById(R.id.speakButton);
		speakButton.setOnClickListener(new Speaker());

		nextLessonButton = (Button) this.findViewById(R.id.nextLessonButton);
		nextLessonButton.setClickable(false);
		nextLessonButton.setVisibility(View.INVISIBLE);

		lessonTaught=false;

		if(dragInstructions!=null){
			fadeOutInstructions(); //fades out the instructions after 5 seconds if they are being shown 
		}
	}

	/* showBodyPart
	 * 
	 * shows the body part image used as analogy
	 * with a nice fade-in effect
	 * 
	 * @param new_x the x location of the body part
	 * @param new_y the y location of the body part
	 */
	protected void showBodyPart (int new_x, int new_y){

		LayoutParams newParameters = new LayoutParams (bodyPartImage.getLayoutParams());
		newParameters.leftMargin = new_x;
		newParameters.topMargin = new_y;
		bodyPartImage.setLayoutParams(newParameters);

		Animation fadeIn = new AlphaAnimation(0.0f, 1.0f); 
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setFillAfter(true);
		fadeIn.setDuration(700); //duration of the animation; 0.7 seconds
		bodyPartImage.startAnimation(fadeIn);
	}

	/* showNextLessonButton
	 *	
	 * show the button to jump to the next lesson with a nice fade in animation
	 * once is show, the button is set as clickable
	 */
	protected void showNextLessonButton (){
		Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setFillAfter(true);
		fadeIn.setDuration(700); //duration of the animation; 0.7 seconds
		nextLessonButton.startAnimation(fadeIn);
		nextLessonButton.setClickable(true);
		nextLessonButton.setVisibility(View.VISIBLE);
	}

	/* fadeOutInstructions 
	 *
	 * dismiss the drag instructions; note that this instructions (a big arrow an a "drag me" text
	 * are shown only in the first lesson. the instructions are automatically dismissed after 5 seconds 
	 */
	private void fadeOutInstructions (){
		Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
		fadeOut.setInterpolator(new DecelerateInterpolator());
		fadeOut.setFillAfter(true);
		fadeOut.setDuration(700);
		fadeOut.setStartOffset(5000);		
		dragInstructions.startAnimation(fadeOut);	
		dragInstructions.setEnabled(false);
	}

	/* putComputerPartBack
	 * 
	 * moves the image of the computer part back to its original location
	 * the parameters for the translation need to be passed to the method because 
	 * the computer part moves back to different locations if it has been dragged 
	 * on the speak button or not
	 * 
	 * @param fromXDelta 
	 * @param toXDelta 
	 * @param fromYDelta 
	 * @param toYDelta 
	 */
	protected void putComputerPartBack(int fromXDelta, int toXDelta, int fromYDelta, int toYDelta){

		TranslateAnimation putBack = new TranslateAnimation (
				Animation.ABSOLUTE, fromXDelta,
				Animation.ABSOLUTE, toXDelta,
				Animation.ABSOLUTE, fromYDelta,
				Animation.ABSOLUTE, toYDelta);
		putBack.setDuration(700);
		computerPartImage.startAnimation(putBack);
	}

	/* Dragger
	 * this is a custom on touch listener that is used by the computer part image
	 */ 
	private class Dragger implements OnTouchListener {

		/* the current drag status (image touched, image dragged, image released) */ 
		private int status;

		/* the sets of parameters used to set the location of the computer part */
		private LayoutParams newParameters;

		/* width and height of the display */
		private Point displaySize;

		/* the height of the statusbar */
		int statusbarHeight;

		public Dragger(Point displaySize){		
			newParameters = new LayoutParams (computerPartImage.getLayoutParams());
			this.displaySize= displaySize;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			status = event.getAction();			

			/* calculates the height of the status bar. note that it has to be done 
			 * here instead of doing it in the constructor because lessonLayout doesn't get
			 * set up until onCreate finish to execute */
			statusbarHeight = (int) (displaySize.y-lessonLayout.getHeight());

			//if the drag action starts 
			if(status==MotionEvent.ACTION_DOWN){

				// hides the instructions
				if(dragInstructions!=null){
					if(dragInstructions.getVisibility()==View.VISIBLE){
						dragInstructions.clearAnimation();
						dragInstructions.setVisibility(View.INVISIBLE);
						dragInstructions.setEnabled(false);
					}
				}

			}else if( status== MotionEvent.ACTION_MOVE){

				newParameters.leftMargin=computeX((int)event.getRawX());
				newParameters.topMargin=computeY((int)event.getRawY());
				computerPartImage.setLayoutParams(newParameters);

			}else if (status == MotionEvent.ACTION_UP){
				int old_x,old_y, new_x, new_y;


				if(droppedOnSpeakButton((int)event.getRawX(), (int)event.getRawY())){

					mp_file.start();
					showBodyPart(displaySize.x/2-(computerPartImage.getWidth()/2)+128, 
							((displaySize.y-statusbarHeight)*3)/4);
					lessonTaught = true;
				}


				if(lessonTaught){	
					old_x=(int) computeX((int)event.getRawX());
					old_y=(int) computeY((int)event.getRawY());
					new_x=(int) displaySize.x/2-(computerPartImage.getWidth()/2)-128;
					new_y=(int) (((displaySize.y-statusbarHeight)*3)/4);	
				}else{
					old_x=(int) computeX((int)event.getRawX());
					old_y=(int) computeY((int)event.getRawY());
					new_x=(int) displaySize.x/2-(computerPartImage.getWidth()/2);
					new_y=(int) (((displaySize.y-statusbarHeight)*3)/4);
				}
				putComputerPartBack((old_x-new_x),0, (old_y-new_y), 0);

				newParameters.leftMargin = new_x;
				newParameters.topMargin = new_y;
				computerPartImage.setLayoutParams(newParameters);


			}
			if(lessonTaught && nextLessonButton.getVisibility() == View.INVISIBLE){
				showNextLessonButton ();
			}

			return true;
		}



		private int computeX (int xTouchLocation){
			int xImagePosition;
			if(xTouchLocation<(computerPartImage.getWidth()/2)){
				xImagePosition = 0;
			}else if (xTouchLocation>(displaySize.x-(computerPartImage.getWidth()/2))){
				xImagePosition = (int) (displaySize.x-(computerPartImage.getWidth()));
			}else{
				xImagePosition = xTouchLocation - (computerPartImage.getWidth()/2);
			}

			return xImagePosition;
		}

		private int computeY (int yTouchLocation){
			int yImagePosition;
			if((yTouchLocation-statusbarHeight)<(computerPartImage.getHeight()/2)){
				yImagePosition = 0;
			}else if ((yTouchLocation-statusbarHeight)>(displaySize.y-statusbarHeight-(computerPartImage.getHeight()/2))){
				yImagePosition = (int) (displaySize.y-statusbarHeight)-(computerPartImage.getHeight());
			}else{
				yImagePosition = (yTouchLocation-statusbarHeight) - (computerPartImage.getHeight()/2);
			}

			return yImagePosition;

		}

		private boolean droppedOnSpeakButton (int cursor_x, int cursor_y){
			if(cursor_x>=speakButton.getX() && cursor_x<=(speakButton.getX()+speakButton.getWidth())){
				if(cursor_y>=(speakButton.getY()+statusbarHeight) && cursor_y<=(speakButton.getY()+speakButton.getHeight()+statusbarHeight)){
					return true;
				}
			}	
			return false;
		}

	}

	public void nextLesson(View view){
		myApp.incrementLessonStatus();
		if (mp_file.isPlaying()) mp_file.stop();
		if(myApp.getLessonStatus() == 3 | myApp.getLessonStatus() == 6){
			Intent i = new Intent(this, MenuActivity.class); 
			startActivity(i);
			this.finish();
		}
		else{
			Intent i = new Intent(this, LessonActivity.class); 
			startActivity(i);
			this.finish();
		}
	}
	private class Speaker implements OnClickListener {

		@Override
		public void onClick(View v) {
			mp_file.start();
		}


	}

}