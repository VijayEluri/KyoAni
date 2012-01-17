package net.hisme.masaki.kyoani.test.models.schedule_service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.test.AndroidTestCase;
import net.hisme.masaki.kyoani.models.Schedule;
import net.hisme.masaki.kyoani.models.schedule_service.AnimeOne;
import net.hisme.masaki.kyoani.models.schedule_service.SessionExpiredException;
import net.hisme.masaki.kyoani.utils.StringUtils;

public class AnimeOneTest extends AndroidTestCase {
  AnimeOne anime_one;

  public void setUp() {
    this.anime_one = new AnimeOne();
    anime_one.clearUpdatedDate();
  }

  public void testNeedUpdate() {
    assertTrue(anime_one.needUpdate());
  }

  public void testParseMyPageOnSuccess() throws SessionExpiredException {
    ArrayList<Schedule> schedules = anime_one.parseMyPage(loadMypageTestData("mypage_success"));
    assertEquals(3, schedules.size());

    Schedule schedule = schedules.get(0);
    assertEquals("機動戦士ガンダムAGE", schedule.getName());
    assertEquals("TBS", schedule.getChannel());
    assertEquals("17:00", schedule.getStartString());

    schedule = schedules.get(2);
    assertEquals("アクエリオンEVOL", schedule.getName());
    assertEquals("テレビ東京", schedule.getChannel());
    assertEquals("25:35", schedule.getStartString());

  }

  public void testHttpGet() {
    String body = anime_one.httpGet("https://github.com/masarakki/KyoAni");
    Matcher match = Pattern.compile("<title>masarakki/KyoAni - GitHub</title>",
        Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNIX_LINES).matcher(body);
    assertTrue(match.find());
  }

  public String loadMypageTestData(String filename) {
    return StringUtils.getString(this.getContext().getAssets(), "test_data/anime_one/" + filename + ".html");
  }
}
