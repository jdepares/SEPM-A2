import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

abstract class User {

	protected String firstName;
	protected String lastName;

	protected String email;
	protected String password;

	protected String phone;

	protected boolean isLoggedIn;

	protected boolean CheckPassword(String password, String userInput) {
		return password.equals(userInput) ? true : false;
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
			BufferedWriter bw = new BufferedWriter(myWriter);
			myWriter.write(content + "\n");
			myWriter.close();
			bw.append(content);
			bw.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}

	protected abstract void ChangePassword(String email, String newPassword) throws IOException, FileNotFoundException;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

}
