package edu.uic.cs342.group4;

/**
 * Interface used for implementing the Observer design pattern
 * @see Observer
 */
public interface Subject {

	/**
	 * Add an Observer to this subject's list of listening observers
	 * @param observer Observer to add to list of listening observers
	 */
	void addObserver(Observer observer);

	/**
	 * Remove an Observer from this subject's list of listening observers
	 * @param observer Observer to remove from list of listening observers
	 */
	void removeObserver(Observer observer);

	/**
	 * Notify all listening observers of a change in state
	 */
	void notifyObservers();

}