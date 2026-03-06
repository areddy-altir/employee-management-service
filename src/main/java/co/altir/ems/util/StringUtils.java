package co.altir.ems.util;

public final class StringUtils {

  private StringUtils() {}

  /**
   * Returns true if the string is null or blank (empty or whitespace only).
   */
  public static boolean isBlank(String s) {
    return s == null || s.isBlank();
  }
}
