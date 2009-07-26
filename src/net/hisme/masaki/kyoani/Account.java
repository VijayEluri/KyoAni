package net.hisme.masaki.kyoani;

import android.content.Context;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Account {
	private String user_id = null;
	private String password = null;
	private Context context = null;
	private static final String ACCOUNT_FILE = "account.txt";

	public Account(Context context) {
		this.context = context;
		load();
	}

	public void setAccount(String user, String password) {
		this.user_id = user;
		this.password = password;
		save();
	}

	public String getUser() {
		return this.user_id;
	}

	public String getPassword() {
		return this.password;
	}

	private boolean test() {
		return false;
	}

	private void save() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context
					.openFileOutput(ACCOUNT_FILE, Context.MODE_PRIVATE)));
			writer.write(this.user_id + "\n" + this.password);

			writer.flush();
			writer.close();
		} catch (java.io.FileNotFoundException e) {

		} catch (java.io.IOException e) {

		}
	}

	private void load() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(context
					.openFileInput(ACCOUNT_FILE)));
			
			this.user_id = reader.readLine();
			this.password = reader.readLine();
		} catch (java.io.FileNotFoundException e) {

		} catch (java.io.IOException e) {

		}
	}
}
