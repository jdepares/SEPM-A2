import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

enum Severity {
	LOW, HIGH
}

enum Status {
	Open, Closed, Unresolved, Resolved, Archived
}

public class Ticket {

	public int ticketNumber;
	public Staff createdBy;
	public Technician assignedTo;

	public String subject;
	public String description;

	public Severity severity;
	public Status status;

	public Ticket(int ticketNumber, Staff createdBy, String subject, String description, Severity severity) {

		super();
		this.ticketNumber = ticketNumber;
		this.createdBy = createdBy;
		this.subject = subject;
		this.description = description;
		this.severity = severity;
		this.status = Status.Open;
	}

	public Ticket(boolean newTicket, int ticketNumber, Staff createdBy, String subject, String description,
			Severity severity) {

		super();
		this.ticketNumber = ticketNumber;
		this.createdBy = createdBy;
		this.subject = subject;
		this.description = description;
		this.severity = severity;
		this.status = Status.Open;

		if (newTicket) {
			String dir = "./src/Files/Tickets.txt";
			String content = this.ticketNumber + "," + this.createdBy.email + ",," + this.subject + ","
					+ this.description + "," + this.severity + "," + this.status + "\n";

			try {
				FileWriter myWriter = new FileWriter(dir, true);
				BufferedWriter bw = new BufferedWriter(myWriter);
				// myWriter.write(content + "\n");
				// myWriter.close();
				bw.append(content);
				bw.close();
			} catch (IOException e) {
			}
		}

	}

	public void changeStatus(String ticketNum, String newStatus) throws IOException, FileNotFoundException {

		String fileName = "./src/Files/Tickets.txt";
		String data = "";
		String[] lineData;
		String[] ticketRecord = null;

		BufferedReader br = new BufferedReader(new FileReader(fileName));

		String line;

		while ((line = br.readLine()) != null) {
			lineData = line.split(",");

			if (lineData[0].equals(ticketNum)) {
				ticketRecord = lineData;
			} else {
				data += line;
			}
		}

		if (ticketRecord != null) {
			ticketRecord[6] = newStatus;

			FileWriter myWriter = new FileWriter(fileName, false);
			BufferedWriter bw = new BufferedWriter(myWriter);

			bw.write(data);
			bw.append(String.format("\n%s,%s,%s,%s,%s,%s,%s", ticketRecord[0], ticketRecord[1], ticketRecord[2], 
					ticketRecord[3], ticketRecord[4], ticketRecord[5], ticketRecord[6]));
			bw.close();
		}

	}

	public void setAssignedTo(Technician assignedTo) {
		this.assignedTo = assignedTo;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
