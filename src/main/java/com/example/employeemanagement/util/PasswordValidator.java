package com.example.employeemanagement.util;

/**
 * Validates password strength. Never logs the password.
 */
public final class PasswordValidator {

  private static final int MIN_LENGTH = 8;
  private static final int MIN_UPPER = 1;
  private static final int MIN_LOWER = 1;
  private static final int MIN_DIGIT = 1;
  private static final int MIN_SPECIAL = 1;

  private PasswordValidator() {}

  /**
   * Returns true if password meets strength rules. Never log the password.
   */
  public static boolean isValid(String password) {
    if (password == null || password.length() < MIN_LENGTH) {
      return false;
    }
    int upper = 0, lower = 0, digit = 0, special = 0;
    for (char c : password.toCharArray()) {
      if (Character.isUpperCase(c)) upper++;
      else if (Character.isLowerCase(c)) lower++;
      else if (Character.isDigit(c)) digit++;
      else special++;
    }
    return upper >= MIN_UPPER && lower >= MIN_LOWER && digit >= MIN_DIGIT && special >= MIN_SPECIAL;
  }
}
