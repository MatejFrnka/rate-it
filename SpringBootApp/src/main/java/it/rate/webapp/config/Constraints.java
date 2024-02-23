package it.rate.webapp.config;

import org.springframework.stereotype.Component;

@Component
public class Constraints {
  public static final int MIN_NAME_LENGTH = 3;
  public static final int MAX_NAME_LENGTH = 40;

  // Description length up to 1000
  public static final int MAX_DESCRIPTION_LENGTH = 1000;
  public static final int MAX_VARCHAR_LENGTH = 255;
  public static final int MIN_USERNAME_LENGTH = 3;
  public static final int MAX_USERNAME_LENGTH = 25;
  public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
  public static final int MAX_CATEGORIES_PER_INTEREST = 3;
  public static final int MAX_BIO_LENGTH = 150;
}
