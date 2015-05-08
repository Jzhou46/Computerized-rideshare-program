package edu.uic.cs342.group4;

import java.util.ArrayList;

/**
 * Encapsulates the management of a car pool member
 */
public class CarPoolMember {

	/**
	 * Unique identifier of a carpool member
	 */
	private long id;

	/**
	 * First name of carpool member
	 */
	private String firstName;

	/**
	 * Last name (surname) of carpool member
	 */
	private String lastName;

	/**
	 * Street address of carpool member
	 */
	private String address;

	/**
	 * Region with respect to campus where carpool member resides
	 */
	private Region region;

	/**
	 * Capacity (number of seats) in carpool member’s vehicle
	 */
	private int vehicleCapacity;

	/**
	 * List of dates this carpool member most recently drove
	 */
	private ArrayList<java.util.Calendar> datesLastDrove;

	/**
	 * Collection of all upcoming dates this carpool member is unavailable to drive
	 */
	private ArrayList<java.util.Calendar> unavailableDates;

	/**
	 * Constructs new CarPoolMember object instance
	 * @param id Unique identifier of a carpool member
	 * @param firstName First name of carpool member
	 * @param lastName Last name (surname) of carpool member
	 * @param address Street address of carpool member
	 * @param region Region with respect to campus where carpool member resides
	 * @param vehicleCapacity Capacity (number of seats) in carpool member’s vehicle
	 */
	public CarPoolMember(long id,
			String firstName,
			String lastName,
			String address,
			Region region,
			int vehicleCapacity) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.region = region;
		this.vehicleCapacity = vehicleCapacity;
		this.datesLastDrove = new ArrayList<java.util.Calendar>();
		this.unavailableDates = new ArrayList<java.util.Calendar>();
	}

	/**
	 * Locates member by ID and if successful, updates first name and last name of the member as specified by the parameters; 
	 * returns true on success, false on failure
	 * @param firstName New first name
	 * @param lastName New last name
	 */
	public boolean updateName(String firstName, String lastName) {

		this.firstName = firstName;
		this.lastName = lastName;

		return true;
	}

	/**
	 * Locates member by ID and if successful, updates address and region as specified by the parameters; 
	 * returns true on success, false on failure
	 * @param address New address
	 * @param region New region
	 */
	public boolean updateAddress(String address, Region region) {
		this.address = address;
		this.region = region;

		return true;
	}

	/** 
	 * Locates member by ID and if successful, updates vehicle capacity as specified by the parameter; 
	 * returns true on success, false on failure
	 * @param newVehicleCapacity New vehicle capacity
	 */
	public boolean updateVehicleCapacity(int newVehicleCapacity) {
		this.vehicleCapacity = newVehicleCapacity;

		return true;
	}


	/*   GETTERS   */

	/**
	 * Getter
	 * @return id
	 */
	public long id() {
		return id;
	}

	/**
	 * Getter
	 * @return firstName
	 */
	public String firstName() {
		return firstName;
	}

	/**
	 * Getter
	 * @return lastName
	 */
	public String lastName() {
		return lastName;
	}

	/**
	 * Getter
	 * @return address
	 */
	public String address() {
		return address;
	}

	/**
	 * Getter
	 * @return region
	 */
	public Region region() {
		return region;
	}

	/**
	 * Getter
	 * @return vehicleCapacity
	 */
	public int vehicleCapacity() {
		return vehicleCapacity;
	}

	/**
	 * Getter
	 * @return datesLastDrove
	 */
	public ArrayList<java.util.Calendar> datesLastDrove() {
		return datesLastDrove;
	}

	/**
	 * Getter
	 * @return unavailableDates
	 */
	public ArrayList<java.util.Calendar> unavailableDates() {
		return unavailableDates;
	}

}