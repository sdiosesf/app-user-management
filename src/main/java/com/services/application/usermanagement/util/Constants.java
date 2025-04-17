package com.services.application.usermanagement.util;

public class Constants {

  public static final String APPLICATION_JSON = "application/json";
  public static final String DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  public static final String KEY_TOKEN = "token";
  public static final String KEY_MESSAGE = "message";
  public static final String KEY_ERROR = "error";
	
  public class Message {

	public static final String ERROR_INCORRECT_USERNAME_PASS = "Incorrect username and/or password";
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_GENERIC = "An unexpected error occurred";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String ERROR_PASSWORD_CRITERIA = "Password does not meet the required criteria";
  }
}
