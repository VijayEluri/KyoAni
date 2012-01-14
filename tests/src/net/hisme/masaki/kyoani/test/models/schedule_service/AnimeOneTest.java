package net.hisme.masaki.kyoani.test.models.schedule_service;

import junit.framework.TestCase;
import net.hisme.masaki.kyoani.models.schedule_service.AnimeOne;

public class AnimeOneTest extends TestCase {
  AnimeOne anime_one;

  public void setUp() {
    this.anime_one = new AnimeOne();
  }

  public void test_need_update() {
    assertTrue(anime_one.needUpdate());
  }
}
