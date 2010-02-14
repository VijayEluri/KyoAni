package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		TextView mail = (TextView) findViewById(R.id.mail);
		mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri
						.parse("mailto:"
								+ getText(R.string.app_author_mail).toString().trim()));
				startActivity(intent);
			}
		});
	}
}
