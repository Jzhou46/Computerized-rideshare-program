package edu.uic.cs342.group4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Encapsulates carpool member management; singleton 
 */

public class CarPoolMemberManager implements Subject {

	CarPoolMember member;
	CarPoolMember newMember;

	/**
	 * Collection of all observing CarPoolManager objects
	 */
	private ArrayList<CarPoolManager> observers;

	/**
	 * Instance of this class, as the class is a singleton
	 */
	private static CarPoolMemberManager instance;

	/**
	 * Collection of all carpool members
	 */
	private ArrayList<CarPoolMember> poolMembers;

	/**
	 * CSV file containing all carpool member data
	 */
	final private String fileName = "members.csv";

	/**
	 * Constructs new CarPoolMemberManager instance
	 */
	private CarPoolMemberManager() {
		observers = new ArrayList<CarPoolManager>();
		poolMembers = new ArrayList<CarPoolMember>();
	}

	/**
	 * Add an observer to the list of Observers we will notify upon state change
	 * @param observer Observer to add
	 */
	public void addObserver(Observer observer) {
		observers.add((CarPoolManager)observer);
	}

	/**
	 * Remove an observer from the list of Observers we will notify upon state change
	 * @param observer Observer to remove
	 */
	public void removeObserver(Observer observer) {
		observers.remove((CarPoolManager)observer);
	}

	/**
	 * Notify all listening observers of state change
	 */
	public void notifyObservers() {
		int i = 0;

		for(i = 0; i < observers.size(); i++)
			observers.get(i).update();
	}

	/**
	 * Instantiates instance attribute if it is null, then returns it
	 * @return Instance of singleton CarPoolMemberManager class
	 */
	public static CarPoolMemberManager getInstance() {
		if(instance == null)
			instance = new CarPoolMemberManager();

		return instance;
	}

	/**
	 * Removes any unavailable dates from CarPoolMember objects in poolMembers list if the dates are in the past
	 */
	public void removePastUnavailableDates() {
		int i = 0;
		Calendar tomorrow = null;

		// Get today's date, rewind to midnight, increment to midnight tomorrow
		// NOTE: Java Calendar.set() method doesn't actually SET anything! You must call
		// a Calendar.get*() method after each set() for the change to take effect. Stupid.
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

		// For every member in the master list...
		for(i = 0; i < poolMembers.size(); i++) {
			int j = 0;

			ArrayList<Calendar> unavailableDates = poolMembers.get(i).unavailableDates();

			// If this member is unavailable for a date that has passed, remove that date
			// Otherwise, if the member is unavailable tomorrow, don't add him/her to a
			// sub-list, thus prevent them from getting assigned to a pool for tomorrow.	
			for(j = 0; j < unavailableDates.size(); j++) {
				if(unavailableDates.get(j).before(tomorrow)) {
					unavailableDates.remove(j);
					j--;
				}
			}
		}

		saveToFile();
	}

	/**
	 * Check if member exists
	 * @param id ID of member to check for
	 */
	public boolean memberExistsById(long id) {
		for(int i = 0; i < poolMembers.size(); i++) {
			if(poolMembers.get(i).id() == id)
				return true;
		}

		return false;
	}


	/**
	 * Adds a new carpool member to the poolMembers list as specified by the parameter; returns true on success, false on failure
	 * @param id ID of member to add
	 * @param firstName First name of member to add 
	 * @param lastName Last name of member to add
	 * @param address Address of member to add
	 * @param region Region of member to add
	 * @param vehicleCapacity Vehicle capacity of member to add
	 * @return True on success, false on failure
	 */
	public boolean addCarPoolMember(long id,
			String firstName,
			String lastName,
			String address,
			Region region,
			int vehicleCapacity) {
		boolean result = false;
		CarPoolMember newMember = new CarPoolMember(id, firstName, lastName, address, region, vehicleCapacity);

		result = poolMembers.add(newMember);

		saveToFile();

		notifyObservers();

		return result;
	}


	/**
	 * Removes an existing carpool member from the poolMembers list as specified by the parameter; returns true on success, false on failure
	 * @param id ID of member to remove
	 * @return True on success, false on failure
	 */
	public boolean removeCarPoolMember(long id) {
		for (int i = 0; i < poolMembers.size(); i++) {
			CarPoolMember member = poolMembers.get(i);

			if ( member.id() == id ) {
				poolMembers.remove(member);

				saveToFile();

				notifyObservers();

				return true;
			}
		}

		return false;
	}

	/**
	 * Locate member
	 * @param id ID of member to locate
	 * @return Member on success, null on failure
	 */
	private CarPoolMember findMember(long id) {
		for (int i = 0; i < poolMembers.size(); i++) {
			CarPoolMember member = poolMembers.get(i);

			if ( member.id() == id )
				return member;
		}

		return null;
	}

	/**
	 * Outputs member info to the console
	 * @param id ID of member to display
	 * @param scanner Open scanner object to write to
	 */
	public void displayMemberInfo(long id, Scanner scanner) {
		CarPoolMember member = findMember(id);

		if(member == null) {
			System.out.println(String.format("Error: Member with ID %d does not exist.", id));
		}
		else {
			System.out.println("Member Info:");
			System.out.println(String.format(" ID: %d", member.id()));
			System.out.println(String.format(" First name: %s", member.firstName()));
			System.out.println(String.format(" Last name: %s", member.lastName()));
			System.out.println(String.format(" Address: %s", member.address()));

			switch(member.region()) {
			case NORTH:
				System.out.println(" Region: North");
				break;

			case SOUTH:
				System.out.println(" Region: South");
				break;

			case EAST:
				System.out.println(" Region: East");
				break;

			case WEST:
				System.out.println(" Region: West");
				break;
			}

			System.out.println(String.format(" Vehicle capacity: %d", member.vehicleCapacity()));
		}
	}

	/**
	 * Updates first and last name attributes of CarPoolMember by ID, as specified by the parameters
	 * @param id ID of member to update
	 * @param firstName New first name
	 * @param lastName New last name
	 * @return True on success, false on failure
	 */
	public boolean updateName(long id, String firstName, String lastName) {
		boolean result = false;
		CarPoolMember member = findMember(id);

		if (member == null)
			return false;

		result = member.updateName(firstName, lastName);

		saveToFile();

		notifyObservers();

		return result;
	}

	/**
	 * Updates address and region attributes of CarPoolMember by ID, as specified by the parameters
	 * @param id ID of member to update
	 * @param address New address
	 * @param region New region
	 * @return True on success, false on failure
	 */
	public boolean updateAddress(long id, String address, Region region) {
		boolean result = false;
		CarPoolMember member = findMember(id);

		if (member == null)
			return false;

		result = member.updateAddress(address, region);

		saveToFile();

		notifyObservers();

		return result;
	}

	/**
	 * Updates vehicle capacity attribute of CarPoolMember by ID, as specified by the parameters
	 * @param id ID of member to update
	 * @param newVehicleCapacity New capacity of member's vehicle
	 * @return True on success, false on failure
	 */
	public boolean updateVehicleCapacity(long id, int newVehicleCapacity) {
		boolean result = false;
		CarPoolMember member = findMember(id);

		if (member == null)
			return false;

		result =  member.updateVehicleCapacity(newVehicleCapacity);

		saveToFile();

		notifyObservers();

		return result;
	}

	/**
	 * Adds a date the CarPoolMember is unavailable to drive by ID, as specified by the parameters
	 * @param id ID of member to update
	 * @param cal Unavailable date
	 * @return True on success, false on failure
	 */
	public boolean addUnavailableDate(long id, Calendar cal) {		
		for (int i = 0; i < poolMembers.size(); i++) {
			CarPoolMember member = poolMembers.get(i);

			if ( member.id() == id ) {
				member.unavailableDates().add(cal);

				saveToFile();

				notifyObservers();

				return true;
			}
		}

		return false;
	}

	/**
	 * Corrects an unscheduled absence by removing latest date driven from the CarPoolMember with id
	 * specified by id_absent and adding that date to the CarPoolMember with id specified by id_substitute.
	 * @param id_absent ID of member who did not drive
	 * @param id_substitute ID of member who did drive
	 * @return True on success, false on failure
	 */
	public boolean correctUnscheduledAbsence(long id_absent, long id_substitute) {
		int array_size = 0;
		CarPoolMember member_absent = null, member_sub = null;

		// find both members
		for (int i = 0; i < poolMembers.size(); i++) {
			CarPoolMember temp = poolMembers.get(i);

			if ( temp.id() == id_absent )
				member_absent = temp;
			else if ( temp.id() == id_substitute )
				member_sub = temp;
		}

		// if either member doesn't exist or members are the same, return false
		if(member_absent == null || member_sub == null || member_absent == member_sub)
			return false;

		array_size = member_absent.datesLastDrove().size();

		if(array_size >= 1)
		{
			member_sub.datesLastDrove().add(member_absent.datesLastDrove().remove(array_size - 1));

			saveToFile();

			notifyObservers();

			return true;
		}

		return false;
	}


	/**
	 * Load previously saved car pool members' data from a file
	 * @return true on success, false on failure
	 */
	public boolean loadFromFile() {
		FileInputStream fis = null;
		BufferedReader br = null;
		String line;

		poolMembers.clear();

		// Open a file input stream and a buffered reader
		try {
			fis = new FileInputStream(fileName);
			br = new BufferedReader(new InputStreamReader(fis));
		}
		catch (FileNotFoundException e) {
			return false;
		}

		try {
			// Read until the end of the file

			// Member info is contained in three consecutive lines
			// The first line contains ID, first name, last name, address,
			// region and vehicle capacity. Parse those...
			while ((line = br.readLine()) != null)   {
				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				CarPoolMember member = null;

				member = new CarPoolMember(Integer.parseInt(tokens[0]),
						tokens[1].replaceAll("^\"|\"$", ""),
						tokens[2].replaceAll("^\"|\"$", ""),
						tokens[3].replaceAll("^\"|\"$", ""),
						Region.values()[Integer.parseInt(tokens[4])],
						Integer.parseInt(tokens[5]));

				// The second line contains the list of dates the member has driven.
				// Parse those...
				line = br.readLine();
				tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				for(String t : tokens) {		
					if(t.length() == 0)
						continue;

					Calendar cal = Calendar.getInstance();

					cal.setTimeInMillis(Long.parseLong(t));

					member.datesLastDrove().add(cal);
				}

				// The third line contains the list of dates the member is unavailable.
				// Parse those...
				line = br.readLine();
				tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				for(String t : tokens) {
					if(t.length() == 0)
						continue;

					Calendar cal = Calendar.getInstance();

					cal.setTimeInMillis(Long.parseLong(t));

					member.unavailableDates().add(cal);
				}

				poolMembers.add(member);
			}
		} catch (IOException e) {
			// ignore
		}

		try {
			br.close();
		} catch (IOException e) {
			// ignore
		}

		notifyObservers();

		return true;
	}

	/**
	 * Save current car pool members' data to a file
	 * @return true on success, false on failure
	 */
	public boolean saveToFile() {
		FileOutputStream fos = null;

		// Open a new file output stream
		try {
			fos = new FileOutputStream(fileName, false);
		}
		catch (FileNotFoundException e) {
			return false;
		}

		// Member info will be contained in three consecutive lines
		for (int i = 0; i < poolMembers.size(); i++) {
			int j = 0;
			String line;
			CarPoolMember member = poolMembers.get(i);

			try {

				// The first line will contain ID, first name, last name, address,
				// region and vehicle capacity. Parse those...
				line = String.format("%d,\"%s\",\"%s\",\"%s\",%d,%d\n",
						member.id(), 
						member.firstName(), 
						member.lastName(), 
						member.address(), 
						member.region().ordinal(), 
						member.vehicleCapacity());

				fos.write(line.getBytes());
				fos.flush();

				// The second line will contain the list of dates the member has driven.
				// Write those...
				for(j = 0; j < member.datesLastDrove().size(); j++) {
					line = String.format("%d%s", member.datesLastDrove().get(j).getTimeInMillis(), ((j+1) < member.datesLastDrove().size()) ? "," : "");
					fos.write(line.getBytes());
					fos.flush();
				}

				line = String.format("\n");
				fos.write(line.getBytes());
				fos.flush();

				// The third line will contain the list of dates the member is unavailable.
				// Write those...
				for(j = 0; j < member.unavailableDates().size(); j++) {
					line = String.format("%d%s", member.unavailableDates().get(j).getTimeInMillis(), ((j+1) < member.unavailableDates().size()) ? "," : "");
					fos.write(line.getBytes());
					fos.flush();
				}

				line = String.format("\n");
				fos.write(line.getBytes());
				fos.flush();

			} catch (IOException e) {
				// ignore
			}		
		}

		if(fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				// Ignore
			}
		}

		return true;
	}

	/**
	 * Getter
	 * @return poolMembers
	 */
	public ArrayList<CarPoolMember> poolMembers() {
		return poolMembers;
	}

}