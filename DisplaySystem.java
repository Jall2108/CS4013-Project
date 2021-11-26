import java.util.*;

public class DisplaySystem {

	/***
	 * From a given list and Scanner, accept an input and return the object.
	 * @param <T> is the type of the list.
	 * @param list is the list of objects to be outputted.
	 * @param in is the Scanner to read the next input line.
	 * @param allowBack gives a return functionality to the user in case they wish to exist a process early.
	 * @return An object from the given list according to user input.
	 */
	public static <T> Object choices(ArrayList<T> list, Scanner in, boolean allowBack) {
		char c = 'A';
		for (Object choice : list) {
			System.out.println(c + ") " + choice.toString());
			c++;
		}
		if (allowBack) {
			System.out.println(c + ") Return.");
		}
		char s = in.nextLine().toUpperCase().charAt(0);
		if (s - 'A' == list.size()) return null;
		else return list.get(s - 'A');
	}
}
