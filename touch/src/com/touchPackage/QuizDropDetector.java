package com.touchPackage;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

/**
 * Gets the coordinates of the positon of the cursor and checks
 * if the image was placed in the correct spot during the quiz.
 *
 */
public class QuizDropDetector {

	private View quizView; //the view with the quiz
	private Point displaySize; 
	private float displayDP; 

	public QuizDropDetector (View view, Point displaySize, DisplayMetrics displayMetric){
		quizView=view;
		this.displaySize= displaySize;
		this.displayDP = displayMetric.density;
		Log.d("displayDP", String.valueOf(displayDP));
	}

	/**
	 * checks to see if the image was dropped in the correct location
	 * 
	 * @param x image x coordinate
	 * @param y image y coordinate 
	 * @param part image being dragged
	 * @return whether was dropped on the correct spot or not
	 */
	public Boolean droppenOn (int x, int y, String part){
		if(cpuIsOnPlace(x,y)){
			if(part.equals("cpu")){
				return true;
			}
		}
		if(powerIsOnPlace(x,y)){
			if(part.equals("plug")){
				return true;
			}
		}
		if(webcamIsOnPlace(x,y)){
			if(part.equals("camera")){
				return true;
			}
		}
		if(speakersIsOnPlace(x,y)){
			if(part.equals("speakers")){
				return true;
			}
		}
		if(microphoneIsOnPlace(x,y)){
			if(part.equals("microphone")){
				return true;
			}
		}
		if(earIsOnPlace(x,y)){
			if(part.equals("ears")){
				return true;
			}
		}
		if(eyesIsOnPlace(x,y)){
			if(part.equals("eyes")){
				return true;
			}
		}
		if(mouthIsOnPlace(x,y)){
			if(part.equals("mouth")){
				return true;
			}
		}
		if(busIsOnPlace(x,y)){
			if(part.equals("bus")){
				return true;
			}
		}
		return false;
	}

	/**
	 * checks is cpu was placed in the correct spot
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @return whether was placed correctly
	 */
	private boolean cpuIsOnPlace(int x, int y){
		if(x>=brain_x_start*displayDP && x<=brain_x_end*displayDP){
			if(y>= brain_y_start*displayDP + getRelativeTop() && y<= brain_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//cpuIsOnPlace
	/**
	 * checks whether the power supply was placed correctly
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return whether was placed correctly
	 */
	private boolean powerIsOnPlace(int x, int y){
		if(x>=heart_x_start*displayDP && x<=heart_x_end*displayDP){
			if(y>= heart_y_start*displayDP + getRelativeTop() && y<= heart_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//powerIsOnPlace
	/**
	 * checks whether the bus was placed correctly 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return whether was placed correctly
	 */
	private boolean busIsOnPlace(int x, int y){
		if(x>=nerves_x_start*displayDP && x<=nerves_x_end*displayDP){
			if(y>= nerves_y_start*displayDP + getRelativeTop() && y<= nerves_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//busIsOnPlace
	/**
	 * checks whether the webcam was placed correctly
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return whether was placed correctly 
	 */
	private boolean webcamIsOnPlace(int x, int y){
		if(x>=eyes_x_start*displayDP && x<=eyes_x_end*displayDP){
			if(y>= eyes_y_start*displayDP + getRelativeTop() && y<= eyes_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//webcameIsOnPlace
	/**
	 * checks whether the speakers were placed correctly 
	 * @param x x coordinate
	 * @param y y-coordinate
	 * @return whether was placed correctly
	 */
	private boolean speakersIsOnPlace(int x, int y){
		if(x>=mouth_x_start*displayDP && x<=mouth_x_end*displayDP){
			if(y>= mouth_y_start*displayDP + getRelativeTop() && y<= mouth_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//speakersIsOnPlace
	/**
	 * checks whether the microphone was placed correctly 
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return whether was placed correctly
	 */
	private boolean microphoneIsOnPlace(int x, int y){
		if(x>=leftEar_x_start*displayDP && x<=leftEar_x_end*displayDP){
			if(y>= leftEar_y_start*displayDP + getRelativeTop() && y<= leftEar_y_end*displayDP + getRelativeTop()){
				return true;
			}
		} else if(x>=rightEar_x_start*displayDP && x<=rightEar_x_end*displayDP){
			if(y>= rightEar_y_start*displayDP + getRelativeTop() && y<= rightEar_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//microphoneIsOnPlace
	/**
	 * check whether the ear was placed correctly
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @return whether was placed correctly 
	 */
	private boolean earIsOnPlace(int x, int y){
		if(x>=microphone_x_start*displayDP && x<=microphone_x_end*displayDP){
			if(y>= microphone_y_start*displayDP + getRelativeTop() && y<= microphone_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//earIsOnPlace
	/**
	 * checks whether the eyes were placed correctly
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return whether was placed correctly 
	 */
	private boolean eyesIsOnPlace(int x, int y){
		if(x>=webcam_x_start*displayDP && x<=webcam_x_end*displayDP){
			if(y>= webcam_y_start*displayDP + getRelativeTop() && y<= webcam_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//eyesIsOnPlace
	/**
	 * checks whether the mouth was placed correctly
	 * @param x x coordinate
	 * @param y y coordinate 
	 * @return whether was placed correctly 
	 */
	private boolean mouthIsOnPlace(int x, int y){
		if(x>=speakerLeft_x_start*displayDP && x<=speakerLeft_x_end*displayDP){
			if(y>= speakerLeft_y_start*displayDP + getRelativeTop() && y<= speakerLeft_y_end*displayDP + getRelativeTop()){
				return true;
			}
		} else if(x>=speakerRight_x_start*displayDP && x<=speakerRight_x_end*displayDP){
			if(y>= speakerRight_y_start*displayDP + getRelativeTop() && y<= speakerRight_y_end*displayDP + getRelativeTop()){
				return true;
			}
		}
		return false;
	}//mouthIsOnPlace

	/**
	 * retrieves the locations to correctly assess relative distance 
	 * @return the correct relative value
	 */
	public int getRelativeTop (){
		return (int) quizView.findViewById(R.id.dropOnImage).getY()+ (int) displaySize.y - quizView.getHeight();
	}//getRelativeTop

	//correct placement values
	final static int brain_x_start = 100;
	final static int brain_x_end =230;
	final static int brain_y_start= 5;
	final static int brain_y_end = 90;
	final static int heart_x_start = 170;
	final static int heart_x_end = 210;
	final static int heart_y_start = 150;
	final static int heart_y_end = 210;
	final static int nerves_x_start = 60;
	final static int nerves_x_end = 275;
	final static int nerves_y_start = 25;
	final static int nerves_y_end = 370;
	final static int eyes_x_start = 150;
	final static int eyes_x_end = 180;
	final static int eyes_y_start = 70;
	final static int eyes_y_end = 100;
	final static int mouth_x_start = 125;
	final static int mouth_x_end = 210;
	final static int mouth_y_start = 100;
	final static int mouth_y_end = 135;
	final static int leftEar_x_start = 90;
	final static int leftEar_x_end = 130;
	final static int leftEar_y_start = 85;
	final static int leftEar_y_end = 125;
	final static int rightEar_x_start = 200;
	final static int rightEar_x_end = 240;
	final static int rightEar_y_start = 70;
	final static int rightEar_y_end = 110;
	final static int webcam_x_start = 130;
	final static int webcam_x_end =170;
	final static int webcam_y_start= 105;
	final static int webcam_y_end = 145;
	final static int microphone_x_start = 190;
	final static int microphone_x_end = 270;
	final static int microphone_y_start = 270;
	final static int microphone_y_end = 345;
	final static int speakerLeft_x_start = 15;
	final static int speakerLeft_x_end = 75;
	final static int speakerLeft_y_start = 165;
	final static int speakerLeft_y_end = 240;
	final static int speakerRight_x_start = 220;
	final static int speakerRight_x_end = 290;
	final static int speakerRight_y_start = 215;
	final static int speakerRight_y_end = 290;
}
