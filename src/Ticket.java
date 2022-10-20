import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

enum Severity {
	LOW, MEDIUM, HIGH
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

	public String date;

	public Ticket(int ticketNumber, Staff createdBy, String subject, String description, Severity severity,
			String date) {

		super();
		this.ticketNumber = ticketNumber;
		this.createdBy = createdBy;
		this.subject = subject;
		this.description = description;
		this.severity = severity;
		this.status = Status.Open;
		this.date = date;
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

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();

		this.date = dtf.format(now);
		if (newTicket) {
			String dir = "./src/Files/Tickets.txt";
			String content = this.ticketNumber + "," + this.createdBy.email + "," + this.subject + ","
					+ this.description + "," + this.severity + "," + this.status + "," + this.date + "\n";

			try {
				FileWriter myWriter = new FileWriter(dir, true);
				BufferedWriter bw = new BufferedWriter(myWriter);
				bw.append(content);
				bw.close();
			} catch (IOException e) {
			}
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