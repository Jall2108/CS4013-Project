import java.time.LocalDate;

public class Reservation {

	public LocalDate CheckInDate;
	public LocalDate CheckOutDate;
	public Hotel hotel;
	public Room room;
	
	public Reservation(LocalDate checkIn, LocalDate checkOut, Hotel h, Room r) {
		CheckInDate = checkIn;
		CheckOutDate = checkOut;
		hotel = h;
		room = r;
	}
	
	public boolean isValid() {
		if (CheckInDate.isAfter(CheckOutDate)) return false;
		else return true;
	}
}
