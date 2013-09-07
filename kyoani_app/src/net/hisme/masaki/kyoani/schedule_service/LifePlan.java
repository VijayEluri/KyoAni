package net.hisme.masaki.kyoani.schedule_service;

import net.hisme.masaki.kyoani.App;
import net.hisme.masaki.kyoani.life_plan.AccessToken;
import net.hisme.masaki.kyoani.life_plan.LifePlanClient;
import net.hisme.masaki.kyoani.models.Schedules;
import net.hisme.masaki.kyoani.schedule_service.exception.LoginFailureException;
import net.hisme.masaki.kyoani.schedule_service.exception.NetworkUnavailableException;

public class LifePlan implements ScheduleService {

  final String api_key = "196a2a5bb19973256ed9e39a44867ee57b15b0eca3880b7d293ac59bb482fb06";
  final String api_secret = "bcadc08a23b845028cdeb96ec28acc55e054caecd7058668b099e105c8df6526";

  final String callback = "kyoani://callback";
  protected LifePlanClient client;
  private AccessToken access_token;

  public LifePlan() {
    client = new LifePlanClient(api_key, api_secret, callback);
  }

  public void setAccessToken(AccessToken token) {
    this.access_token = token;
  }

  public String getAuthorizationUrl() {
    return client.getAuthorizationUrl();
  }

  public AccessToken getAccessToken(String code) {
    return client.getAccessToken(code);
  }

  public AccessToken getAccessToken(AccessToken old) {
    return client.refresh(old);
  }

  @Override
  public boolean login() throws NetworkUnavailableException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Schedules reloadSchedules() throws LoginFailureException, NetworkUnavailableException {
    if (access_token == null) {
      throw new LoginFailureException();
    }
    if (access_token.isExpired()) {
      access_token = client.refresh(access_token);
      App.li.saveToken(access_token);
    }
    return client.getSchedules(access_token);
  }

  @Override
  public Schedules getSchedules() throws LoginFailureException, NetworkUnavailableException {
    return reloadSchedules();
  }
}
