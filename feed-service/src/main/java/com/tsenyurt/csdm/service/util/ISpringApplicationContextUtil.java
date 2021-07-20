package com.tsenyurt.csdm.service.util;

import org.springframework.context.ApplicationContext;

public interface ISpringApplicationContextUtil {
  public Object getBean(String beanName);

  public <T> T getBean(Class clazz);

  <T> T getBean(String qualifier, Class<T> clazz);

  @SuppressWarnings("unchecked")
  public String getBeanNameFromType(Class clazz);

  public String resolveValue(String key);

  ApplicationContext getAPPLICATION_CONTEXT();
}
