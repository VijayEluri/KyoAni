package net.hisme.masaki.kyoani.life_plan;

import java.io.Serializable;
import java.util.Date;

public class AccessToken implements Serializable {
  private static final long serialVersionUID = 469502450719432861L;

  protected String access_token;
  protected String refresh_token;
  protected Integer expires_in;
  protected Date created_at;

  public AccessToken() {
    this.created_at = new Date();
  }

  public AccessToken(String access_token) {
    this(access_token, null);
  }

  public AccessToken(String access_token, String refresh_token) {
    this(access_token, refresh_token, null);
  }

  public AccessToken(String access_token, String refresh_token, Integer expires_in) {
    this(access_token, refresh_token, expires_in, new Date());
  }

  public AccessToken(String access_token, String refresh_token, Integer expires_in, Date created_at) {
    this.access_token = access_token;
    this.refresh_token = refresh_token;
    this.expires_in = expires_in;
    this.created_at = created_at;
  }

  public String getAccessToken() {
    return access_token;
  }

  public String getRefreshToken() {
    return refresh_token;
  }

  public Date getExpiresAt() {
    if (expires_in == null)
      return null;
    return new Date(this.created_at.getTime() + expires_in * 1000);
  }

  public boolean isExpired() {
    Date expires_at = getExpiresAt();
    if (expires_at == null)
      return false;
    return new Date().after(expires_at);
  }

  public boolean isRefreshable() {
    return refresh_token != null;
  }

  public void setAccessToken(String access_token) {
    this.access_token = access_token;
  }

  public void setRefreshToken(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public void setExpiresIn(Integer expires_in) {
    this.expires_in = expires_in;
  }

  @Override
  public String toString() {
    return String.format("AccessToken[%s, refresh=%s, expires_at=%s]", access_token, refresh_token, getExpiresAt());
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    AccessToken that = (AccessToken) o;
    return this.access_token.equals(that.access_token);
  }

  @Override
  public int hashCode()
  {
    return 31 * access_token.hashCode();
  }

  public boolean isValid() {
    if (access_token == null) {
      return false;
    }
    return true;
  }
}
