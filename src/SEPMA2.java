import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SEPMA2 {

	public User currentUser;

	public List<Staff> staff = new ArrayList<>();
	public List<Admin> admin = new ArrayList<>();
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

		String mainMenu = "Welcome \n" + "1. Login\n" + "2. Forgot Password\n" + "3. Create Account\n" + "4. Exit\n";

		int input = 0;

		while (input != 4) {

			switch (input) {
			case 1:
				if (login()) {
					if (this.currentUser instanceof Staff) {
						StaffLoginMenu();
					} else if (this.currentUser instanceof Technician) {
						TechLoginMenu();
					} else {
						adminLogin();

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

		String menu = "Welcome " + currentUser.firstName + " " + currentUser.lastName.toUpperCase() + "\n"
				+ "1. Dashboard\n" + "2. New Ticket\n" + "3. Logout\n" + "4. Exit\n";

		int input = 0;
		while (input != 4) {

			switch (input) {
			case 1:
				System.out.println("--------------------\nActive Tickets\n--------------------\n");

				for (int i = 0; i < this.tickets.size(); i += 1) {
					if (this.tickets.get(i).createdBy == currentUser && this.tickets.get(i).status != Status.Closed) {
						Ticket ticket = this.tickets.get(i);
						System.out.println(String.format(
								"Date: %s\nTicket Number: %d\nSubject: %s\nStatus: %s\nDescription: %s\n", ticket.date,
								ticket.ticketNumber, ticket.subject, ticket.description, ticket.status));
					}
				}

				System.out.println("\nPress any key to continue...");
				sc.nextLine();
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
		String menu = "--------------------------\nWelcome\n--------------------------\n" + "1. Dashboard\n" + "2. Change Ticket Status\n"
				+ "3. Change Ticket Severity\n" + "4. Logout\n";
		String menuInput = "";

		while (!menuInput.equals("4")) {

			System.out.println(menu);
			menuInput = sc.nextLine();

			switch (menuInput) {
			// Prints all assigned tickets of the current user as "ticket number: subject"
		    case "1":
		          int technicianTickets = 0;
	              for (int i = 0; i < this.tickets.size(); i++) {
	                    if (this.tickets.get(i).assignedTo == currentUser) {
	                        technicianTickets++;
	                        System.out.printf("Ticket %s: %s\n", tickets.get(i).ticketNumber, tickets.get(i).subject);
	                    }
	              }
	               if (technicianTickets == 0) {
	                      System.out.printf("This technician has no tickets assigned to them\n");
	                  }
		        break;
			case "2":
				System.out.println("Enter ticket number: ");
				int ticketNum = Integer.parseInt(sc.nextLine());
				Ticket ticket = null;

				for (int i = 0; i < tickets.size(); i += 1) {
					if (tickets.get(i).ticketNumber == ticketNum) {
						ticket = tickets.get(i);
					}
				}

				if (ticket != null) {
					if(ticket.getStatus() == Status.Archived) {
						System.out.println("Cannot change the status of an archived ticket");
						TechLoginMenu();
					}
					System.out.println(String.format("Ticket: %s\nSubject: %s\nDescription: %s\n", ticket.ticketNumber,
							ticket.subject, ticket.description));
					System.out.println(
							"Select new status: \n1. Open\n2. Closed\n3. Unresolved\n4. Resolved\n5. Archived\n");
					String statusInput = sc.nextLine();
					Boolean error = false;

					try {
						switch (statusInput) {
						case "1":
							ticket.setStatus(Status.Open);
							break;
						case "2":
							ticket.setStatus(Status.Closed);
							startTimer(ticket);
							break;
						case "3":
							ticket.setStatus(Status.Unresolved);
							break;
						case "4":
							ticket.setStatus(Status.Resolved);
							break;
						case "5":
							ticket.setStatus(Status.Archived);
							break;
						default:
							error = true;
							break;
						}
						refreshTickets();
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
				break;
			case "3":
				System.out.println("Which ticket would you like to change: ");
				ticketNum = Integer.parseInt(sc.nextLine());
				ticket = null;

				for (int i = 0; i < tickets.size(); i += 1) {
					if (tickets.get(i).ticketNumber == ticketNum) {
						ticket = tickets.get(i);
					}
				}
				if (ticket != null) {
					System.out.println(String.format(
							"Ticket number: %s\nAssigned to: %s\nSubject: %s\nDescription: %s\nSeverity: %s",
							ticket.ticketNumber, ticket.assignedTo.firstName, ticket.subject, ticket.description,
							ticket.severity));
					System.out.println("New severity level: \n1. Low\n2. Medium\n3. High\n");
					String newSeverity = sc.nextLine();
					String oldSeverity = ticket.severity.toString();

					try {
						switch (newSeverity) {
						case "1":
							ticket.setSeverity(Severity.LOW);
							break;
						case "2":
							ticket.setSeverity(Severity.MEDIUM);
							break;
						case "3":
							ticket.setSeverity(Severity.HIGH);
							break;
						default:
							System.out.println(String.format("Out of bounds."));
							break;
						}
						refreshTickets();
					} catch (IOException e) {
						System.out.println("Error: Cannot access system records");
					}

					// If severity LEVEL has changed, a new technician will need to be assigned
					if (!isSameLevel(ticket.severity, oldSeverity)) {
						if (ticket.severity.toString() == "HIGH") {
							assignTicket(ticket, 2);
						} else {
							assignTicket(ticket, 1);
						}
						try {
							refreshTickets();
						} catch (IOException e) {
							System.out.println("Error: Cannot access system records");
						}
					}
					System.out.println("Ticket severity level changed.");
					System.out.println(String.format(
							"Ticket number: %s\nAssigned to: %s\nSubject: %s\nDescription: %s\nSeverity: %s",
							ticket.ticketNumber, ticket.assignedTo.firstName, ticket.subject, ticket.description,
							ticket.severity));
				}
			}
		}
	}
	
	//Archive closed ticket after 1 day of being closed
	private void startTimer(Ticket ticket) {
		Timer timer = new Timer();
		TimerTask t = new TimerTask() {
			@Override
			public void run() {
				ticket.setStatus(Status.Archived);
				try {
					refreshTickets();
				} catch (IOException e) {
					e.printStackTrace();
				}
				timer.cancel();
			}
		};
		timer.scheduleAtFixedRate(t,1000 * 60 * 60 * 24,5);
	}

public void adminLogin() {
		String mainMenu = "Welcome \n" + "1. Dashboard\n" + "2. Exit\n";

		int input = 0;

		while (input != 2) {

			switch (input) {
			case 1:
				adminDashboard();
				break;
			}
			System.out.println(mainMenu);
			input = Integer.parseInt(sc.nextLine());
		}
	}

	public void adminDashboard() {

		System.out.println("Start Date (YYYY-MM-DD)");
		String startDate = sc.nextLine();

		System.out.println("End Date (YYYY-MM-DD)");
		String endDate = sc.nextLine();

		try {
			int total = 0;
			int open = 0;
			int closed = 0;
			int resolved = 0;
			int unresolved = 0;
			int archived = 0;

			Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

			for (int i = 0; i < this.tickets.size(); i++) {

				Date ticketDate = new SimpleDateFormat("yyyy-MM-dd").parse(this.tickets.get(i).date);
				Status status = this.tickets.get(i).status;

				if (ticketDate.after(start) && ticketDate.before(end)) {

					switch (status) {

					case Open:
						open++;
						break;
					case Closed:
						closed++;
						break;
					case Resolved:
						resolved++;
						break;
					case Unresolved:
						unresolved++;
						break;
					case Archived:
						archived++;
						break;

					}

					total++;

				}

			}

			String results = "-------------\n" + "1. Total: " + total + "\n2. Open: " + open + "\n3. Closed: " + closed
					+ "\n4. Resolved: " + resolved + "\n5. Unresolved: " + unresolved + "\n6. Archived: " + archived
					+ "\n-------------\n";
			System.out.println(results);
		} catch (ParseException e) {

		}

	}



	private boolean isSameLevel(Severity oldSeverity, String newSeverity) {

		if (oldSeverity.toString() == "HIGH" && newSeverity != "HIGH") {
			return false;
		}

		if (oldSeverity.toString() == "MEDIUM" && newSeverity == "HIGH") {
			return false;
		}

		if (oldSeverity.toString() == "LOW" && newSeverity == "HIGH") {
			return false;
		}

		return true;
	}

	public void newTicket() {

		System.out.println("Subject:");
		String subject = sc.nextLine();

		System.out.println("Description:");
		String description = sc.nextLine();

		String sev = """
				1. Low
				2. Medium
				3. High""";

		System.out.println(sev);
		int severity = Integer.parseInt(sc.nextLine());

		Severity severityEnum;
		int level;

		if (severity == 1) {
			level = 1;
			severityEnum = Severity.LOW;
		} else if (severity == 2) {
			level = 1;
			severityEnum = Severity.MEDIUM;
		} else {
			level = 2;
			severityEnum = Severity.HIGH;
		}

		Ticket ticket = new Ticket(
				true, this.tickets.size() + 101, this.staff.stream()
						.filter(x -> x.email.equalsIgnoreCase(((Staff) this.currentUser).email)).findFirst().get(),
				subject, description, severityEnum);

		this.tickets.add(ticket);
		assignTicket(ticket, level);

	}

	private void assignTicket(Ticket ticket, int level) {
		Technician tech = null;
		List<Technician> lowestTickets = new ArrayList<>();
		// Find tech with the lowest tickets currently assigned
		for (int i = 0; i < technician.size(); i++) {
			if (tech == null && technician.get(i).level == level) {
				tech = technician.get(i);
			} else if (technician.get(i).level == level
					&& technician.get(i).assignedTickets.size() < tech.assignedTickets.size()) {
				tech = technician.get(i);
			}
		}
		// If others techs have the same amount of tickets add them to the lowestTickets
		// List
		if (tech != null) {
			for (int i = 0; i < technician.size(); i++) {
				if (technician.get(i).level == level
						&& technician.get(i).assignedTickets.size() == tech.assignedTickets.size()) {
					lowestTickets.add(technician.get(i));
				}
			}
		}
		// Assign a randome tech amongst the list of techs with the lowest tickets
		if (lowestTickets.size() > 1) {
			Random rand = new Random();
			int upperbound = lowestTickets.size();
			int random = rand.nextInt(upperbound);
			tech = lowestTickets.get(random);
		}
		ticket.setAssignedTo(tech);
		tech.addTicket(ticket);
		try {
			refreshTickets();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Ticket assigned to " + tech.firstName);
	}

	public void refreshTickets() throws IOException {
		String fileName = "./src/Files/Tickets.txt";
		FileWriter myWriter = new FileWriter(fileName, false);
		BufferedWriter bw = new BufferedWriter(myWriter);

		for (int i = 0; i < tickets.size(); i++) {
			bw.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", tickets.get(i).ticketNumber,
					tickets.get(i).createdBy.email, tickets.get(i).assignedTo.email, tickets.get(i).subject,
					tickets.get(i).description, tickets.get(i).severity, tickets.get(i).status, tickets.get(i).date));
		}
		bw.close();
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
		} else if (this.admin.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().isPresent()) {

			succesfulLogin = this.admin.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst().get()
					.CheckPassword(password);

			this.currentUser = (Admin) this.admin.stream().filter(x -> x.email.equalsIgnoreCase(email)).findFirst()
					.get();
		}

		return succesfulLogin;
	}

	public void logout() {
		this.currentUser = null;
		MainMenu();

	}


	public void logout() {
		this.currentUser = null;
		MainMenu();

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
			System.out.println("Password must contain an upper case, lower case, number and be minimum 20 characters:");
			password = sc.nextLine();
			checkPasswordRequirments(password);
		}
		this.staff.add(new Staff(firstName, lastName, email, password, phoneNo));
		System.out.println("Account created");
		MainMenu();
	}

	// Check the input email does not already exist in the system
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

		// Admin
		try {
			File myObj = new File("./src/Files/Admin.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] user = data.split(",");
				this.admin.add(new Admin(true, user[0], user[1], user[2], user[3], user[4]));

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
						ticketData[3], ticketData[4], Severity.valueOf(ticketData[5]), ticketData[7]);

				ticket.setStatus(Status.valueOf(ticketData[6]));

				if (!ticketData[2].equals("")) {
					ticket.setAssignedTo(this.technician.stream().filter(x -> x.email.equalsIgnoreCase(ticketData[2]))
							.findFirst().get());
				}
				this.tickets.add(ticket);
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
