public class Technician extends User {
	
	public int level;
	
	public String firstName;
	public String lastName;

	public String email;
	public String password;

	public String phone;

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

}
