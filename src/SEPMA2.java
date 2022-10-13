import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SEPMA2 {

	public User currentUser;

	public List<Staff> staff = new ArrayList<>();
	public List<Technician> technician = new ArrayList<>();
	public List<Ticket> tickets = new ArrayList<>();
	Scanner sc = new Scanner(System.in);

	public SEPMA2() {

		// Import data from text files
		startUp();

		MainMenu();

	}

	// -------------Menus----------------
	public void MainMenu() {

		String mainMenu = """
				--------------------------
				Welcome
				--------------------------
				1. Login
				2. Forgot Password
				3. Create Account
				4. Exit
				""";

		int input = 0;

		while (input != 4) {

			switch (input) {
			case 1:
				if (login()) {
					if (this.currentUser instanceof Staff) {

						StaffLoginMenu();
					} else {

						TechLoginMenu();
					}

					break;
				} else {
					break;
				}
			case 2:
				forgotPassword();
				break;
			case 3:
				createAccount();
				break;

			}
			System.out.println(mainMenu);
			input = Integer.parseInt(sc.nextLine());
		}
	}

	public void StaffLoginMenu() {

		Staff currentUser = (Staff) this.currentUser;

		String menu = """
				--------------------------
				Welcome""" + " " + currentUser.firstName + " " + currentUser.lastName.toUpperCase() + "\n" + """
				--------------------------
				1. Dashboard
				2. New Ticket
				3. Logout
				4. Exit
				""";

		int input = 0;
		while (input != 4) {

			switch (input) {
			case 1:
				break;
			case 2:
				newTicket();

				break;
			case 3:
				logout();
				break;

			}
			System.out.println(menu);
			input = Integer.parseInt(sc.nextLine());
		}

	}

	public void TechLoginMenu() {
		Technician currentUser = (Technician) this.currentUser;

		String menu = "--------------------------\nWelcome\n--------------------------\n" + 
		"1. Change Ticket Status\n2. Logout";
		String input = "";

		while (!input.equals("2")) {

			System.out.println(menu);
			input = sc.nextLine();

			switch (input) {
			case "1":
				System.out.println("Enter ticket number: ");
				int ticketNum = Integer.parseInt(sc.nextLine());
				Ticket ticket = null;

				for (int i = 0; i < tickets.size(); i += 1) {
					if (tickets.get(i).ticketNumber == ticketNum) {
						ticket = tickets.get(i);
					}
				}

				if (ticket != null) {
					System.out.println(String.format("Ticket: %s\nSubject: %s\nDescription: %s\n", ticket.ticketNumber,
							ticket.subject, ticket.description));
					System.out.println(
							"Select new status: \n1. Open\n2. Closed\n3. Unresolved\n4. Resolved\n5. Archived\n");
					input = sc.nextLine();
					Boolean error = false;

					try {
						switch (input) {
						case "1":
							ticket.setStatus(Status.Open);
							ticket.changeStatus(String.valueOf(ticketNum), "Open");
							break;
						case "2":
							ticket.setStatus(Status.Closed);
							ticket.changeStatus(String.valueOf(ticketNum), "Closed");
							break;
						case "3":
							ticket.setStatus(Status.Unresolved);
							ticket.changeStatus(String.valueOf(ticketNum), "Unresolved");
							break;
						case "4":
							ticket.setStatus(Status.Resolved);
							ticket.changeStatus(String.valueOf(ticketNum), "Resolved");
							break;
						case "5":
							ticket.setStatus(Status.Archived);
							ticket.changeStatus(String.valueOf(ticketNum), "Archived");
							break;
						default:
							error = true;
							break;
						}
					} catch (IOException e) {
						System.out.println("Error: Cannot access system records");
					}

					if (error) {
						System.out.println("Error: Invalid selection.");
					} else {
						System.out.println("Ticket status changed.");
					}

				} else {
					System.out.println(String.format("Ticket number %s does not exist.", ticketNum));
				}
			}
		}

	}

	public void newTicket() {

		System.out.println("Subject:");
		String subject = sc.nextLine();

		System.out.println("Description:");
		String description = sc.nextLine();

		String sev = """
				1. Low
				2. High""";

		System.out.println(sev);
		int severity = Integer.parseInt(sc.nextLine());

		Severity severityEnum = severity == 1 ? Severity.LOW : Severity.HIGH;

		Ticket ticket = new Ticket(
				true, this.tickets.size() + 101, this.staff.stream()
						.filter(x -> x.email.equalsIgnoreCase(((Staff) this.currentUser).email)).findFirst().get(),
				subject, description, severityEnum);

		this.tickets.add(ticket);
		this.staff.stream().filter(x -> x.email.equalsIgnoreCase(((Staff) this.currentUser).email)).findFirst().get()
				.addTicket(ticket);

	}

	// ------------- Functions-------------
	public boolean login() {
		System.out.println("--------------------------");
		System.out.println("Login");
		System.out.println("--------------------------");
		System.out.println("email");
		String email = sc.nextLine();
		System.out.println("password");
		String password = sc.nextLine();

		boolean succesfulLogin = false;

		if (this.staff.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().isPresent()) {

			succesfulLogin = this.staff.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().get()
					.CheckPassword(password);

			this.currentUser = (Staff) this.staff.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst()
					.get();

		} else if (this.technician.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().isPresent()) {

			succesfulLogin = this.technician.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().get()
					.CheckPassword(password);

			this.currentUser = (Technician) this.technician.stream().filter(x -> x.email.equalsIgnoreCase(email))
					.findFirst().get();
		}

		return succesfulLogin;
	}

	public void logout() {
		this.currentUser = null;
		MainMenu();

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

	// check the input email does not already exist in the system
	private boolean checkEmailIsUnique(String email) {
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

		if (password.length() < 20) {
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

	// -----------Start Up--------------
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

		// Tickets
		try {
			File myObj = new File("./src/Files/Tickets.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] ticketData = data.split(",");

				Ticket ticket = new Ticket(Integer.parseInt(ticketData[0]),
						this.staff.stream().filter(x -> x.email.equalsIgnoreCase(ticketData[1])).findFirst().get(),
						ticketData[3], ticketData[4], Severity.valueOf(ticketData[5]));

				ticket.setStatus(Status.valueOf(ticketData[6]));

				if (!ticketData[2].equals("")) {
					ticket.setAssignedTo(this.technician.stream().filter(x -> x.email.equalsIgnoreCase(ticketData[2]))
							.findFirst().get());

				}

				this.tickets.add(ticket);
				this.staff.stream().filter(x -> x.email.equalsIgnoreCase(ticketData[1])).findFirst().get()
						.addTicket(ticket);

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
