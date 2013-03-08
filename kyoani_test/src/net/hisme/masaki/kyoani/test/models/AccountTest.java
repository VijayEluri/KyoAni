package net.hisme.masaki.kyoani.test.models;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.models.Account;

public class AccountTest extends TestCase {
  Account account = null;
  Account blank_account = null;

  public void setUp() {
    this.account = new Account("UserName", "PassWord");
    this.blank_account = new Account("", "");
  }

  public void testUserName() {
    assertEquals("UserName", this.account.getUserName());
  }

  public void testPassword() {
    assertEquals("PassWord", this.account.getPassword());
  }

  public void testIsBlank() {
    assertFalse(this.account.isBlank());
    assertTrue(this.blank_account.isBlank());
  }
}
