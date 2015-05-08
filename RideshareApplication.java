package edu.uic.cs342.group4;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Main client class.  Contains entry point to the application.
 */
public class RideshareApplication {
	/**
	 * Instance of CarPoolMemberManager class, which handles the management of all carpool members in the system.
	 */
	private static CarPoolMemberManager carPoolMemberManager;
	/**
	 * Instance of CarPoolManager class, which handles the management of all carpools in the system
	 */
	private static CarPoolManager carPoolManager;

	/**
	 * Displays Main Menu and prompt
	 */
	private static void displayMainMenu() {
		System.out.println("|--------------------------------------------------------|");
		System.out.println("|             RideshareApplication: Main Menu            |");
		System.out.println("|--------------------------------------------------------|");
		System.out.println("|  1) Add a member to RideshareApplication               |");
		System.out.println("|  2) Remove a member from RideshareApplication          |");
		System.out.println("|  3) Display member info                                |");
		System.out.println("|  4) Update member attributes                           |");
		System.out.println("|  5) Mark a member as unavailable for a future date     |");
		System.out.println("|  6) Correct an unscheduled absence                     |");
		System.out.println("|  7) Get tomorrow's carpool information                 |");
		System.out.println("|  8) Exit RideshareApplication                          |");
		System.out.println("|--------------------------------------------------------|");
		System.out.print("Please enter a number from the menu above: ");
	}

	/**
	 * Displays Edit Member Attributes Menu and prompt
	 */
	private static void displayEditMemberAttributesMenu() {
		System.out.println("|--------------------------------------------------------|");
		System.out.println("|   RideshareApplication: Edit Member Attributes Menu    |");
		System.out.println("|--------------------------------------------------------|");
		System.out.println("|  1) Edit member's name                                 |");
		System.out.println("|  2) Edit member's address                              |");
		System.out.println("|  3) Edit member's vehicle capacity                     |");
		System.out.println("|  4) Return to main menu                                |");
		System.out.println("|--------------------------------------------------------|");
		System.out.print("Please enter a number from the menu above: ");
	}

	/**
	 * Displays prompt and waits for any key to be pressed
	 */
	private static void pressAnyKeyToContinue(Scanner scanner)
	{ 
		System.out.println("Press any key to continue...");

		try {
			scanner.nextLine();
		}  
		catch(Exception e) {
			// Ignore
		}  
	}

	/**
	 * Displays Select Member Region Menu and prompt
	 */
	private static void displaySelectMemberRegionMenu() {
		System.out.println("Enter member's region:");
		System.out.println(" 1) North");
		System.out.println(" 2) South");
		System.out.println(" 3) East");
		System.out.println(" 4) West");
		System.out.print("Please enter a number from the list above: ");
	}

	/**
	 * Displays Select Member Region Menu and prompt and retrieves/processes user input
	 */
	private static Region doSelectMemberRegionMenu(Scanner scanner) {
		int input;

		while (true) {
			displaySelectMemberRegionMenu();

			try {
				input = Integer.parseInt(scanner.nextLine());

				switch (input) {
				case 1:
					return Region.NORTH;

				case 2:
					return Region.SOUTH;

				case 3:
					return Region.EAST;

				case 4:
					return Region.WEST;

				default:
					System.out.println();
					System.out.println("Invalid input. Please try again.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Invalid input. Please try again.");
				continue;
			}
		}
	}

	/**
	 * Displays Edit Member Attributes Menu and prompt and retrieves/processes user input
	 */
	private static void doEditMemberAttributesMenu(Scanner scanner) {
		boolean done = false, success = true;
		int vehicleCapacity = 0;
		Region region;
		long id = 0;
		String firstName, lastName, address;
		int input = 0;

		while (!done) {
			displayEditMemberAttributesMenu();

			try {
				// Get the option
				input = Integer.parseInt(scanner.nextLine());

				System.out.println();

				// Switch on option
				switch (input) {
				// Case 1: Edit member's name
				case 1:
					System.out.print("Enter member's ID: ");

					try {
						id = Long.parseLong(scanner.nextLine());

						// If member doesn't exist, say so and bail
						if(!carPoolMemberManager.memberExistsById(id)) {
							System.out.println();
							System.out.println(String.format("Error: Member with ID %d does not exist.", id));
						}
						else {
							// Prompt for and collect the updated info
							System.out.print("Enter new first name: ");
							firstName = scanner.nextLine();
	
							System.out.print("Enter new last name: ");
							lastName = scanner.nextLine();
	
							// Store the updated info
							success = carPoolMemberManager.updateName(id, firstName, lastName);
	
							// Notify the user of the result
							if (success == true) {
								System.out.println();
								System.out.println("Member's name updated successfully!");
								carPoolMemberManager.displayMemberInfo(id, scanner);
							} else {
								System.out.println();
								System.out.println("Failed to update member's name!");
							}
						}
					} catch (NumberFormatException e) {
						System.out.println();
						System.out.println("Invalid input. Please try again.");
					} finally {
						System.out.println();
						pressAnyKeyToContinue(scanner);
					}
					break;

					// Case 2: Edit member's address/region
				case 2:
					System.out.print("Enter member's ID: ");

					try {
						id = Long.parseLong(scanner.nextLine());

						// If member doesn't exist, say so and bail
						if(!carPoolMemberManager.memberExistsById(id)) {
							System.out.println();
							System.out.println(String.format("Error: Member with ID %d does not exist.", id));
						}
						else {
							// Prompt for and collect the updated info
							System.out.print("Enter new address: ");
							address = scanner.nextLine();
	
							region = doSelectMemberRegionMenu(scanner);
	
							// Store the updated info
							success = carPoolMemberManager.updateAddress(id, address, region);
	
							// Notify the user of the result
							if (success == true) {
								System.out.println();
								System.out.println("Member's address updated successfully!");
								carPoolMemberManager.displayMemberInfo(id, scanner);
							} else {
								System.out.println();
								System.out.println("Failed to update member's address!");
							}
						}
					} catch (NumberFormatException e) {
						System.out.println();
						System.out.println("Invalid input. Please try again.");
					} finally {
						System.out.println();
						pressAnyKeyToContinue(scanner);
					}
					break;

					// Case 3: Edit member's vehicle capacity
				case 3:
					System.out.print("Enter member's ID: ");

					try {
						id = Long.parseLong(scanner.nextLine());

						// If member doesn't exist, say so and bail
						if(!carPoolMemberManager.memberExistsById(id)) {
							System.out.println();
							System.out.println(String.format("Error: Member with ID %d does not exist.", id));
						}
						else {
							// Prompt for and collect the updated info
							while(true)
							{
								System.out.print("Enter new vehicle capacity: ");
								vehicleCapacity = Integer.parseInt(scanner.nextLine());
	
								// Validate vehicle capacity
								if(vehicleCapacity < 2) {
									System.out.println();
									System.out.println("Invalid vehicle capacity. Please try again.");
								}
								else {
									break;
								}
							}
	
							// Store the updated info
							success = carPoolMemberManager.updateVehicleCapacity(id, vehicleCapacity);
	
							// Notify the user of the result
							if (success == true) {
								System.out.println();
								System.out.println("Member's vehicle capacity updated successfully!");
								carPoolMemberManager.displayMemberInfo(id, scanner);
							}
							else {
								System.out.println();
								System.out.println("Failed to update member's vehicle capacity!");
							}
						}
					} catch (NumberFormatException e) {
						System.out.println();
						System.out.println("Invalid input. Please try again.");
					} finally {
						System.out.println();
						pressAnyKeyToContinue(scanner);
					}
					break;

				case 4:
					done = true;
					break;

				default:
					System.out.println("Invalid input. Please try again.\n");
					pressAnyKeyToContinue(scanner);
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Invalid input. Please try again.\n");
				pressAnyKeyToContinue(scanner);
			}
		}
	}

	/**
	 * Prompts and retrieves/processes user input needed to add a member
	 */
	private static void addMember(Scanner scanner) {
		boolean success = true;
		int vehicleCapacity = 0;
		Region region;
		long id = 0;
		String firstName, lastName, address;

		System.out.print("Enter member's ID: ");

		try {
			id = Long.parseLong(scanner.nextLine());

			// If member already exists, say so and bail
			if(carPoolMemberManager.memberExistsById(id)) {
				System.out.println();
				System.out.println(String.format("Error: Member already exists with ID %d.", id));
			}
			else {
				// Prompt for and collect the new member's info
				System.out.print("Enter member's first name: ");
				firstName = scanner.nextLine();
	
				System.out.print("Enter member's last name: ");
				lastName = scanner.nextLine();
	
				System.out.print("Enter member's address: ");
				address = scanner.nextLine();
	
				region = doSelectMemberRegionMenu(scanner);
	
				while(true)
				{
					System.out.print("Enter member's vehicle capacity: ");
					vehicleCapacity = Integer.parseInt(scanner.nextLine());
	
					// Validate vehicle capacity
					if(vehicleCapacity < 2) {
						System.out.println();
						System.out.println("Invalid vehicle capacity. Please try again.");
					}
					else {
						break;
					}
				}
	
				// Try to add the new member
				success = carPoolMemberManager.addCarPoolMember(id, firstName, lastName, address, region, vehicleCapacity);
	
				// Notify the user of the result
				if (success == true) {
					System.out.println();
					System.out.println("Member added successfully!");
					carPoolMemberManager.displayMemberInfo(id, scanner);
				}
				else {
					System.out.println();
					System.out.println("Failed to add member!");
				}
			}
		} catch (NumberFormatException e) {
			System.out.println();
			System.out.println("Invalid input. Please try again.");
		}

		System.out.println();
	}

	/**
	 * Prompts and retrieves/processes user input needed to remove a member
	 */
	private static void removeMember(Scanner scanner) {
		boolean success = true;
		long id = 0;

		System.out.print("Enter ID of member to remove: ");

		try {
			id = Long.parseLong(scanner.nextLine());
			
			// If member doesn't exist, say so and bail
			if(!carPoolMemberManager.memberExistsById(id)) {
				System.out.println();
				System.out.println(String.format("Error: Member with ID %d does not exist.", id));
			}
			else {
				// Try to remove the member
				success = carPoolMemberManager.removeCarPoolMember(id);
	
				// Notify the user of the result
				if (success == true) {
					System.out.println();
					System.out.println("Member removed successfully!");
				}
				else {
					System.out.println();
					System.out.println("Failed to remove member!");
				}
			}
		} catch (NumberFormatException e) {
			System.out.println();
			System.out.println("Invalid input. Please try again.");
		} finally {
			System.out.println();
		}
	}

	/**
	 * Prompts and retrieves/processes user input needed to add an unavailable date for a member
	 */
	private static void addUnavailableDate(Scanner scanner) {
		boolean success = true;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		long id = 0;

		System.out.print("Enter member's ID: ");

		try {
			id = Long.parseLong(scanner.nextLine());

			// If member doesn't exist, say so and bail
			if(!carPoolMemberManager.memberExistsById(id)) {
				System.out.println();
				System.out.println(String.format("Error: Member with ID %d does not exist.", id));
			}
			else {
				// Prompt for and collect the date
				System.out.print("Enter date (MM/DD/YYYY): ");
	
				cal.setTime(formatter.parse(scanner.nextLine()));
	
				// Try to add the unavailable date
				success = carPoolMemberManager.addUnavailableDate(id, cal);
	
				// Notify the user of the result
				if (success == true) {
					System.out.println();
					System.out.println("Date added successfully!");
				}
				else {
					System.out.println();
					System.out.println("Failed to add date!");
				}
			}
		} catch (NumberFormatException | ParseException e) {
			System.out.println();
			System.out.println("Invalid input. Please try again.");
		} finally {
			System.out.println();
		}
	}

	/**
	 * Prompts and retrieves/processes user input needed to correct an unscheduled absence
	 */
	private static void doCorrectUnscheduledAbsence(Scanner scanner) {
		boolean success = true;
		long id_absent = 0, id_substitute = 0;

		try {
			System.out.print("Enter the ID of the absent driver: ");

			id_absent = Long.parseLong(scanner.nextLine());

			// If absent driver doesn't exist, say so and bail
			if(!carPoolMemberManager.memberExistsById(id_absent)) {
				System.out.println();
				System.out.println(String.format("Error: Member with ID %d does not exist.", id_absent));
			}
			else {
				System.out.print("Enter the ID of the substitute driver: ");
	
				id_substitute = Long.parseLong(scanner.nextLine());
	
				// If substitute driver doesn't exist, say so and bail
				if(!carPoolMemberManager.memberExistsById(id_substitute))
				{
					System.out.println();
					System.out.println(String.format("Error: Member with ID %d does not exist.", id_substitute));
					return;
				}
	
				// Try to correct the unscheduled absence
				success = carPoolMemberManager.correctUnscheduledAbsence(id_absent, id_substitute);
	
				// Notify the user of the result
				if (success == true) {
					System.out.println();
					System.out.println("Unscheduled absence corrected successfully!");
				}
				else {
					System.out.println();
					System.out.println("Failed to correct unscheduled absense!");
				}
			}
		} catch (NumberFormatException e) {
			System.out.println();
			System.out.println("Invalid input. Please try again.");
		} finally {
			System.out.println();
		}
	}

	private static void displayMemberInfo(Scanner scanner) {
		long id = 0;

		try {
			System.out.print("Enter the member's ID: ");

			id = Long.parseLong(scanner.nextLine());

			System.out.println();

			carPoolMemberManager.displayMemberInfo(id, scanner);

		} catch (NumberFormatException e) {
			System.out.println();
			System.out.println("Invalid input. Please try again.");
		}

		System.out.println();
	}

	/**
	 * Displays Main Menu and prompt
	 */
	private static void doMainMenu(Scanner scanner) {
		boolean done = false;
		int input = 0;

		while (!done) {

			displayMainMenu();

			try {
				input = Integer.parseInt(scanner.nextLine());

				System.out.println();

				// Switch on user input
				switch (input) {
				// Case 1: Add member
				case 1:
					addMember(scanner);
					pressAnyKeyToContinue(scanner);
					break;

					// Case 2: Remove member
				case 2:
					removeMember(scanner);
					pressAnyKeyToContinue(scanner);
					break;

					// Case 3: Display member info
				case 3:
					displayMemberInfo(scanner);
					pressAnyKeyToContinue(scanner);
					break;

					// Case 4: Edit member attributes
				case 4:
					doEditMemberAttributesMenu(scanner);
					break;

					// Case 5: Add unavailable date
				case 5:
					addUnavailableDate(scanner);
					pressAnyKeyToContinue(scanner);
					break;

					// Case 6: Correct an unscheduled absence
				case 6:
					doCorrectUnscheduledAbsence(scanner);
					pressAnyKeyToContinue(scanner);
					break;

					// Case 7: Save tomorrow's carpool data to a file
				case 7:
					saveTomorrowsCarPoolData();
					pressAnyKeyToContinue(scanner);
					break;

					// Case 8: Exit the application
				case 8:
					done = true;
					break;

				default:
					System.out.println("Invalid input. Please try again.\n");
					pressAnyKeyToContinue(scanner);
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Invalid input. Please try again.\n");
				pressAnyKeyToContinue(scanner);
			}
		}
		System.out.println("RideshareApplication exiting..");
	}

	/*-----------------------------------------------------------------------------------
	 *                                      MAIN
	 *---------------------------------------------------------------------------------*/
	/**
	 * Entry point for application, receives an array of strings representing
	 * the command line arguments to the application
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		carPoolMemberManager = CarPoolMemberManager.getInstance();
		carPoolManager = CarPoolManager.getInstance();

		carPoolMemberManager.loadFromFile();

		doMainMenu(scanner);
	}

	/**
	 * Formats and saves tomorrow's carpool data to a text file.
	 */

	public static void saveTomorrowsCarPoolData() {
		String fileName, line;
		FileOutputStream fos = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar tomorrow = null;

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

		fileName = String.format("carpools_%s.txt", dateFormat.format(tomorrow.getTime()));

		try {
			fos = new FileOutputStream(fileName, false);
		} catch (FileNotFoundException e) {
			// ignore
		}

		line = String.format("Date: %s\n\n", dateFormat.format(tomorrow.getTime()));
		try {
			fos.write(line.getBytes());
			fos.flush();
		} catch (IOException e) {
			// ignore
		}

		// Create new optimized carpools
		carPoolManager.update();

		// Reset our iterator
		carPoolManager.reset();

		while (carPoolManager.hasNext()) {
			int i = 0;
			CarPool pool = carPoolManager.next();

			try {
				line = String.format("Carpool %d:\n", pool.id());
				fos.write(line.getBytes());
				fos.flush();

				// First member of each pool is the driver.
				// Update driver's number of times drove attribute.
				pool.poolMembers().get(0).datesLastDrove().add(tomorrow);
				carPoolMemberManager.saveToFile();

				line = String.format("Driver: %s %s\nPassenger(s): \n",
						pool.poolMembers().get(0).firstName(),
						pool.poolMembers().get(0).lastName());

				fos.write(line.getBytes());
				fos.flush();

				if (pool.poolMembers().size() <= 1) {
					line = String.format("        <<NONE>>\n\n");
					fos.write(line.getBytes());
					fos.flush();
				} else {
					for (i = 1; i < pool.poolMembers().size(); i++) {
						line = String.format("        %s %s\n        %s\n\n",
								pool.poolMembers().get(i).firstName(),
								pool.poolMembers().get(i).lastName(),
								pool.poolMembers().get(i).address());

						fos.write(line.getBytes());
						fos.flush();
					}
				}
			} catch (IOException e) {
				// ignore
			}

		}

		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				// Ignore
			}
		}

		System.out.println(String.format("Tomorrow's carpool information saved to %s\n", fileName));
	}
}