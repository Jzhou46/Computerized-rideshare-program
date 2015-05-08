package edu.uic.cs342.group4;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Comparator used by Collections.sort(). Used for sorting car pool members.
 */
public class CarPoolMemberComparator implements Comparator<CarPoolMember> {

	/**
	 * Comparator used by Collections.sort(). Used for sorting car pool members.
	 * Primary sort by vehicle capacity, ascending; secondary sort by date last drove, ascending;
	 * tertiary sort by number of times driven, ascending.
	 * @param m1 First member to compare
	 * @param m2 Second member to compare
	 */
	public int compare(CarPoolMember m1, CarPoolMember m2) {
		// Primary sort: vehicle capacity, ascending
		if(m1.vehicleCapacity() > m2.vehicleCapacity()) {
			return 1;
		}
		else if(m1.vehicleCapacity() == m2.vehicleCapacity()) {
			// Vehicle capacities equal. Secondary sort: date last drove, ascending
			if(m1.datesLastDrove().size() == 0)
				return -1;
			else if(m2.datesLastDrove().size() == 0)
				return 1;
			else
			{   
				Calendar m1Date = m1.datesLastDrove().get(m1.datesLastDrove().size()-1);
				Calendar m2Date = m2.datesLastDrove().get(m2.datesLastDrove().size()-1);

				if(m2Date.before(m1Date)) {
					return 1;
				}
				else if(m2Date.equals(m1Date)) {
					// Date last drove equal. Tertiary sort: number of times driven, ascending
					if(m1.datesLastDrove().size() > m2.datesLastDrove().size()) {
						return 1;
					}
					else if(m1.datesLastDrove().size() == m2.datesLastDrove().size()) {
						return 0;
					}
					else {
						return -1;
					}
				}
				else {
					return -1;
				}
			}
		}
		else {
			return -1;
		}
	}
}