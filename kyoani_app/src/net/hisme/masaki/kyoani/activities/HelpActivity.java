package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;

/**
 * @author masarakki
 */
public class HelpActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.help);

    ((Button) findViewById(R.id.button_link_to_animeone)).setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://life-plan.np-complete-doj.in")));
      }
    });
    ((Button) findViewById(R.id.button_link_to_setting)).setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        startActivity(new Intent(HelpActivity.this, AccountActivity.class));
      }
    });
  }
}