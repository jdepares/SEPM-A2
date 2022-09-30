import java.io.FileWriter;
import java.io.IOException;

class User {

	protected boolean CheckPassword(String password, String userInput) {
		return password.equals(userInput)  ? true : false;
	}

	protected boolean NewUser(boolean staff, String firstName, String lastName, String email, String password,
			String phone) {
		return NewUser(staff, 0, firstName, lastName, email, password, phone);
	}

	protected boolean NewUser(boolean staff, int techLevel, String firstName, String lastName, String email,
			String password, String phone) {

		String dir = staff ? "./src/Files/Staff.txt" : "./src/Files/Technicians.txt";
		String content = firstName + "," + lastName + "," + email + "," + password + "," + phone;
		content = staff ? content : techLevel + "," + content;

		try {
			FileWriter myWriter = new FileWriter(dir, true);
			myWriter.write(content + "\n");
			myWriter.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}
}