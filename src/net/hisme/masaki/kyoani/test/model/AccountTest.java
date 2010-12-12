package net.hisme.masaki.kyoani.test.model;

import net.hisme.masaki.kyoani.models.Account;
import org.junit.*;
import static org.junit.Assert.*;

public class AccountTest {
    Account account = null;

    @Before
    public void set_account() {
        account = new Account();
    }

    @Test
    public void getUser() {
        assertEquals(account.getUser(), null);
    }

    @Test
    public void getPassword() {
        assertEquals(account.getPassword(), null);
    }

    @Test
    public void BlankException() {
        Account.BlankException e = new Account.BlankException();
        assertEquals(e.getClass().getName(),
                "net.hisme.masaki.kyoani.models.Account$BlankException");
    }
}
