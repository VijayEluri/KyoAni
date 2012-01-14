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

  public void test_username() {
    assertEquals("UserName", this.account.username());
  }

  public void test_password() {
    assertEquals("PassWord", this.account.password());
  }

  public void test_is_blank() {
    assertFalse(this.account.is_blank());
    assertTrue(this.blank_account.is_blank());
  }
}
