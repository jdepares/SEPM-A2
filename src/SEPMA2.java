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
	}

	public void createAccount() {
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
