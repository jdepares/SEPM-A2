import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SEPMA2 {

	public List<Staff> staff = new ArrayList<>();
	public List<Technician> technician = new ArrayList<>();
	Scanner sc = new Scanner(System.in);

	public SEPMA2() {

		// Import data from text files
		startUp();

		// Remove before submit, used to test
		for (int i = 0; i < this.staff.size(); i++) {
			System.out.println(this.staff.get(i).getFirstName());
			System.out.println(this.staff.get(i).getLastName());
			System.out.println(this.staff.get(i).getEmail());
			System.out.println(this.staff.get(i).getPassword());
			System.out.println(this.staff.get(i).getPhone());
			System.out.println("------------");
		}

		for (int i = 0; i < this.technician.size(); i++) {
			System.out.println(this.technician.get(i).getLevel());
			System.out.println(this.technician.get(i).getFirstName());
			System.out.println(this.technician.get(i).getLastName());
			System.out.println(this.technician.get(i).getEmail());
			System.out.println(this.technician.get(i).getPassword());
			System.out.println(this.technician.get(i).getPhone());
			System.out.println("------------");
		}

		mainMenu();

	}

	public void mainMenu() {
		System.out.println("--------------------------");
		System.out.println("Welcome");
		System.out.println("--------------------------");
		System.out.println("1. Login");
		System.out.println("2. Logout");
		System.out.println("3. Forgot Password");
		System.out.println("4. Create Account");
		System.out.println("5. Exit");

		int input = 0;

		while (input != 5) {

			switch (input) {
			case 1:
				if (this.staff.stream().filter(x -> x.getIsLoggedIn() == true).findFirst().isPresent()) {
					System.out.println("Already logged in");
				} else if (login()) {
					System.out.println("Login successful");
				}
				mainMenu();
				break;
			case 2:
				if (this.staff.stream().filter(x -> x.getIsLoggedIn() == true).findFirst().isPresent()) {
					logout();
				} else {
					System.out.println("You are not logged in yet");
				}
				mainMenu();
				break;
			case 3:
				forgotPassword();
				mainMenu();
				break;
			case 4:
				createAccount();
				mainMenu();
				break;
			}

			input = Integer.parseInt(sc.nextLine());
		}
		System.exit(0);
	}

	public boolean login() {
		System.out.println("--------------------------");
		System.out.println("Login");
		System.out.println("--------------------------");
		System.out.println("email");
		String email = sc.nextLine();
		System.out.println("password");
		String password = sc.nextLine();

		if (this.staff.stream().filter(x -> x.getEmail().equalsIgnoreCase(email)).findFirst().isPresent()) {
			return this.staff.stream().filter(x -> x.getEmail().equalsIgnoreCase(email)).findFirst().get()
					.CheckPassword(password);

		} else if (this.technician.stream().filter(x -> x.getEmail().equalsIgnoreCase(email)).findFirst().isPresent()) {
			return this.technician.stream().filter(x -> x.getEmail().equalsIgnoreCase(email)).findFirst().get()
					.CheckPassword(password);
		} else {
			System.out.print("Email or password incorrect\n");
			return false;
		}
	}

	public void logout() {
		if (this.staff.stream().filter(x -> x.getIsLoggedIn() == true).findFirst().isPresent()) {
			User loggedInUser = this.staff.stream().filter(x -> x.getIsLoggedIn() == true).findFirst().get();
			loggedInUser.setIsLoggedIn(false);
			System.out.println("Successfully logged out");
		}
	}

	public void forgotPassword() {
		String email, newPassword;
		User user = null;

		System.out.println("--------------------------");
		System.out.println("Forgot Password");
		System.out.println("--------------------------");
		System.out.println("Enter email address:");

		email = sc.nextLine();

		for (int i = 0; i < this.staff.size(); i++) {
			if (this.staff.get(i).email.equals(email)) {
				user = this.staff.get(i);
			}
		}

		if (user == null) {
			for (int i = 0; i < this.technician.size(); i++) {
				if (this.technician.get(i).email.equals(email)) {
					user = this.technician.get(i);
				}
			}
		}

		if (user != null) {
			System.out.println("User found...");
			System.out.println("Enter new password: ");

			newPassword = sc.nextLine();

			try {
				user.ChangePassword(email, newPassword);
			} catch (Exception e) {
				System.out.println("Error");
			}

		} else {
			System.out.println("No users found matching " + email);
		}
	}

	public void createAccount() {
		String email;
		String firstName;
		String lastName;
		String phoneNo;
		String password;

		System.out.println("--------------------------");
		System.out.println("Create Account");
		System.out.println("--------------------------");
		System.out.println("Enter email address:");
		email = sc.nextLine();
		while (email == null) {
			System.out.println("Email cannot be blank:");
			email = sc.nextLine();
		}
		System.out.println("Enter first name:");
		firstName = sc.nextLine();
		while (firstName == null) {
			System.out.println("First name cannot be blank:");
			firstName = sc.nextLine();
		}
		System.out.println("Enter last name:");
		lastName = sc.nextLine();
		while (lastName == null) {
			System.out.println("Last name cannot be blank:");
			lastName = sc.nextLine();
		}
		System.out.println("Enter phone number:");
		phoneNo = sc.nextLine();
		while (phoneNo == null) {
			System.out.println("Phone number cannot be blank:");
			phoneNo = sc.nextLine();
		}
		System.out.println("Enter password:");
		password = sc.nextLine();
		while (!checkPasswordString(password)) {
			System.out.println("Password must contain an upper case, lower case and number:");
			password = sc.nextLine();
			checkPasswordString(password);
		}

		this.staff.add(new Staff(firstName, lastName, email, password, phoneNo));
		System.out.println("Account created");
		mainMenu();
	}

	// Check the password input contains an uppercase, lowercase and number
	// character
	private boolean checkPasswordString(String password) {
		boolean upper = false;
		boolean lower = false;
		boolean num = false;
		char tmp;

		for (int i = 0; i < password.length(); i++) {
			tmp = password.charAt(i);

			if (Character.isUpperCase(tmp)) {
				upper = true;
			} else if (Character.isLowerCase(tmp)) {
				lower = true;
			} else if (Character.isDigit(tmp)) {
				num = true;
			}
		}
		if (upper && lower && num) {
			return true;
		}
		return false;
	}

	public boolean startUp() {

		// Staff
		try {
			File myObj = new File("./src/Files/Staff.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] user = data.split(",");
				this.staff.add(new Staff(true, user[0], user[1], user[2], user[3], user[4]));

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			return false;
		}

		// Technician
		try {
			File myObj = new File("./src/Files/Technicians.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] technician = data.split(",");
				this.technician.add(new Technician(true, Integer.parseInt(technician[0]), technician[1], technician[2],
						technician[3], technician[4], technician[5]));

			}
			myReader.close();
		} catch (FileNotFoundException e) {
			return false;
		}

		return true;
	}

	public static void main(String[] args) {

		new SEPMA2();

	}

}
