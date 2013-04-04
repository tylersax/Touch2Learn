package com.touchPackage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
public class QuizActivity extends Activity {

	private RelativeLayout quizLayout; 
	private QuizDropDetector dropDetector;

	private Button speakButton; //speaks the text 
	private Button nextQuizButton; //brings the user to the next quiz

	private ImageView part1; //first quiz draggable image
	private ImageView part2; //second quiz draggable image
	private ImageView part3; //third quiz draggable image
	private ImageView dropOnImage; //image that gets little pictures dropped on - either the body or the computer 
	private Intent i;
	private App myApp;
	private MediaPlayer mp_file;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (App)getApplicationContext();
		
		//shows the quiz and sets QuizStatus to 0 because it was not taken
		if (myApp.getQuizStatus() == 0){
			setContentView(R.layout.quiz1);
			quizLayout = (RelativeLayout) this.findViewById(R.id.quizLayout);
		}
		
		else{
			setContentView(R.layout.quiz2);
			quizLayout = (RelativeLayout) this.findViewById(R.id.quizLayout);
		}
		
		part1 = (ImageView) this.findViewById(R.id.partImage1);
		part2 = (ImageView) this.findViewById(R.id.partImage2);
		part3 = (ImageView) this.findViewById(R.id.partImage3);
		
		/* gets the display proprieties. they are needed to compute correctely all the animations */
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetric = new DisplayMetrics(); 
		Point displaySize = new Point();
		display.getSize(displaySize);
		display.getMetrics(displayMetric);
		dropDetector = new QuizDropDetector(quizLayout, displaySize, displayMetric);

		dropOnImage = (ImageView) this.findViewById(R.id.dropOnImage);
		
		part1.setOnTouchListener( new Dragger(part1,displaySize));
		part2.setOnTouchListener( new Dragger(part2,displaySize));
		part3.setOnTouchListener( new Dragger(part3,displaySize));

		speakButton = (Button) this.findViewById(R.id.speakButton);
		speakButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Drop something on me!\n"+myApp.getScore(), Toast.LENGTH_SHORT).show();
			}
		});

		nextQuizButton = (Button) this.findViewById(R.id.nextQuizButton);
		
		//takes the user to the second quiz or sets the quiz to finished 
		nextQuizButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mp_file.isPlaying()) mp_file.stop();
				if(myApp.getQuizStatus() == 1){
					myApp.setQuizStatus(0);
					i = new Intent(QuizActivity.this, ShareScores.class); 
					startActivity(i);
					QuizActivity.this.finish();
				}
				else {
					myApp.setQuizStatus(1);
					i = new Intent(QuizActivity.this, QuizActivity.class); 
					startActivity(i);
					QuizActivity.this.finish();
				}
			}
		});
	}

   /* Dragger
    * this is a custom on touch listener that is used by the computer/body parts image that the user has to
    * drag during the quiz
    */ 
	private class Dragger implements OnTouchListener {

		private int attemptsCounter;//how many time the user drags the image until they put it in the right location
		private int status;
		private LayoutParams newParameters;
		private Point displaySize;
		private ImageView part; //image being dragged
		int statusbarHeight; //height of the status bar
		private int original_x; //original x coordinate of image
		private int original_y; //original y coordinate of image

		public Dragger(ImageView part, Point displaySize){
            this.part=part;
			this.displaySize= displaySize;
			this.attemptsCounter = 1;
     	}

		//deal with the touch events of the user
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			status = event.getAction();
			
			// calculates the height of the status bar. note that it has to be done 
			// here instead of doing it in the constructor because lessonLayout doesn't get
			// set up until onCreate finish to execute 
			statusbarHeight = displaySize.y - quizLayout.getHeight();
			
			if(status==MotionEvent.ACTION_DOWN){		//when the drag action starts 

				//saves the original position of the part is being dragged 
				original_x= (int) part.getX();
				original_y= (int) part.getY();
				newParameters = new LayoutParams (part.getHeight(),part.getWidth());

				// if the user starts dragging the bus part, the images changes to show the nervous system
				// instead of the standard body image
				if(part.getContentDescription().equals("bus")){
					transitionNervousSystem(true);
				}

			}else if( status== MotionEvent.ACTION_MOVE){ //while the drag action is happening

				// current_x/current_y is where the computer/body part is dropped
				// original_x/original_y is where the computer part should go
				newParameters.leftMargin=computeX((int)event.getRawX());
				newParameters.topMargin=computeY((int)event.getRawY());
				part.setLayoutParams(newParameters);

			}else if (status == MotionEvent.ACTION_UP){		//when the computer part is relased 
			
				// current_x/current_y is where the computer part is dropped
				int current_x,current_y;
				current_x=(int) computeX((int)event.getRawX());
				current_y=(int) computeY((int)event.getRawY());

				// drop detector is called to test if the image is droppe in the right location;
				// if so, it hides the part and replace it with a nice checkmark. the score of
				// the user is also updated. 
				if(dropDetector.droppenOn((int) event.getRawX(), (int) event.getRawY(),part.getContentDescription().toString())){
					hidePart(part);
					newParameters.leftMargin = original_x;
					newParameters.topMargin = original_y;
					part.setLayoutParams(newParameters);
					showCheckmark(part);
					setScore();
					
				}else{
					// if the image is insted dorppod on the speak button, the lesson is speaked;
					// otherwise the attempts counter is updated to decrease the score.
					if(speakIfDroppedOnSpeakButton((int)event.getRawX(), (int)event.getRawY(), part)==false){
						attemptsCounter++;
					}
					putPartBack((current_x-original_x),0, current_y-original_y, 0);
				}
				
				//if the user was dragging the bus icon, the body is reverted to the initial image
				if(part.getContentDescription().equals("bus")){
					transitionNervousSystem(false);
				}
			}
           return true;
		}//onTouch

		/**
		 * determines the score depending on how many attempts it took to 
		 * place the image correctly
		 */
		private void setScore (){
			if (attemptsCounter==1){
				myApp.incrementScore(100);
			}else if (attemptsCounter==2){
				myApp.incrementScore(50);
				
			}else if (attemptsCounter==3){
				myApp.incrementScore(25);
			}
		}//setScore

		/**
		 * if showNervousSystem is true it shows the nervous syste instead of the standard body image
		 * if it is set to false, it reverts back to the original image
		 * a nice fade transition is computed to please the user
		 * 
		 * @param showNervousSystem
		 */
		private void transitionNervousSystem (boolean showNervousSystem){

			if(showNervousSystem){
				Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
				fadeOut.setInterpolator(new DecelerateInterpolator());
				fadeOut.setFillAfter(true);
				fadeOut.setDuration(500);
				dropOnImage.startAnimation(fadeOut);	
				dropOnImage.setImageResource(R.drawable.nerves);
				Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
				fadeIn.setInterpolator(new DecelerateInterpolator());
				fadeIn.setFillAfter(true);
				fadeIn.setDuration(500);
				dropOnImage.startAnimation(fadeIn);
				
			}else {
				Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
				fadeOut.setInterpolator(new DecelerateInterpolator());
				fadeOut.setFillAfter(true);
				fadeOut.setDuration(500);
				dropOnImage.startAnimation(fadeOut);	
				dropOnImage.setImageResource(R.drawable.body);
				Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
				fadeIn.setInterpolator(new DecelerateInterpolator());
				fadeIn.setFillAfter(true);
				fadeIn.setDuration(500);
				dropOnImage.startAnimation(fadeIn);
			}

		}//transitionNervousSystem

		/**
		 * calculates the offset between the touch imput and the actual position of the image
		 * based on the height of the status bar ahd the size of the computer part image 
		 *
		 * @param xTouchLocation
		 * @return
		 */
		private int computeX (int xTouchLocation){
			int xImagePosition;
			if(xTouchLocation<(part.getWidth()/2)){
				xImagePosition = 0;
			}else if (xTouchLocation>(displaySize.x-(part.getWidth()/2))){
				xImagePosition = (int) (displaySize.x-(part.getWidth()));
			}else{
				xImagePosition = xTouchLocation - (part.getWidth()/2);
			}

			return xImagePosition;
		}//computeX

		/**
		 * calculates the offset between the touch imput and the actual position of the image
		 * based on the height of the status bar ahd the size of the computer part image 
		 * 
		 * @param yTouchLocation
		 * @return
		 */
		private int computeY (int yTouchLocation){
			int yImagePosition;
			if((yTouchLocation-statusbarHeight)<(part.getHeight()/2)){
				yImagePosition = 0;
			}else if (yTouchLocation>(displaySize.y- statusbarHeight -(part.getHeight()/2))){
				yImagePosition = (int) (displaySize.y-statusbarHeight)-(part.getHeight());
			}else{
				yImagePosition = (yTouchLocation-statusbarHeight) - (part.getHeight()/2);
			}

			return yImagePosition;
		}//computeY
		
		/**
		 * fades out image if it was placed in the correct location
		 * @param part image that was dragged
		 */
		private void hidePart(ImageView part){
			Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
			fadeOut.setInterpolator(new DecelerateInterpolator());
			fadeOut.setFillAfter(true);
			fadeOut.setDuration(500);
			part.startAnimation(fadeOut);
		}//hidePart

		/**
		 * Shows checkmark in place of image if image was placed in the correct place
		 * @param part image being placed
		 */
		private void showCheckmark (ImageView part){
			part.setImageResource(R.drawable.checkmark);
			Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
			fadeIn.setInterpolator(new DecelerateInterpolator());
			fadeIn.setFillAfter(true);
			fadeIn.setDuration(500);
			part.startAnimation(fadeIn);
			part.setEnabled(false);
		} //showCheckmark

		/**
		 * 
		 * speaks the corrispond lesson to the part dropped on the speak button
		 * 
		 * @param cursor_x x coordinate of cursos
		 * @param cursor_y y coordinate of cursor
		 * @param part image being dragged
		 * @return true if in the correct spot 
		 */
		private boolean speakIfDroppedOnSpeakButton(int cursor_x, int cursor_y, ImageView part){

			if(cursor_x>=speakButton.getX() && cursor_x<=(speakButton.getX()+speakButton.getWidth())){
				if(cursor_y>=(speakButton.getY()+statusbarHeight) && cursor_y<=(speakButton.getY()+speakButton.getHeight()+statusbarHeight)){
					//TODO: play audio for specific image
					if (mp_file.isPlaying()) mp_file.stop();
					if (myApp.getQuizStatus() == 0){
						if (part == part1){
							mp_file = MediaPlayer.create(QuizActivity.this, R.raw.lesson1audio);
							mp_file.start();
						}
						else if (part == part2){
							mp_file = MediaPlayer.create(QuizActivity.this, R.raw.lesson2audio);
							mp_file.start();
						}
						else if (part == part3){
							mp_file = MediaPlayer.create(QuizActivity.this, R.raw.lesson3audio);
							mp_file.start();
						}
					}
					else if (myApp.getQuizStatus() == 1){
						if (part == part1){
							mp_file = MediaPlayer.create(QuizActivity.this, R.raw.lesson4audio);
							mp_file.start();
						}
						else if (part == part2){
							mp_file = MediaPlayer.create(QuizActivity.this, R.raw.lesson6audio);
							mp_file.start();
						}
						else if (part == part3){
							mp_file = MediaPlayer.create(QuizActivity.this, R.raw.lesson5audio);
							mp_file.start();
						}
					}
					
					return true;
				}
			}
			return false;
			
		}//speakIfDroppedOnSpeakButton

		/* putComputerPartBack
		* 
		* moves the part back to its original location
		* the parameters for the translation need to be passed to the method because 
		* the computer part moves back to different locations if it has been dragged 
		* on the speak button or not
		* 
		* @param fromXDelta 
		* @param toXDelta 
		* @param fromYDelta 
		* @param toYDelta 
		*/
		private void putPartBack(int fromXDelta, int toXDelta, int fromYDelta, int toYDelta){

			TranslateAnimation putBack = new TranslateAnimation (
					Animation.ABSOLUTE, fromXDelta,
					Animation.ABSOLUTE, toXDelta,
					Animation.ABSOLUTE, fromYDelta,
					Animation.ABSOLUTE, toYDelta);
			putBack.setDuration(700);
			part.startAnimation(putBack);


			newParameters.leftMargin = original_x;
			newParameters.topMargin = original_y;
			part.setLayoutParams(newParameters);
		}//putPartBack

	}

}