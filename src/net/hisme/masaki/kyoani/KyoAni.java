package net.hisme.masaki.kyoani;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class KyoAni extends Activity {
	/** Called when the activity is first created. */
	Activity application = null;
	AnimeOne anime_one = null;
	Account account = null;
	EditText form_id = null, form_password = null;
	Button save_button = null, cancel_button = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		application = this;
		account = new Account(this);
		anime_one = new AnimeOne(account);
		form_id = (EditText) findViewById(R.id.form_id);
		form_password = (EditText) findViewById(R.id.form_password);
		form_id.setText(account.getUser());
		form_password.setText(account.getPassword());
		save_button = (Button) findViewById(R.id.button_ok);
		cancel_button = (Button) findViewById(R.id.button_cancel);
		save_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				account.setAccount(form_id.getText().toString(), form_password
						.getText().toString());

				TextView result_area = (TextView) findViewById(R.id.result_area);
				if (anime_one.login()) {
					account.save();
					result_area.setText("ログイン成功しました");
				} else {
					result_area.setText("ログイン失敗しました");
				}

				// save_button.setText(result ? "true" : "false");
			}
		});
		cancel_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				application.finish();
			}
		});
	}
}