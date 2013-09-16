package net.hisme.masaki.kyoani.activities;

import net.hisme.masaki.kyoani.App.Log;
import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.R;
import net.hisme.masaki.kyoani.life_plan.AccessToken;
import net.hisme.masaki.kyoani.schedule_service.LifePlan;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.account);
    Button auth_button = (Button) findViewById(R.id.button_authorize);
    AccessToken token = App.li.loadToken();
    updateAccessToken(token);
    auth_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        LifePlan client = new LifePlan();
        Log.d(client.getAuthorizationUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW,
            Uri.parse(client.getAuthorizationUrl()));
        startActivity(intent);
      }
    });
  }

  public void updateAccessToken(AccessToken token) {
    TextView authorized = (TextView) findViewById(R.id.authorized);
    authorized.setText(token != null ? token.getAccessToken() : "no");
  }

  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    final Uri uri = intent.getData();
    if (uri != null && uri.toString().startsWith("kyoani://callback")) {
      final Handler handler = new Handler();
      new Thread() {
        public void run() {
          String authenticate_code = uri.getQueryParameter("code");
          LifePlan client = new LifePlan();
          final AccessToken token = client.getAccessToken(authenticate_code);
          App.li.saveToken(token);
          handler.post(new Runnable() {
            public void run() {
              AccountActivity.this.updateAccessToken(token);
            }
          });
        }
      }.start();

    }
  }
}
