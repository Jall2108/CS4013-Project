import java.util.*;

public class Hotel {
	
	public String name;
	public ArrayList<Room> rooms;
	
	public Hotel (String Name, ArrayList<Room> Rooms) {
		name = Name;
		rooms = Rooms;
	}
	
	public String toString() {
		return name;
	}
}
