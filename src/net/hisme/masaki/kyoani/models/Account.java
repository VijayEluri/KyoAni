package net.hisme.masaki.kyoani.models;

/**
 * アカウント情報
 * 
 * @author masaki
 * 
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
  public String username() {
    return this.username;
  }

  /**
   * get password
   * 
   * @return password
   */
  public String password() {
    return this.password;
  }

  public boolean is_blank() {
    return this.password == null || this.username == null
        || this.password == "" || this.username == "";
  }
}
