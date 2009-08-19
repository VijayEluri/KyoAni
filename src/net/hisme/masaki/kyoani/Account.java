package net.hisme.masaki.kyoani;

public class Account {
	private String user_id = null;
	private String password = null;

	public Account(String user_id, String password) {
		this.user_id = user_id;
		this.password = password;
	}

	public String getUser() {
		return this.user_id;
	}

	public String getPassword() {
		return this.password;
	}

}
