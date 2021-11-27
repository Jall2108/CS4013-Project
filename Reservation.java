import java.time.LocalDate;

public class Reservation {

	public int number;
	public String name;
	public LocalDate CheckInDate;
	public LocalDate CheckOutDate;
	public String type;
	public Hotel hotel;
	public Room room;
	
	public Reservation(String Name, LocalDate checkIn, LocalDate checkOut, String Type, Hotel h, Room r) {
		name = Name;
		CheckInDate = checkIn;
		CheckOutDate = checkOut;
		hotel = h;
		room = r;
		if (Type.equals("AP")) type = Type;
		else type = "S";
		//make number
	}
	
	public boolean isValid() {
		if (CheckInDate.isAfter(CheckOutDate)) return false;
		else return true;
	}
	
	public String toString() {
		return type + ", in " + hotel.toString() + ", room " + room.roomType;
	}
}
