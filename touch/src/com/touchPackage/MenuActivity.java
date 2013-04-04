package com.touchPackage;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.app.Activity;
import android.content.Intent;


/**
 * 
 * This activity creates a menu. This menu has the two lesson options:
 * internal and external computer parts. The user picks which lesson
 * he would like to do first. After completing the first set of lessons, the user 
 * returns to this activity to select the other lesson. 
 *
 */
public class MenuActivity extends Activity {
	private App myApp;
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity);
		myApp = (App)getApplicationContext();
		
    	final CheckBox checkBox1 = (CheckBox) findViewById(R.id.startlesson1);
    	final CheckBox checkBox2 = (CheckBox) findViewById(R.id.startlesson2);
    	
    	//Check boxes checked if player has already completed the lesson
    	
    	if (myApp.isLesson1complete() == true) {
            checkBox1.setChecked(true);
        }
    	if (myApp.isLesson2complete() == true) {
            checkBox2.setChecked(true);
        }
	
    }//onCreate()
	
	/**
	 * 
	 * @param view
	 * Calls the Lesson Activity to start the first set of lessons
	 */
	public void launchLesson1(View view){
		Intent i = new Intent(this, LessonActivity.class); 
		myApp.setLessonStatus(0);//start with the first lesson set - internal parts
		startActivity(i);
		this.finish();
	}//launchLesson1()
	
	/**
	 * @param view
	 * Calls the Lesson Activity to start the second set of lessons
	 */
	public void launchLesson2(View view){
		Intent i = new Intent(this, LessonActivity.class); 
		myApp.setLessonStatus(3);//start with the second lesson set - external parts
		startActivity(i);
		this.finish();
	}//launchLesson2()
	
	/**
	 * 
	 * @param view
	 * After the two lessons are completed, starts the Quiz Activity so the user
	 * can test their knowledge. 
	 */
	public void launchQuiz(View view){
		Intent i = new Intent(this, QuizActivity.class); 
		startActivity(i);
		this.finish();
	}//launchQuiz()
}//MenuActivity
