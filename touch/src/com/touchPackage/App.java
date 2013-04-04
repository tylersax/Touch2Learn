package com.touchPackage;

import android.app.Application;

/**
 * 
 * Allows the program to constantly access information about the user, 
 * such as his name and score.
 *
 */
public class App extends Application{
	
	private String global_name; //user's name
	private int global_score; //user's score
	private int global_lessonStatus; //which lessons the user has completed, from 0-6
	private int quizStatus; //which quizzes the user has completed, from 0-1
	private boolean lesson1complete = false; //whether lesson1 is completed - initially it is not
	private boolean lesson2complete = false;//whether lesson2 is completed - initially it is not
	
	@Override
    public void onCreate() {
        global_name = "my friend";
        global_lessonStatus = 0; //initially, not on any lessons
		quizStatus = 0; //initially, haven't taken the quiz
		global_score = 0; 
        
        super.onCreate();
    }
    
	/**
	 * 
	 * @return user's name
	 */
    public String getName() {
		return global_name;
	}//getName()
	
    /**
     * @param in_name sets the user's name
     */
	public void setName(String in_name) {
		global_name = in_name;
	}//getName()
	
	/**
	 * 
	 * @return user's score
	 */
	public int getScore() {
		return global_score;
	}//getScore()
	
	/**
	 * 
	 * @param score sets the user's score
	 */
	public void incrementScore(int score) {
		global_score += score;
	}//incrementScore()
	
	/**
	 * 
	 * @return which lesson the use is currently on, between 0 and 6
	 */
	public int getLessonStatus() {
		return global_lessonStatus;
	}//getLessonStatus()
	
	/**
	 * increases global_lessonStatus by 1 when the lesson changes
	 */
	public void incrementLessonStatus() {
		global_lessonStatus++;
	}//getLessonStatus()
	
	/**
	 * 
	 * @param status which lesson the user is on
	 */
	public void setLessonStatus(int status) {
		global_lessonStatus = status;
	}//getLessonStatus()
	
	/**
	 * @return whether the quiz has been completed or not
	 */
	public int getQuizStatus() {
		return quizStatus;
	}//getQuizStatus()
	
	/**
	 * 
	 * @param value sets the boolean to true or false, depends on if the quiz was completed
	 */
	public void setQuizStatus(int value) {
		quizStatus= value;
	}//getQuizStatus()
	
	/**
	 * gets called when the first lesson is created
	 */
	public void lesson1completed() {
		lesson1complete = true;
	}//lesson1completed()
	
	/**
	 * gets called when the second lesson is created
	 */
	public void lesson2completed() {
		lesson2complete = true;
	}//lesson2completed()
	
	/**
	 * 
	 * @return boolean telling if lesson1 was completed or not
	 */
	public boolean isLesson1complete() {
		if(lesson1complete == true) return true;
		else return false;	
	}//isLesson1complete()
	
	/**
	 * 
	 * @return boolean telling if lesson2 was completed or not
	 */
	public boolean isLesson2complete() {
		if(lesson2complete == true) return true;
		else return false;
	}//isLesson2complete()

}
