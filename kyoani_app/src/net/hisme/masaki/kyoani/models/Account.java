package net.hisme.masaki.kyoani.models;

/**
 * アカウント情報
 * 
 * @author masarakki
 */
public class Account {
  private String username = null;
  private String password = null;

  /**
   * create Account instance
   */
  public Account(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * get user name
   * 
   * @return user name
   */
  public String getUserName() {
    return this.username;
  }

  /**
   * get password
   * 
   * @return password
   */
  public String getPassword() {
    return this.password;
  }

  public boolean isBlank() {
    return this.password == null || this.username == null
        || this.password == "" || this.username == "";
  }
}
