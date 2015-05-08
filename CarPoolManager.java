package edu.uic.cs342.group4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * Encapsulates the management of carpools; singleton
 */
public class CarPoolManager implements Observer, Iterator {

	/**
	 * Instance of this class, as this class is a singleton
	 */
	private static CarPoolManager instance;

	/**
	 * Collection of all carpools
	 */
	private ArrayList<CarPool> pools;

	/**
	 * Denotes current element; used for iterating through pools
	 */
	private int curElement;

	/**
	 * Constructs a new CarPoolManager instance
	 */
	private CarPoolManager() {
		pools = new ArrayList<CarPool>();

		// Reset our iterator
		this.reset(); 

		// Add ourself as an observer of CarPoolMemberManager
		CarPoolMemberManager.getInstance().addObserver(this);
	}

	/**
	 * Instantiates instance attribute if it is null, then returns it
	 * @return Instance of singleton CarPoolManager class
	 */
	public static CarPoolManager getInstance() {
		if (instance == null)
			instance = new CarPoolManager();

		return instance;
	}

	/**
	 * Resets iterator to first element of pools
	 */
	public void reset() {
		curElement = -1;
	}

	/**
	 * Determine whether there is another element in pools to iterate to
	 * @return true if another element exists, false if end of pools has been reached
	 */
	public boolean hasNext() {
		if (curElement + 1 < pools.size())
			return true;
		else
			return false;
	}

	/**
	 * Retrieves the next CarPool in pools.
	 * @return the next CarPool in pools if one exists, null otherwise
	 */
	public CarPool next() {
		if (curElement + 1 >= pools.size())
			return null;
		else
			return pools.get(++curElement);
	}

	/**
	 * Creates a new car pool arrangement based on the latest data
	 * in CarPoolMemberManager class.
	 */
	public void update() {
		int i = 0, id = 1;
		Calendar tomorrow = null;
		CarPoolMemberComparator cpmComparator = new CarPoolMemberComparator();
		CarPoolMemberManager carPoolMemberManager = CarPoolMemberManager.getInstance();
		ArrayList<CarPoolMember> members = carPoolMemberManager.poolMembers();
		ArrayList<CarPoolMember> members_north = new ArrayList<CarPoolMember>();
		ArrayList<CarPoolMember> members_south = new ArrayList<CarPoolMember>();
		ArrayList<CarPoolMember> members_east = new ArrayList<CarPoolMember>();
		ArrayList<CarPoolMember> members_west = new ArrayList<CarPoolMember>();

		// Tell CarPoolMemberManager to prune any unavailable dates that have passed
		carPoolMemberManager.removePastUnavailableDates();

		// Get today's date, rewind to midnight, increment to midnight tomorrow
		// NOTE: Java Calendar.set() method doesn't actually SET anything! You
		// must call
		// a Calendar.get*() method after each set() for the change to take
		// effect. Stupid.
		tomorrow = Calendar.getInstance();
		tomorrow.set(Calendar.HOUR_OF_DAY, 0);
		tomorrow.getTimeInMillis();
		tomorrow.set(Calendar.MINUTE, 0);
		tomorrow.getTimeInMillis();
		tomorrow.set(Calendar.SECOND, 0);
		tomorrow.getTimeInMillis();
		tomorrow.set(Calendar.MILLISECOND, 0);
		tomorrow.getTimeInMillis();
		tomorrow.add(Calendar.DATE, 1);

		pools.clear();

		// For every member in the master list...
		for (i = 0; i < members.size(); i++) {
			int j = 0;
			boolean unavailable = false;

			ArrayList<Calendar> unavailableDates = members.get(i)
					.unavailableDates();

			// If this member is unavailable for tomorrow, don't add him/her to a
			// sub-list, thus prevent them from getting assigned to a pool for tomorrow
			for (j = 0; j < unavailableDates.size(); j++) {
				if (unavailableDates.get(j).equals(tomorrow)) {
					unavailable = true;
					break;
				}
			}

			if (unavailable)
				continue;

			// Add the member to the appropriate sub-list, based on the member's region
			switch (members.get(i).region()) {
			case NORTH:
				members_north.add(members.get(i));
				break;

			case SOUTH:
				members_south.add(members.get(i));
				break;

			case EAST:
				members_east.add(members.get(i));
				break;

			case WEST:
				members_west.add(members.get(i));
				break;
			}
		}

		// Sort north region sublist
		Collections.sort(members_north, cpmComparator);

		// Divide north region members into carpools
		for (i = 0; i < members_north.size(); i++) {
			int j = 0;
			CarPool new_pool = null;
			int pool_size = members_north.get(i).vehicleCapacity();

			if (members_north.size() - i < pool_size)
				pool_size = (members_north.size() - i);

			new_pool = new CarPool(id++, pool_size);

			for (j = 0; j < pool_size; j++) {
				new_pool.addMember(members_north.get(i + j));
			}

			i += (j - 1);

			pools.add(new_pool);
		}

		// Sort south region sublist
		Collections.sort(members_south, cpmComparator);

		// Divide south region members into carpools
		for (i = 0; i < members_south.size(); i++) {
			int j = 0;
			CarPool new_pool = null;
			int pool_size = members_south.get(i).vehicleCapacity();

			if (members_south.size() - i < pool_size)
				pool_size = (members_south.size() - i);

			new_pool = new CarPool(id++, pool_size);

			for (j = 0; j < pool_size; j++) {
				new_pool.addMember(members_south.get(i + j));
			}

			i += (j - 1);

			pools.add(new_pool);
		}

		// Sort east region sublist
		Collections.sort(members_east, cpmComparator);

		// Divide east region members into carpools
		for (i = 0; i < members_east.size(); i++) {
			int j = 0;
			CarPool new_pool = null;
			int pool_size = members_east.get(i).vehicleCapacity();

			if (members_east.size() - i  < pool_size)
				pool_size = (members_east.size() - i);

			new_pool = new CarPool(id++, pool_size);

			for (j = 0; j < pool_size; j++) {
				new_pool.addMember(members_east.get(i + j));
			}

			i += (j - 1);

			pools.add(new_pool);
		}

		// Sort west region sublist
		Collections.sort(members_west, cpmComparator);

		// Divide west region members into carpools
		for (i = 0; i < members_west.size(); i++) {
			int j = 0;
			CarPool new_pool = null;
			int pool_size = members_west.get(i).vehicleCapacity();

			if (members_west.size() - i < pool_size)
				pool_size = (members_west.size() - i);

			new_pool = new CarPool(id++, pool_size);

			for (j = 0; j < pool_size; j++) {
				new_pool.addMember(members_west.get(i + j));
			}

			i += (j - 1);

			pools.add(new_pool);
		}
	}

}