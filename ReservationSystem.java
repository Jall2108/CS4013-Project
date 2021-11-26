import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReservationSystem {

	final LocalDate errorDate = LocalDate.MIN;
	
	ArrayList<Reservation> reservations;
	ArrayList<Hotel> hotels;
	Scanner in;
	
	/***
	 * Creates new reservation system and sets up available rooms and hotels.
	 */
	public ReservationSystem() throws IOException {
		reservations = new ArrayList<Reservation>();
		in = new Scanner(System.in);
		hotels = readHotels();
	}
	
	/***
	 * Starts a new reservation system.
	 */
	public void start() {
		while (true) {
			System.out.println("Welcome to the L4 Hotels Reservation System!");
			System.out.println("To reserve a room, enter 'R'."
					+ "\nTo view existing reservations, enter V."
					+ "\nTo cancel a reservation, enter 'C'."
					+ "\nTo quit this session, enter 'Q'.");
			String input = in.nextLine().toUpperCase();
			
			switch (input) {
				case "R":
					System.out.println("Please select a hotel by entering the appropiate letter.");
					Hotel h = (Hotel) DisplaySystem.choices(hotels, in, true);
					if (h == null) break;
					else {
						System.out.println("Hotel: " + h.name);
						System.out.println("Please select a room by entering the appropiate letter.");
						Room r = (Room) DisplaySystem.choices(h.rooms, in, true);
						if (r == null) break;
						else {
							System.out.println("Room: " + r.roomType);
							System.out.println("Please enter your preferred check in date.");
							LocalDate checkInDate = inputDate();
							if (checkInDate != errorDate) {
								System.out.println("Please enter your preferred check out date.");
								LocalDate checkOutDate = inputDate();
								if (checkOutDate != errorDate && checkOutDate.isAfter(checkInDate)) {
									Reservation R = new Reservation(checkInDate, checkOutDate, h, r);
									if(isReserved(R)) {
										System.out.println("Unfortunately, this room is booked out during this time. Please try again at a different time.");
									}
									else {
										System.out.println("There is room for your reservation. Your reservation amounts to €" + reservationCost(R) + " for your stay of " + ChronoUnit.DAYS.between(checkInDate, checkOutDate) + " days.");
										System.out.println("Enter 'Y' if this is acceptable, or 'N' if unacceptable.");
										if (in.nextLine().charAt(0) == 'Y') {
											reserve(R);
										}
										System.out.println();
									}
								}
							}
						}
					}
					break;
				case "Q":
					System.out.println("Thank you for using the L4 Hotels Reservation System!");
					in.close();
					return;
			}
		}
	}
	
	
	/***
	 * Calculates the cost of staying for a reservation.
	 */
	public int reservationCost(Reservation r) {
		int day = r.CheckInDate.getDayOfWeek().getValue() - 1; //value in range [1, 7] adjusted to value in range[0, 6].
		long daysBetween = ChronoUnit.DAYS.between(r.CheckInDate, r.CheckOutDate);
		int cost = 0;
		for (int i = 0; i < daysBetween; i++) {
			cost += r.room.weekRates[(day + i) % 7];
		}
		return cost;
	}
	
	/***
	 * Logs a new reservation if it doesn't clash with a pre-existing reservation.
	 * @param r is the new reservation.
	 */
	public void reserve(Reservation r) {
		reservations.add(r);
		System.out.println("Thank you for making a reservation. We hope you enjoy your stay with L4 Hotels.");
	}	
	
	/***
	 * Handles the processing of date inputs.
	 * @return the date asked for, given the input was successful. Returns Date(0, 0, 0) on failure.
	 */
	public LocalDate inputDate() {
		System.out.println("Please enter the date in the format DD/MM/YYYY.");
		String dateInfo = in.nextLine();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate date = LocalDate.parse(dateInfo, formatter);
		if (date.isBefore(LocalDate.now())) {
			System.out.println("That date has already passed. Please try again.");
			return errorDate;
		}
		return date;
	}
	
	
	/***
	 * Determines if a new reservation can fit in the existing schedule.
	 * @param r is the new reservation.
	 * @return True if the space is already reserved, false if it is free.
	 */
	public boolean isReserved(Reservation r) {
		if (reservations.size() == 0) return false;
		int count = 1; //count of concurrent room usages.
		for (int i = 0; i < reservations.size(); i++) {
			if (r.room.roomType == reservations.get(i).room.roomType) {
				if ((r.CheckInDate.isAfter(reservations.get(i).CheckInDate) || r.CheckInDate.isEqual(reservations.get(i).CheckInDate))
						&& r.CheckInDate.isBefore(reservations.get(i).CheckOutDate)) {
					count++;
				}
				else if ((r.CheckOutDate.isAfter(reservations.get(i).CheckInDate)
						&& (r.CheckOutDate.isBefore(reservations.get(i).CheckOutDate) || r.CheckOutDate.isEqual(reservations.get(i).CheckOutDate)))) {
					count++;
				}
			}
		}
		return (count > r.room.numberOfRooms) ? true : false;
	}
	
	/***
	 * Reads l4Hotels.csv and converts each line to a new hotel.
	 * @return arraylist of hotels.
	 * @throws IOException if file not found.
	 * @supressWarnings is used to supress the warning of the casting of Rooms.clone() to type ArrayList<Room>.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Hotel> readHotels () throws IOException {
		File f = new File("src/l4Hotels.csv");
		Scanner reader = new Scanner(f);
		ArrayList<String> lines = new ArrayList<String>();
		while (reader.hasNextLine()) lines.add(reader.nextLine());
		//hotelType declared separately since it holds over from prior lines
		String HotelType = "";
		ArrayList<Room> Rooms = new ArrayList<Room>();
		ArrayList<Hotel> Hotels = new ArrayList<Hotel>();
		for (int i = 0; i < lines.size(); i++) {
			//if a line doesn't contain any digit it doesn't represent a hotel room
			if (lines.get(i).matches(".*\\d")) {
				String[] clauses = lines.get(i).split(",");
				//save hotel data and get information for the next hotel
				if (clauses[0].matches(".*\\S.*")) {
					if (HotelType != "") Hotels.add(new Hotel(HotelType, (ArrayList<Room>) Rooms.clone()));
					Rooms.clear();
					HotelType = clauses[0];
				}
				int[] rates = new int[7];
				for (int j = 0; j < rates.length; j++) {
					rates[j] = Integer.parseInt(clauses[5 + j]);
				}
				Rooms.add(new Room(HotelType, clauses[1], Integer.parseInt(clauses[2]), Integer.parseInt(clauses[3]), Integer.parseInt(clauses[4]), rates));
			}
			if (i + 1 == lines.size()) Hotels.add(new Hotel(HotelType, Rooms));
		}
		reader.close();
		return Hotels;
	}
}
