package edu.uic.cs342.group4;

import java.util.ArrayList;

/**
 * Represents an individual carpool, i.e., a group of ridesharing
 * program members who will share a ride on a given day
 */
public class CarPool {

	/**
	 * Unique identifier of carpool
	 */
	private int id;

	/**
	 * Capacity of vehicle with lowest capacity in this carpool
	 */
	private int minVehicleCapacity;

	/**
	 * Collection of all members of a carpool
	 */
	private ArrayList<CarPoolMember> poolMembers;

	/**
	 * Constructs a new CarPool instance
	 * @param id Unique identifier of carpool
	 * @param minVehicleCapacity Capacity of vehicle with lowest capacity in this carpool
	 */
	public CarPool(int id, int minVehicleCapacity) {
		poolMembers = new ArrayList<CarPoolMember>();

		this.id = id;
		this.minVehicleCapacity = minVehicleCapacity;
	}

	/**
	 * Appends newMember to a CarPool returns 
	 * @param newMember Member to add to carpool
	 * @return true on success, false on failure
	 */
	public boolean addMember(CarPoolMember newMember) {
		return poolMembers.add(newMember);
	}

	/**
	 * Getter
	 * @return id
	 */
	public int id() {
		return id;
	}

	/**
	 * Getter
	 * @return minVehicleCapacity
	 */
	public int minVehicleCapacity() {
		return minVehicleCapacity;
	}

	/**
	 * Getter
	 * @return poolMembers
	 */
	public ArrayList<CarPoolMember> poolMembers() {
		return poolMembers;
	}

}