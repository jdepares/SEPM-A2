import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SEPMA2 {

	public List<Staff> staff = new ArrayList<>();
	public List<Technician> technician = new ArrayList<>();
	Scanner sc = new Scanner(System.in);

	public SEPMA2() {

		// Import data from text files
		startUp();

		// Remove before submit, used to test
		for (int i = 0; i < this.staff.size(); i++) {
			System.out.println(this.staff.get(i).firstName);
			System.out.println(this.staff.get(i).lastName);
			System.out.println(this.staff.get(i).email);
			System.out.println(this.staff.get(i).password);
			System.out.println(this.staff.get(i).phone);
			System.out.println("------------");
		}

		for (int i = 0; i < this.technician.size(); i++) {
			System.out.println(this.technician.get(i).level);
			System.out.println(this.technician.get(i).firstName);
			System.out.println(this.technician.get(i).lastName);
			System.out.println(this.technician.get(i).email);
			System.out.println(this.technician.get(i).password);
			System.out.println(this.technician.get(i).phone);
			System.out.println("------------");
		}

		MainMenu();

	}

	public void MainMenu() {
		System.out.println("--------------------------");
		System.out.println("Welcome");
		System.out.println("--------------------------");
		System.out.println("1. Login");
		System.out.println("2. Forgot Password");
		System.out.println("3. Create Account");
		System.out.println("4. Exit");

		int input = 0;

		while (input != 4) {

			switch (input) {
				case 1:
					if (login()) {

						System.out.println("Successful login, do something");
					} else {
						MainMenu();
					}
				case 2:
					forgotPassword();
					break;
				case 3:
					createAccount();
			}

			input = Integer.parseInt(sc.nextLine());
		}
	}

	public boolean login() {
		System.out.println("--------------------------");
		System.out.println("Login");
		System.out.println("--------------------------");
		System.out.println("email");
		String email = sc.nextLine();
		System.out.println("password");
		String password = sc.nextLine();

		if (this.staff.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().isPresent()) {

			return this.staff.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().get()
					.CheckPassword(password);

		} else if (this.technician.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().isPresent()) {

			return this.technician.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().get()
					.CheckPassword(password);
		} else {
			return false;
		}
	}

	public void forgotPassword() {
		String email, newPassword;
		Boolean match = false;
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
		while (email == null || !checkEmailIsUnique(email)) {
			System.out.println("Please enter a unique email address:");
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
		while (!checkPasswordRequirments(password)) {
			System.out.println("Password must contain an upper case, lower case and number:");
			password = sc.nextLine();
			checkPasswordRequirments(password);
		}
		this.staff.add(new Staff(firstName, lastName, email, password, phoneNo));
		System.out.println("Account created");
		MainMenu();
	}

	//check the input email does not already exist in the system
	private boolean checkEmailIsUnique(String email){
		for (int i = 0; i < this.staff.size(); i++) {
			if (this.staff.get(i).email.equals(email)) {
				return false;
			}
		}
		return true;
	}
	
	// Check the password input contains an uppercase, lowercase, number
	// character and is at least 20 characters in length
	private boolean checkPasswordRequirments(String password) {
		boolean upper = false;
		boolean lower = false;
		boolean num = false;
		char tmp;

		if(password.length() < 20){
			return false;
		}

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
			File myObj = new File("./Files/Staff.txt");
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
