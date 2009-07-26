package net.hisme.masaki.kyoani;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class KyouAni extends Activity {
	/** Called when the activity is first created. */
	Activity application = null;
	Account account = null;
	EditText form_id = null;
	EditText form_password = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		application = this;
		account = new Account(this);
		form_id = (EditText) findViewById(R.id.form_id);
		form_password = (EditText) findViewById(R.id.form_password);
		form_id.setText(account.getUser());
		form_password.setText(account.getPassword());
		Button save_button = (Button) findViewById(R.id.button_ok);
		Button cancel_button = (Button) findViewById(R.id.button_cancel);
		save_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				account.setAccount(form_id.getText().toString(), form_password
						.getText().toString());
			}
		});
		cancel_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				application.finish();
			}
		});
	}

}