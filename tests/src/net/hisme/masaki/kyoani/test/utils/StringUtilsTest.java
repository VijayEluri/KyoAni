package net.hisme.masaki.kyoani.test.utils;

import net.hisme.masaki.kyoani.utils.StringUtils;
import android.test.AndroidTestCase;
import net.hisme.masaki.kyoani.test.R;

@SuppressWarnings("unused")
public class StringUtilsTest extends AndroidTestCase {
  public void testGetStringFromInputStream() {
    // assertEquals("unko\nchinko",
    // StringUtils.getString(this.getContext().getResources().openRawResource(R.raw.animeone_mypage)));
  }

  public void testGetStringFromAssetFile() {
    assertEquals("unko\nchinko",
        StringUtils.getString(this.getContext().getAssets(), "test_data/is_test.txt"));
  }
}
