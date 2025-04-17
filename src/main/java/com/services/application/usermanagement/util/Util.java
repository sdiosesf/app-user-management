package com.services.application.usermanagement.util;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Date;

@UtilityClass
public class Util {

  public static String getCurrentDateTime() {
    return new SimpleDateFormat(Constants.DATE_FORMAT_1).format(new Date());
  }
}
