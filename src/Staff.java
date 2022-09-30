public class Staff extends User {

	public String firstName;
	public String lastName;

	public String email;
	public String password;

	public String phone;

	public Staff(String firstName, String lastName, String email, String password, String phone) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		super.NewUser(true, firstName, lastName, email, password, phone);
	}

	public Staff(boolean startUp, String firstName, String lastName, String email, String password, String phone) {
		super();
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
		System.out.println(this.password);
		System.out.println(password);
		return super.CheckPassword(password, this.password);
	}

}
