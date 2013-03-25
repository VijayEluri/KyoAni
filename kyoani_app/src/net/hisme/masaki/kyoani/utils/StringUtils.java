package net.hisme.masaki.kyoani.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.res.AssetManager;
import android.text.TextUtils;

/**
 * @author masarakki
 */
public class StringUtils {
  public static String getString(AssetManager asset, String filename) {
    try {
      return getString(asset.open(filename));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getString(InputStream in) {
    return getString(new InputStreamReader(in));
  }

  public static String getString(InputStreamReader in) {
    return getString(new BufferedReader(in));
  }

  private static String getString(BufferedReader in) {
    try {
      ArrayList<String> list = new ArrayList<String>();
      String line;
      while ((line = in.readLine()) != null) {
        list.add(line);
      }
      return TextUtils.join("\n", list);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
