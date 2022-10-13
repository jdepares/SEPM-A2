import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Technician extends User {
	
	public int level;
	
	public String firstName;
	public String lastName;

	public String email;
	public String password;

	public String phone;
	public List<Ticket> assignedTickets = new ArrayList<>();

	public Technician(int level, String firstName, String lastName, String email, String password, String phone) {
		super();
		this.level = level;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		super.NewUser(true, firstName, lastName, email, password, phone);
	}

	public Technician(boolean startUp, int level, String firstName, String lastName, String email, String password, String phone) {
		super();
		this.level = level;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;

		if (!startUp) {
			super.NewUser(true, firstName, lastName, email, password, phone);
		}
	}

	public boolean CheckPassword(String password) {
		return super.CheckPassword(password, this.password);
	}

	@Override
	protected void ChangePassword(String email, String newPassword) throws IOException, FileNotFoundException {
		this.password = newPassword;
		
		String fileName = "./src/Files/Technicians.txt";
		String data = "";
		String[] lineData;
		String emailRecord;
		String[] userRecord = null;
		
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		String line;
		
		while ((line = br.readLine()) != null) {
			
			lineData = line.split(",");
			emailRecord = lineData[3];
			
			if (emailRecord.equals(email)) {
				userRecord = lineData;			
			} else {
				data += line + "\n";
			}
		}
		
		// Write data to file
		if (userRecord != null) {
			userRecord[4] = newPassword;
			
			FileWriter myWriter = new FileWriter(fileName, false);
			BufferedWriter bw = new BufferedWriter(myWriter);
			//myWriter.write(content + "\n");
			//myWriter.close();

			bw.write(data);
			bw.append(String.format("%s,%s,%s,%s,%s,%s", userRecord[0], userRecord[1], userRecord[2], userRecord[3], userRecord[4], userRecord[5]));
			bw.close();
		}
	}
	
	public void addTicket(Ticket ticket) {
		this.assignedTickets.add(ticket);
	}
}
