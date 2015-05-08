package edu.uic.cs342.group4;

/**
 * Interface used for implementing the Iterator design pattern
 */
public interface Iterator {

	/**
	 * Resets iterator to first element of collection
	 */
	void reset();

	/**
	 * Determine whether there is another element in collection to iterate to
	 * @return true if another element exists, false if end of pools has been reached
	 */
	boolean hasNext();

	/**
	 * Retrieves the next object in the collection
	 * @return the next Object in collection if one exists, null otherwise
	 */
	Object next();

}