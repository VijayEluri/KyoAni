package net.hisme.masaki.kyoani.models;

import net.hisme.masaki.kyoani.models.Account;
import org.junit.*;
import static org.junit.Assert.*;

public class AccountTest {
  Account account = null;

  @Before
  public void before() {
    this.account = new Account("UserName", "PassWord");
  }

  @Test
  public void username_should_eq_UserName() {
    assertEquals("UserName", this.account.username());
  }

  @Test
  public void password_should_eq_PassWord() {
    assertEquals("PassWord", this.account.password());
  }

  @Test
  public void is_blank_should_be_false() {
    assertFalse(this.account.is_blank());
  }

  @Test
  public void is_blank_should_be_true_if_username_and_password_is_blank() {
    this.account = new Account("", "");
    assertTrue(this.account.is_blank());
  }
}
