package net.hisme.masaki.kyoani.test.model;

import net.hisme.masaki.kyoani.models.Account;
import net.hisme.masaki.kyoani.models.AnimeOne;

import org.junit.*;
import static org.junit.Assert.*;

public class AnimeOneTest {
    private AnimeOne anime_one;

    @Before
    public void before() {
        anime_one = new AnimeOne();
    }

    @Test
    public void getAccount() {
        Account account = new Account();
        anime_one.setAccount(account);
        assertEquals(anime_one.getAccount(), account);
    }

    @Test
    public void getContext() {

    }
}
