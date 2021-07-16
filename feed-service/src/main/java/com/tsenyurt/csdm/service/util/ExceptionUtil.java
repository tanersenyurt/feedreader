package com.tsenyurt.csdm.service.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang3.StringUtils;

public class ExceptionUtil {
  public static String convertStackTraceToString(Throwable exception)
  {
    if (exception == null)
    {
      throw new IllegalArgumentException("Throwable exception sended null... ", exception);
    }
    StringWriter sw = new StringWriter();
    exception.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  public static String convertStackTraceToString(Throwable exception, int length)
  {
    return StringUtils.substring(convertStackTraceToString(exception), 0, length);
  }
}
