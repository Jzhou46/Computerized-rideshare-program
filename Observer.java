package edu.uic.cs342.group4;

/**
 * Interface used for implementing the Observer design pattern
 * @see Subject
 */
public interface Observer {

	/**
	 * Method to be called by Subject to notify Observer
	 * of a change in state
	 */
	void update();
}